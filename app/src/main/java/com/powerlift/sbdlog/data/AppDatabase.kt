package com.powerlift.sbdlog.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

class Converters {
    @TypeConverter
    fun fromExercise(value: Exercise): String = value.name

    @TypeConverter
    fun toExercise(value: String): Exercise = Exercise.valueOf(value)
}

@Database(entities = [WorkoutLog::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutLogDao(): WorkoutLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sbd-log.db"
                ).build().also { INSTANCE = it }
            }
    }
}
EOFcd ~/sbd-log

cat > app/src/main/java/com/powerlift/sbdlog/WorkoutViewModel.kt << 'EOF'
package com.powerlift.sbdlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.powerlift.sbdlog.data.Exercise
import com.powerlift.sbdlog.data.WorkoutLog
import com.powerlift.sbdlog.data.WorkoutLogDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutViewModel(private val dao: WorkoutLogDao) : ViewModel() {

    private val _selectedExercise = MutableStateFlow(Exercise.SQUAT)
    val selectedExercise: StateFlow<Exercise> = _selectedExercise.asStateFlow()

    val logs: StateFlow<List<WorkoutLog>> = dao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val topSets: StateFlow<Map<Exercise, WorkoutLog?>> = logs
        .let { flow ->
            kotlinx.coroutines.flow.combine(
                listOf(
                    Exercise.SQUAT, Exercise.BENCH, Exercise.DEADLIFT
                ).map { ex ->
                    flow.map { list -> ex to list.filter { it.exercise == ex }
                        .maxWithOrNull(compareBy({ it.weightKg }, { it.reps })) }
                }
            ) { arr -> arr.toMap() }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())
        }

    fun selectExercise(exercise: Exercise) {
        _selectedExercise.value = exercise
    }

    fun addLog(exercise: Exercise, weightKg: Double, reps: Int, rpe: Int?, note: String) {
        viewModelScope.launch {
            dao.insert(
                WorkoutLog(
                    exercise = exercise,
                    weightKg = weightKg,
                    reps = reps,
                    rpe = rpe,
                    note = note
                )
            )
        }
    }

    fun deleteLog(log: WorkoutLog) {
        viewModelScope.launch { dao.delete(log) }
    }

    class Factory(private val dao: WorkoutLogDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            WorkoutViewModel(dao) as T
    }
}
