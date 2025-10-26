package com.fabian.practica_modulo7.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabian.practica_modulo7.R
import com.fabian.practica_modulo7.data.ExerciseRepository
import com.fabian.practica_modulo7.data.local.UserPreferencesRepository // Importa el repo de preferencias
import kotlinx.coroutines.launch
import okio.IOException

// Asegúrate de importar tu clase de estado de detalle
import com.fabian.practica_modulo7.ui.viewmodels.DetailScreenState

// MODIFICADO: Añadir preferencesRepository al constructor
class ExerciseDetailViewModel(
    private val repository: ExerciseRepository,
    private val preferencesRepository: UserPreferencesRepository // <-- Asegúrate que esté aquí
) : ViewModel() {

    private val _uiState = MutableLiveData<DetailScreenState>(DetailScreenState.Loading)
    val uiState: LiveData<DetailScreenState> = _uiState

    // Función para cargar el detalle del ejercicio (sigue igual)
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

    // --- FUNCIÓN QUE FALTABA ---
    fun markExerciseAsCompleted(exerciseId: String, points: Int?) {
        // Validamos que los puntos no sean nulos
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
                // Aquí podrías emitir un nuevo estado si quieres (ej. ProgressSaved)
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error saving progress for ID: $exerciseId", e)
                // Emitir estado de error si falla el guardado
                val errorMessage = e.message ?: "Error al guardar"
                _uiState.postValue(DetailScreenState.Error(R.string.error_unexpected, listOf(errorMessage)))
            }
        }
    }
    // --- FIN FUNCIÓN QUE FALTABA ---
}