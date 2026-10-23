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
}

@Database(entities = [VerdictEntity::class, BookmarkEntity::class], version = 1, exportSchema = false)
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
