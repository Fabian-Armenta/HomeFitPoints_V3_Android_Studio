package com.fabian.practica_modulo7.data.local

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

// Objeto que contiene las claves (keys) para acceder a las preferencias
object UserPreferencesKeys {
    // Clave para guardar el puntaje total
    val TOTAL_POINTS = intPreferencesKey("total_points")

    // Clave para guardar los IDs de ejercicios completados
    val COMPLETED_EXERCISE_IDS = stringSetPreferencesKey("completed_exercise_ids")
}