package com.fabian.practica_modulo7.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fabian.practica_modulo7.data.ExerciseRepository
import com.fabian.practica_modulo7.data.local.UserPreferencesRepository

class ViewModelFactory(
    private val repository: ExerciseRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // PASAMOS AMBOS repositorios al ExerciseListViewModel
            return ExerciseListViewModel(repository, preferencesRepository) as T
        }
        if (modelClass.isAssignableFrom(ExerciseDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // PASAMOS AMBOS repositorios al ExerciseDetailViewModel
            return ExerciseDetailViewModel(repository, preferencesRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}