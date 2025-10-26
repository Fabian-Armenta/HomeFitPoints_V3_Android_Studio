package com.fabian.practica_modulo7.data.remote.model

import com.google.gson.annotations.SerializedName

data class ExerciseDetailDto (
    @SerializedName("nombre")
    var name: String? = null,
    @SerializedName("imagen")
    var imagen: String? = null,
    @SerializedName("descripcion")
    var descripcion: String? = null,
    @SerializedName("tipo_ejercicio")
    var tipoEjercicio: String? = null,
    @SerializedName("repeticiones")
    var reps: Int = 0,
    @SerializedName("dificultad")
    var dificultad: String? = null,
    @SerializedName("calorias_quemadas")
    var caloriasQuemadas: Int = 0,
    @SerializedName("puntos")
    var puntos: Int = 0
)