package com.lastwagon.core.data.content

import android.content.Context
import androidx.room.withTransaction
import com.lastwagon.core.data.ContentVersionEntity
import com.lastwagon.core.data.InspectionCategoryEntity
import com.lastwagon.core.data.InspectionItemEntity
import com.lastwagon.core.data.LastWagonDatabase
import com.lastwagon.core.data.encodeApplicabilityFlags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

/**
 * Loads authored, source-verified inspection content from the JSON asset and REPLACES the
 * inspection categories/items in Room (delete-then-insert), so the labeled sample rows do not
 * linger alongside authored content. Fail-closed: it refuses to seed if the content is
 * structurally invalid OR contains any non-VERIFIED item.
 *
 * NOTE: not yet wired into app startup — that switch (and making `ensureSampleContent` skip the
 * inspection tables once [REAL_CONTENT_MARKER] is present) is the gated B6 import step.
 */
class ContentLoader(private val database: LastWagonDatabase) {
    private val dao = database.dao()

    suspend fun seedFrom(context: Context, assetPath: String = DEFAULT_ASSET) = withContext(Dispatchers.IO) {
        val text = context.assets.open(assetPath).bufferedReader().use { it.readText() }
        val content = ContentJson.parse(text)
        // Runtime gate: structural validity (future-date check skipped to avoid device-clock
        // false positives — it is enforced at build time) + fail-closed on any non-VERIFIED item.
        val problems = ContentValidator.structuralProblems(content, LocalDate.now(), checkDates = false)
        require(problems.isEmpty()) { "Invalid inspection content: ${problems.joinToString("; ")}" }
        val blockers = ContentValidator.shipBlockers(content)
        require(blockers.isEmpty()) { "Refusing to seed non-VERIFIED items: ${blockers.map { it.id }}" }
        database.withTransaction {
            dao.deleteAllInspectionItems()
            dao.deleteAllInspectionCategories()
            dao.insertCategories(content.categories.sortedBy { it.sequence }.map { it.toEntity() })
            dao.insertItems(content.items.map { it.toEntity() })
            dao.insertContentVersion(ContentVersionEntity(REAL_CONTENT_MARKER, System.currentTimeMillis()))
        }
    }

    companion object {
        const val DEFAULT_ASSET = "content/inspection.json"
        /** content_versions row that marks authoritative (non-sample) inspection content as present. */
        const val REAL_CONTENT_MARKER = 1_000
    }
}

internal fun ContentCategory.toEntity() = InspectionCategoryEntity(id, title, sequence)

internal fun ContentItem.toEntity() = InspectionItemEntity(
    id = id, categoryId = categoryId, title = name, inspectFor = inspectFor,
    sampleDefects = defects.joinToString("\n"), sequence = sequence, isSample = false,
    sourceCitation = citations.joinToString("; ") { it.ref },
    verificationStatus = verificationStatus.name,
    applicabilityFlags = encodeApplicabilityFlags(applicability),
)
