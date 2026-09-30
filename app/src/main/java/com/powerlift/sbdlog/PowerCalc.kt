package com.powerlift.sbdlog

import com.powerlift.sbdlog.data.Exercise
import com.powerlift.sbdlog.data.WorkoutLog

object PowerCalc {

    fun estimateOneRm(weightKg: Double, reps: Int): Double {
        if (reps <= 1) return weightKg
        return weightKg * (1 + reps / 30.0)
    }

    fun trainingWeight(oneRm: Double, percent: Double): Double =
        oneRm * percent / 100.0

    fun roundToPlate(weightKg: Double): Double =
        (kotlin.math.round(weightKg / 2.5) * 2.5)

    fun volume(weightKg: Double, reps: Int): Double = weightKg * reps

    fun isPr(log: WorkoutLog, allLogs: List<WorkoutLog>): Boolean {
        val best = allLogs
            .filter { it.exercise == log.exercise && it.id != log.id }
            .maxOfOrNull { estimateOneRm(it.weightKg, it.reps) } ?: return true
        return estimateOneRm(log.weightKg, log.reps) > best
    }

    fun formatWeight(weightKg: Double): String =
        if (weightKg % 1.0 == 0.0) weightKg.toInt().toString()
        else String.format("%.1f", weightKg)
}
