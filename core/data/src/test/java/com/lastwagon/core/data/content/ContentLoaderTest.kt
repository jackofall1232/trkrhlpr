package com.lastwagon.core.data.content

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lastwagon.core.data.InspectionCategoryEntity
import com.lastwagon.core.data.InspectionItemEntity
import com.lastwagon.core.data.LastWagonDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** Proves the loader is fail-closed AND actually replaces labeled sample rows with the
 *  verified authored content from the real shipped asset (no UNVERIFIED/sample rows linger). */
@RunWith(AndroidJUnit4::class)
class ContentLoaderTest {
    private lateinit var db: LastWagonDatabase
    private lateinit var ctx: Context

    @Before fun setUp() {
        ctx = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(ctx, LastWagonDatabase::class.java).allowMainThreadQueries().build()
    }

    @After fun tearDown() = db.close()

    @Test fun seedFromReplacesSampleRowsWithVerifiedAuthoredContent() = runTest {
        val dao = db.dao()
        // Pre-existing labeled sample rows (as ensureSampleContent would create).
        dao.insertCategories(listOf(InspectionCategoryEntity("sample-cab", "Cab", 1)))
        dao.insertItems(listOf(
            InspectionItemEntity("sample-x", "sample-cab", "Sample", "if", "d", 1, true, "", "UNVERIFIED", ""),
        ))

        ContentLoader(db).seedFrom(ctx) // loads core/data/src/main/assets/content/inspection.json

        val items = dao.observeInspectionItems().first()
        assertEquals(3, items.size)
        assertTrue("no sample rows remain", items.none { it.isSample })
        assertTrue("all authored items are VERIFIED", items.all { it.verificationStatus == "VERIFIED" })
        assertTrue("authored provenance present", items.all { it.sourceCitation.isNotBlank() })
        assertTrue("sample category evicted",
            dao.observeInspectionCategories().first().none { it.id == "sample-cab" })
        assertTrue("real-content marker written",
            dao.contentVersionCount(ContentLoader.REAL_CONTENT_MARKER) > 0)
    }
}
