package com.fabian.practica_modulo7.ui.viewmodels

import androidx.annotation.StringRes
import com.fabian.practica_modulo7.data.remote.model.ExerciseDetailDto
import com.fabian.practica_modulo7.R

sealed class DetailScreenState {
    object Loading : DetailScreenState()
    data class Success(val exerciseDetail: ExerciseDetailDto) : DetailScreenState()
    data class Error(@StringRes val messageResId: Int, val args: List<Any>? = null) : DetailScreenState()
}