package com.fabian.practica_modulo7.data.remote.model

import com.google.gson.annotations.SerializedName

data class ExerciseDto (

    @SerializedName("id")
    var id: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("title")
    var title: String? = null

)