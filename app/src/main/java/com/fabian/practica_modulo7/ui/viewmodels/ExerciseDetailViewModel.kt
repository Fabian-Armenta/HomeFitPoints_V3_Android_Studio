package com.fabian.practica_modulo7.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabian.practica_modulo7.R
import com.fabian.practica_modulo7.data.ExerciseRepository
import com.fabian.practica_modulo7.data.local.UserPreferencesRepository
import kotlinx.coroutines.launch
import okio.IOException

class ExerciseDetailViewModel(
    private val repository: ExerciseRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<DetailScreenState>(DetailScreenState.Loading)
    val uiState: LiveData<DetailScreenState> = _uiState
    fun loadExerciseDetail(exerciseId: String?) {
        if (exerciseId == null) {
            _uiState.postValue(DetailScreenState.Error(R.string.error_unexpected, listOf("ID de ejercicio nulo")))
            return
        }
        _uiState.postValue(DetailScreenState.Loading)
        viewModelScope.launch {
            try {
                val detail = repository.getExerciseDetailApiary(exerciseId)
                _uiState.postValue(DetailScreenState.Success(detail))
            } catch (e: IOException) {
                _uiState.postValue(DetailScreenState.Error(R.string.error_connection))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Desconocido"
                _uiState.postValue(DetailScreenState.Error(R.string.error_unexpected, listOf(errorMessage)))
            }
        }
    }

    fun markExerciseAsCompleted(exerciseId: String, points: Int?) {
        if (points == null) {
            Log.e("DetailViewModel", "Points are null, cannot save progress for ID: $exerciseId")
            return
        }

        viewModelScope.launch {
            try {
                // Llama a las funciones del repo de preferencias
                preferencesRepository.addCompletedExerciseId(exerciseId)
                preferencesRepository.addPoints(points)
                Log.d("DetailViewModel", "Progress saved for ID: $exerciseId, Points: $points")
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error saving progress for ID: $exerciseId", e)
                val errorMessage = e.message ?: "Error al guardar"
                _uiState.postValue(DetailScreenState.Error(R.string.error_unexpected, listOf(errorMessage)))
            }
        }
    }
}