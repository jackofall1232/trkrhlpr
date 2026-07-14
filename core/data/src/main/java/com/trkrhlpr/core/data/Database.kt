package com.trkrhlpr.core.data

import android.content.Context
import androidx.room.*
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
)

@Entity(tableName = "inspection_completions", indices = [Index("itemId")])
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

@Dao
interface TrkrHlprDao {
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
    @Query("SELECT * FROM questions WHERE type = 'daily' LIMIT 1")
    suspend fun getDailyQuestion(): QuestionEntity?
    @Query("SELECT COUNT(*) FROM test_attempts")
    fun observeTestAttemptCount(): Flow<Int>
    @Query("SELECT COUNT(*) FROM test_attempts WHERE correct = 1")
    fun observeCorrectTestAttemptCount(): Flow<Int>
    @Query("SELECT COUNT(*) FROM daily_completions")
    fun observeDailyCompletionCount(): Flow<Int>
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
    suspend fun insertContentVersion(value: ContentVersionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setInspectionCompletion(value: InspectionCompletionEntity)
    @Query("DELETE FROM inspection_completions WHERE itemId = :itemId")
    suspend fun deleteInspectionCompletion(itemId: String)
    @Insert suspend fun insertTestAttempt(value: TestAttemptEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyCompletion(value: DailyCompletionEntity)
    @Query("DELETE FROM inspection_completions") suspend fun clearInspectionProgress()
    @Query("DELETE FROM test_attempts") suspend fun clearTestProgress()
    @Query("DELETE FROM daily_completions") suspend fun clearDailyProgress()
    @Transaction suspend fun resetProgress() {
        clearInspectionProgress(); clearTestProgress(); clearDailyProgress()
    }
}

@Database(
    entities = [ContentVersionEntity::class, InspectionCategoryEntity::class,
        InspectionItemEntity::class, InspectionCompletionEntity::class,
        TestCategoryEntity::class, QuestionEntity::class, TestAttemptEntity::class,
        DailyCompletionEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TrkrHlprDatabase : RoomDatabase() {
    abstract fun dao(): TrkrHlprDao
    companion object {
        fun create(context: Context) =
            Room.databaseBuilder(context, TrkrHlprDatabase::class.java, "trkrhlpr.db").build()
    }
}
