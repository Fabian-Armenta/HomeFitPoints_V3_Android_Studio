package com.fabian.practica_modulo7.ui.viewmodels

import androidx.annotation.StringRes
import com.fabian.practica_modulo7.data.remote.model.ExerciseDto

sealed class ListScreenState {
    object Loading : ListScreenState()
    data class Success(
        val exercises: List<ExerciseDto>,
        val completedIds: Set<String>
    ) : ListScreenState()
    data class Error(@StringRes val messageResId: Int, val args: List<Any>? = null) : ListScreenState()
}