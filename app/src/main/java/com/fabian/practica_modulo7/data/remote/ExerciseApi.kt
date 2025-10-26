package com.fabian.practica_modulo7.data.remote

import com.fabian.practica_modulo7.data.remote.model.ExerciseDetailDto
import com.fabian.practica_modulo7.data.remote.model.ExerciseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseApi {

    @GET("Ejercicios/Ejercicios_list")
    suspend fun getExerciseApiary(): List<ExerciseDto>

    @GET("Ejercicios/Ejercicio_detail/{id}")
    suspend fun getExerciseDetailApiary(
        @Path("id") id: String?
    ): ExerciseDetailDto
}