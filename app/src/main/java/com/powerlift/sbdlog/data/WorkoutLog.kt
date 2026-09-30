package com.powerlift.sbdlog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Exercise(val label: String) {
    SQUAT("深蹲"),
    BENCH("卧推"),
    DEADLIFT("硬拉")
}

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exercise: Exercise,
    val weightKg: Double,
    val reps: Int,
    val rpe: Int? = null,
    val isPr: Boolean = false,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
