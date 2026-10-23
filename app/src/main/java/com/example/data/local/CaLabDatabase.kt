package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.LabVerdict
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray

@Entity(tableName = "verdicts")
data class VerdictEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val paperName: String,
    val topicName: String,
    val masteryScore: Int,
    val nailedJson: String,
    val slippedJson: String,
    val examinerTip: String,
    val nextAction: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): LabVerdict {
        val nailedList = mutableListOf<String>()
        val slippedList = mutableListOf<String>()
        try {
            val nailedArr = JSONArray(nailedJson)
            for (i in 0 until nailedArr.length()) nailedList.add(nailedArr.getString(i))
            val slippedArr = JSONArray(slippedJson)
            for (i in 0 until slippedArr.length()) slippedList.add(slippedArr.getString(i))
        } catch (_: Exception) {}

        return LabVerdict(
            id = id,
            masteryScore = masteryScore,
            paperName = paperName,
            topicName = topicName,
            whatYouNailed = nailedList,
            whatSlipped = slippedList,
            icaiExaminerTip = examinerTip,
            nextAction = nextAction,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomain(verdict: LabVerdict): VerdictEntity {
            val nailedArr = JSONArray()
            verdict.whatYouNailed.forEach { nailedArr.put(it) }
            val slippedArr = JSONArray()
            verdict.whatSlipped.forEach { slippedArr.put(it) }

            return VerdictEntity(
                paperName = verdict.paperName,
                topicName = verdict.topicName,
                masteryScore = verdict.masteryScore,
                nailedJson = nailedArr.toString(),
                slippedJson = slippedArr.toString(),
                examinerTip = verdict.icaiExaminerTip,
                nextAction = verdict.nextAction
            )
        }
    }
}

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val type: String, // "DIFFERENCE", "SCENARIO", "TOPIC"
    val title: String,
    val paperName: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "topic_progress")
data class TopicProgressEntity(
    @PrimaryKey val topicId: String,
    val writtenSolved: Int = 0,
    val mcqAttempted: Int = 0,
    val mcqCorrect: Int = 0,
    val oralRecallsCompleted: Int = 0,
    val lastRevisedAt: Long = 0L
)

@Entity(tableName = "error_log")
data class ErrorLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val paperName: String,
    val topicId: String,
    val topicName: String,
    val priorityTierName: String,
    val questionOrConcept: String,
    val userMistake: String,
    val correctRule: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_differences")
data class CustomDifferenceEntity(
    @PrimaryKey val id: String,
    val paperName: String,
    val title: String,
    val conceptA: String,
    val conceptB: String,
    val rowsJson: String,
    val memoryHook: String,
    val examinerTrapNote: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): com.example.data.model.ConceptDifference {
        val rowsList = mutableListOf<com.example.data.model.ComparisonRow>()
        try {
            val arr = org.json.JSONArray(rowsJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                rowsList.add(
                    com.example.data.model.ComparisonRow(
                        basisOfDifference = obj.optString("basisOfDifference", "Point ${i + 1}"),
                        conceptAValue = obj.optString("conceptAValue", "-"),
                        conceptBValue = obj.optString("conceptBValue", "-")
                    )
                )
            }
        } catch (_: Exception) {}

        val paper = com.example.data.model.PaperCode.fromCode(paperName)

        return com.example.data.model.ConceptDifference(
            id = id,
            paper = paper,
            title = title,
            conceptA = conceptA,
            conceptB = conceptB,
            comparisonPoints = if (rowsList.isNotEmpty()) rowsList else listOf(
                com.example.data.model.ComparisonRow("Comparison", "Check ICAI Material", "Check ICAI Material")
            ),
            memoryHook = memoryHook,
            examinerTrapNote = examinerTrapNote
        )
    }

    companion object {
        fun fromDomain(diff: com.example.data.model.ConceptDifference): CustomDifferenceEntity {
            val arr = org.json.JSONArray()
            diff.comparisonPoints.forEach { row ->
                val obj = org.json.JSONObject()
                obj.put("basisOfDifference", row.basisOfDifference)
                obj.put("conceptAValue", row.conceptAValue)
                obj.put("conceptBValue", row.conceptBValue)
                arr.put(obj)
            }

            return CustomDifferenceEntity(
                id = diff.id,
                paperName = diff.paper.code,
                title = diff.title,
                conceptA = diff.conceptA,
                conceptB = diff.conceptB,
                rowsJson = arr.toString(),
                memoryHook = diff.memoryHook,
                examinerTrapNote = diff.examinerTrapNote
            )
        }
    }
}

@Dao
interface StudyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerdict(verdict: VerdictEntity): Long

    @Query("SELECT * FROM verdicts ORDER BY createdAt DESC")
    fun getAllVerdicts(): Flow<List<VerdictEntity>>

    @Query("SELECT * FROM verdicts ORDER BY createdAt DESC")
    suspend fun getVerdictsList(): List<VerdictEntity>

    @Query("SELECT AVG(masteryScore) FROM verdicts")
    fun getAverageScore(): Flow<Double?>

    @Query("SELECT AVG(masteryScore) FROM verdicts")
    suspend fun getAverageScoreValue(): Double?

    @Query("SELECT COUNT(*) FROM verdicts")
    fun getTotalSessionsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM verdicts")
    suspend fun getTotalSessionsCountValue(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun removeBookmark(id: String)

    @Query("SELECT * FROM bookmarks ORDER BY savedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    suspend fun isBookmarked(id: String): Boolean

    // Topic Progress tracking
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: TopicProgressEntity)

    @Query("SELECT * FROM topic_progress WHERE topicId = :topicId")
    suspend fun getProgressForTopic(topicId: String): TopicProgressEntity?

    @Query("SELECT * FROM topic_progress")
    fun getAllProgress(): Flow<List<TopicProgressEntity>>

    @Query("SELECT * FROM topic_progress")
    suspend fun getAllProgressList(): List<TopicProgressEntity>

    @Query("UPDATE topic_progress SET writtenSolved = writtenSolved + 1, lastRevisedAt = :timestamp WHERE topicId = :topicId")
    suspend fun incrementWrittenCount(topicId: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE topic_progress SET oralRecallsCompleted = oralRecallsCompleted + 1, lastRevisedAt = :timestamp WHERE topicId = :topicId")
    suspend fun incrementOralRecallCount(topicId: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE topic_progress SET mcqAttempted = mcqAttempted + :attempted, mcqCorrect = mcqCorrect + :correct, lastRevisedAt = :timestamp WHERE topicId = :topicId")
    suspend fun recordMcqResult(topicId: String, attempted: Int, correct: Int, timestamp: Long = System.currentTimeMillis())

    // Error Log tracking
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertErrorLog(errorLog: ErrorLogEntity): Long

    @Query("SELECT * FROM error_log ORDER BY timestamp DESC")
    fun getAllErrorLogs(): Flow<List<ErrorLogEntity>>

    @Query("SELECT * FROM error_log ORDER BY timestamp DESC")
    suspend fun getErrorLogsList(): List<ErrorLogEntity>

    @Query("DELETE FROM error_log WHERE id = :id")
    suspend fun deleteErrorLog(id: Long)

    // Custom Differences tracking
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomDifference(diff: CustomDifferenceEntity)

    @Query("SELECT * FROM custom_differences ORDER BY createdAt DESC")
    fun getAllCustomDifferences(): Flow<List<CustomDifferenceEntity>>

    @Query("SELECT * FROM custom_differences ORDER BY createdAt DESC")
    suspend fun getCustomDifferencesList(): List<CustomDifferenceEntity>
}

@Database(entities = [VerdictEntity::class, BookmarkEntity::class, TopicProgressEntity::class, ErrorLogEntity::class, CustomDifferenceEntity::class], version = 4, exportSchema = false)
abstract class CaLabDatabase : RoomDatabase() {
    abstract fun studyDao(): StudyDao

    companion object {
        @Volatile
        private var INSTANCE: CaLabDatabase? = null

        fun getInstance(context: android.content.Context): CaLabDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    CaLabDatabase::class.java,
                    "ca_lab_database.db"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
