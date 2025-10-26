package com.fabian.practica_modulo7

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // <-- AÑADE ESTA IMPORTACIÓN

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // --- AÑADE ESTA LÍNEA ---
        installSplashScreen()
        // --- FIN DE LO AÑADIDO ---

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}