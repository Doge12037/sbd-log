package com.powerlift.sbdlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutLogDao {

    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs WHERE exercise = :exercise ORDER BY timestamp DESC")
    fun observeByExercise(exercise: Exercise): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs WHERE exercise = :exercise ORDER BY weightKg DESC, reps DESC LIMIT 1")
    suspend fun getTopSet(exercise: Exercise): WorkoutLog?

    @Insert
    suspend fun insert(log: WorkoutLog): Long

    @Delete
    suspend fun delete(log: WorkoutLog)

    @Query("DELETE FROM workout_logs")
    suspend fun clearAll()
}
