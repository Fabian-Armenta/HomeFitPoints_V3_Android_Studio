package com.fabian.practica_modulo7.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabian.practica_modulo7.R
import com.fabian.practica_modulo7.data.ExerciseRepository
import com.fabian.practica_modulo7.data.local.UserPreferencesRepository
import com.fabian.practica_modulo7.data.remote.model.ExerciseDto
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException

class ExerciseListViewModel(
    private val repository: ExerciseRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<ListScreenState>(ListScreenState.Loading)
    val uiState: LiveData<ListScreenState> = _uiState

    // Función para cargar los ejercicios (ahora combina API y DataStore)
    fun loadExercises() {
        _uiState.postValue(ListScreenState.Loading)

        viewModelScope.launch {
            try {
                val exercisesFlow: Flow<List<ExerciseDto>> = flow { emit(repository.getExercisesApiary()) }

                // Usamos combine para esperar a que AMBOS (API y DataStore) emitan un valor
                exercisesFlow.combine(preferencesRepository.completedExerciseIdsFlow) { exercisesFromApi, completedIds ->
                    // Cuando tengamos ambos, emitimos el estado Success combinado
                    _uiState.postValue(ListScreenState.Success(exercisesFromApi, completedIds))
                }
                    .catch { e ->
                        if (e is IOException) {
                            _uiState.postValue(ListScreenState.Error(R.string.error_connection))
                        } else {
                            val errorMessage = e.message ?: "Desconocido"
                            _uiState.postValue(ListScreenState.Error(R.string.error_unexpected, listOf(errorMessage)))
                        }
                    }
                    .collect()

            } catch (e: Exception) {
                val errorMessage = e.message ?: "Desconocido"
                _uiState.postValue(ListScreenState.Error(R.string.error_unexpected, listOf(errorMessage)))
            }
        }
    }
}