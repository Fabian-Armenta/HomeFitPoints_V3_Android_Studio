package com.fabian.practica_modulo7.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException



class UserPreferencesRepository(context: Context) {
    private val TAG = "UserPreferencesRepo"
    private val dataStore = context.dataStore
    val totalPointsFlow: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error en puntos totales.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferencesKeys.TOTAL_POINTS] ?: 0
        }
    val completedExerciseIdsFlow: Flow<Set<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error en IDs.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferencesKeys.COMPLETED_EXERCISE_IDS] ?: emptySet()
        }
    suspend fun addCompletedExerciseId(exerciseId: String) {
        dataStore.edit { preferences ->
            val currentIds = preferences[UserPreferencesKeys.COMPLETED_EXERCISE_IDS] ?: emptySet()
            preferences[UserPreferencesKeys.COMPLETED_EXERCISE_IDS] = currentIds + exerciseId
            Log.d(TAG, "Added exercise ID: $exerciseId. New set: ${currentIds + exerciseId}")
        }
    }

    suspend fun addPoints(pointsToAdd: Int) {
        dataStore.edit { preferences ->
            val currentPoints = preferences[UserPreferencesKeys.TOTAL_POINTS] ?: 0
            preferences[UserPreferencesKeys.TOTAL_POINTS] = currentPoints + pointsToAdd
            Log.d(TAG, "Added points: $pointsToAdd. New total: ${currentPoints + pointsToAdd}")
        }
    }
    suspend fun clearProgress() {
        dataStore.edit { preferences ->
            preferences.remove(UserPreferencesKeys.TOTAL_POINTS)
            preferences.remove(UserPreferencesKeys.COMPLETED_EXERCISE_IDS)
            Log.d(TAG, "User progress cleared.")
        }
    }
}