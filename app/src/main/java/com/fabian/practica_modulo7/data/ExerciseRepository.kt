package com.fabian.practica_modulo7.data

import com.fabian.practica_modulo7.data.remote.ExerciseApi
import com.fabian.practica_modulo7.data.remote.model.ExerciseDetailDto
import com.fabian.practica_modulo7.data.remote.model.ExerciseDto
import retrofit2.Retrofit

class ExerciseRepository(private val retrofit: Retrofit) {
    private val exerciseApi = retrofit.create(ExerciseApi::class.java)

    suspend fun getExercisesApiary(): List<ExerciseDto> = exerciseApi.getExerciseApiary()
    suspend fun getExerciseDetailApiary(id: String?): ExerciseDetailDto = exerciseApi.getExerciseDetailApiary(id)
}