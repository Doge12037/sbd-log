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
EOFcd ~/sbd-log

cat > app/src/main/java/com/powerlift/sbdlog/ui/theme/Theme.kt << 'EOF'
package com.powerlift.sbdlog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B5E20),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = Color(0xFF002204),
    secondary = Color(0xFF4E6352),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD0E8D2),
    onSecondaryContainer = Color(0xFF0B1F12),
    tertiary = Color(0xFF39656D),
    onTertiary = Color.White,
    background = Color(0xFFFCFDF7),
    onBackground = Color(0xFF1A1C19),
    surface = Color(0xFFFCFDF7),
    onSurface = Color(0xFF1A1C19),
    surfaceVariant = Color(0xFFDDE5DB),
    onSurfaceVariant = Color(0xFF414942),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color(0xFF00390A),
    primaryContainer = Color(0xFF005314),
    onPrimaryContainer = Color(0xFFA5D6A7),
    secondary = Color(0xFFB4CCB7),
    onSecondary = Color(0xFF203526),
    secondaryContainer = Color(0xFF364B3B),
    onSecondaryContainer = Color(0xFFD0E8D2),
    tertiary = Color(0xFFA1CED7),
    onTertiary = Color(0xFF00363E),
    background = Color(0xFF1A1C19),
    onBackground = Color(0xFFE2E3DD),
    surface = Color(0xFF1A1C19),
    onSurface = Color(0xFFE2E3DD),
    surfaceVariant = Color(0xFF414942),
    onSurfaceVariant = Color(0xFFC1C9BF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

@Composable
fun SBDLogTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
