package com.fabian.practica_modulo7.application

import android.app.Application
import com.fabian.practica_modulo7.data.ExerciseRepository
import com.fabian.practica_modulo7.data.local.UserPreferencesRepository
import com.fabian.practica_modulo7.data.remote.RetrofitHelper

class ExerciseApp: Application() {
    lateinit var repository: ExerciseRepository
    lateinit var preferencesRepository: UserPreferencesRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val retrofit = RetrofitHelper.getRetrofitInstance()
        repository = ExerciseRepository(retrofit)
        preferencesRepository = UserPreferencesRepository(this)

    }
}