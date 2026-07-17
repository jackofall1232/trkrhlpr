package com.lastwagon.core.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "content_versions")
data class ContentVersionEntity(@PrimaryKey val version: Int, val installedAtEpochMillis: Long)

@Entity(tableName = "inspection_categories")
data class InspectionCategoryEntity(@PrimaryKey val id: String, val title: String, val sequence: Int)

@Entity(
    tableName = "inspection_items",
    foreignKeys = [ForeignKey(
        entity = InspectionCategoryEntity::class, parentColumns = ["id"], childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("categoryId")],
)
data class InspectionItemEntity(
    @PrimaryKey val id: String, val categoryId: String, val title: String,
    val inspectFor: String, val sampleDefects: String, val sequence: Int, val isSample: Boolean,
    // Content-provenance and applicability metadata (schema v2). Defaults keep the additive
    // 1 -> 2 migration and any legacy row valid without back-filled content.
    @ColumnInfo(defaultValue = "") val sourceCitation: String = "",
    @ColumnInfo(defaultValue = "UNVERIFIED") val verificationStatus: String = "UNVERIFIED",
    @ColumnInfo(defaultValue = "") val applicabilityFlags: String = "",
)

@Entity(tableName = "inspection_completions")
data class InspectionCompletionEntity(@PrimaryKey val itemId: String, val completedAtEpochMillis: Long)

@Entity(tableName = "test_categories")
data class TestCategoryEntity(
    @PrimaryKey val id: String, val title: String, val kind: String, val sequence: Int,
)

@Entity(tableName = "questions", indices = [Index("categoryId")])
data class QuestionEntity(
    @PrimaryKey val id: String, val categoryId: String, val prompt: String,
    val answersEncoded: String, val correctAnswerId: String, val explanation: String,
    val type: String, val isSample: Boolean,
)

@Entity(tableName = "test_attempts", indices = [Index("questionId")])
data class TestAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, val questionId: String,
    val selectedAnswerId: String, val correct: Boolean, val answeredAtEpochMillis: Long,
)

@Entity(tableName = "daily_completions")
data class DailyCompletionEntity(
    @PrimaryKey val questionId: String, val correct: Boolean, val completedAtEpochMillis: Long,
)

/** Day-keyed daily-question completion (schema v3): one row per UTC day, enabling streak
 *  counting. Keyed by epochDay so repeating the same question on different days is preserved. */
@Entity(tableName = "daily_day_completions")
data class DailyDayCompletionEntity(
    @PrimaryKey val epochDay: Long, val questionId: String, val correct: Boolean,
    val completedAtEpochMillis: Long,
)

/** Local mock-exam history (schema v4). Stores only a factual score — no readiness/pass-fail. */
@Entity(tableName = "exam_results")
data class ExamResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, val categoryTitle: String,
    val correct: Int, val total: Int, val completedAtEpochMillis: Long,
)

/** Truck-stop directory record (schema v5, Track C). Amenity columns are three-state
 *  nullable booleans: NULL means the source did not record the fact — unknown, never
 *  treated as absent. */
@Entity(tableName = "truck_stops", indices = [Index("state")])
data class TruckStopEntity(
    @PrimaryKey val id: String, val name: String, val state: String, val highway: String,
    val latitude: Double, val longitude: Double,
    val truckParkingSpaces: Int?, val hasDiesel: Boolean?, val hasShowers: Boolean?,
    val hasFood: Boolean?, val hasRepair: Boolean?, val isSample: Boolean,
    @ColumnInfo(defaultValue = "") val sourceCitation: String = "",
    @ColumnInfo(defaultValue = "UNVERIFIED") val verificationStatus: String = "UNVERIFIED",
    @ColumnInfo(defaultValue = "") val datasetVintage: String = "",
)

data class TestAttemptStats(val total: Int, val correct: Int)

@Dao
interface LastWagonDao {
    @Query("SELECT * FROM inspection_categories ORDER BY sequence")
    fun observeInspectionCategories(): Flow<List<InspectionCategoryEntity>>
    @Query("SELECT * FROM inspection_items ORDER BY sequence")
    fun observeInspectionItems(): Flow<List<InspectionItemEntity>>
    @Query("SELECT itemId FROM inspection_completions")
    fun observeCompletedInspectionIds(): Flow<List<String>>
    @Query("SELECT * FROM test_categories ORDER BY sequence")
    fun observeTestCategories(): Flow<List<TestCategoryEntity>>
    @Query("SELECT * FROM questions WHERE categoryId = :categoryId AND type = 'practice' LIMIT 1")
    suspend fun getPracticeQuestion(categoryId: String): QuestionEntity?
    @Query("SELECT * FROM questions WHERE categoryId = :categoryId AND type = 'practice' ORDER BY id")
    suspend fun getPracticeQuestions(categoryId: String): List<QuestionEntity>
    @Query("SELECT * FROM questions WHERE type = 'daily' LIMIT 1")
    suspend fun getDailyQuestion(): QuestionEntity?
    @Query("SELECT * FROM questions WHERE type = 'daily' ORDER BY id")
    suspend fun getDailyQuestions(): List<QuestionEntity>
    @Query(
        """
        SELECT COUNT(*) AS total,
               COALESCE(SUM(CASE WHEN correct = 1 THEN 1 ELSE 0 END), 0) AS correct
        FROM test_attempts
        """,
    )
    fun observeTestAttemptStats(): Flow<TestAttemptStats>
    @Query("SELECT COUNT(*) FROM daily_completions")
    fun observeDailyCompletionCount(): Flow<Int>
    @Query("SELECT epochDay FROM daily_day_completions")
    fun observeCompletedDailyDays(): Flow<List<Long>>
    @Query("SELECT * FROM exam_results ORDER BY completedAtEpochMillis DESC, id DESC")
    fun observeExamResults(): Flow<List<ExamResultEntity>>
    @Query("SELECT * FROM truck_stops ORDER BY state, name")
    fun observeTruckStops(): Flow<List<TruckStopEntity>>
    @Query("SELECT COUNT(*) FROM content_versions WHERE version = :version")
    suspend fun contentVersionCount(version: Int): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(values: List<InspectionCategoryEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(values: List<InspectionItemEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestCategories(values: List<TestCategoryEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(values: List<QuestionEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTruckStops(values: List<TruckStopEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentVersion(value: ContentVersionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setInspectionCompletion(value: InspectionCompletionEntity)
    @Query("DELETE FROM inspection_completions WHERE itemId = :itemId")
    suspend fun deleteInspectionCompletion(itemId: String)
    @Insert suspend fun insertTestAttempt(value: TestAttemptEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyCompletion(value: DailyCompletionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyDayCompletion(value: DailyDayCompletionEntity)
    @Insert suspend fun insertExamResult(value: ExamResultEntity)
    @Query("DELETE FROM inspection_completions") suspend fun clearInspectionProgress()
    @Query("DELETE FROM test_attempts") suspend fun clearTestProgress()
    @Query("DELETE FROM daily_completions") suspend fun clearDailyProgress()
    @Query("DELETE FROM daily_day_completions") suspend fun clearDailyDayProgress()
    @Query("DELETE FROM exam_results") suspend fun clearExamResults()
    @Transaction suspend fun resetProgress() {
        clearInspectionProgress(); clearTestProgress(); clearDailyProgress()
        clearDailyDayProgress(); clearExamResults()
    }
}

/**
 * Schema v1 -> v2: adds content-provenance and applicability columns to inspection_items
 * (source citation, verification status, applicability flags). Additive and non-destructive —
 * existing rows and local progress are preserved; the NOT NULL DEFAULTs match the entity's
 * @ColumnInfo(defaultValue = ...) so Room's post-migration schema validation passes.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE inspection_items ADD COLUMN sourceCitation TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE inspection_items ADD COLUMN verificationStatus TEXT NOT NULL DEFAULT 'UNVERIFIED'")
        db.execSQL("ALTER TABLE inspection_items ADD COLUMN applicabilityFlags TEXT NOT NULL DEFAULT ''")
    }
}

/**
 * Schema v2 -> v3: adds the day-keyed daily_day_completions table used for streak counting.
 * Additive and non-destructive — the legacy daily_completions table is left intact.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS daily_day_completions (" +
                "epochDay INTEGER NOT NULL, questionId TEXT NOT NULL, " +
                "correct INTEGER NOT NULL, completedAtEpochMillis INTEGER NOT NULL, " +
                "PRIMARY KEY(epochDay))",
        )
    }
}

/** Schema v3 -> v4: adds the exam_results table for local mock-exam history. Additive. The
 *  column SQL must match Room's generated createSql for the autoGenerate PK (rowid alias). */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `exam_results` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `categoryTitle` TEXT NOT NULL, " +
                "`correct` INTEGER NOT NULL, `total` INTEGER NOT NULL, " +
                "`completedAtEpochMillis` INTEGER NOT NULL)",
        )
    }
}

/** Schema v4 -> v5: adds the truck_stops directory table (Track C). Additive and
 *  non-destructive. Amenity columns are intentionally nullable (NULL = unknown); the
 *  NOT NULL DEFAULT columns mirror the entity's @ColumnInfo defaults so Room's
 *  post-migration schema validation passes. */
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `truck_stops` (" +
                "`id` TEXT NOT NULL, `name` TEXT NOT NULL, `state` TEXT NOT NULL, " +
                "`highway` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, " +
                "`truckParkingSpaces` INTEGER, `hasDiesel` INTEGER, `hasShowers` INTEGER, " +
                "`hasFood` INTEGER, `hasRepair` INTEGER, `isSample` INTEGER NOT NULL, " +
                "`sourceCitation` TEXT NOT NULL DEFAULT '', " +
                "`verificationStatus` TEXT NOT NULL DEFAULT 'UNVERIFIED', " +
                "`datasetVintage` TEXT NOT NULL DEFAULT '', " +
                "PRIMARY KEY(`id`))",
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_truck_stops_state` ON `truck_stops` (`state`)")
    }
}

@Database(
    entities = [ContentVersionEntity::class, InspectionCategoryEntity::class,
        InspectionItemEntity::class, InspectionCompletionEntity::class,
        TestCategoryEntity::class, QuestionEntity::class, TestAttemptEntity::class,
        DailyCompletionEntity::class, DailyDayCompletionEntity::class, ExamResultEntity::class,
        TruckStopEntity::class],
    version = 5,
    exportSchema = true,
)
abstract class LastWagonDatabase : RoomDatabase() {
    abstract fun dao(): LastWagonDao
    companion object {
        fun create(context: Context) =
            Room.databaseBuilder(context, LastWagonDatabase::class.java, "lastwagon.db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                .build()
    }
}
