package com.fabian.practica_modulo7.ui.fragments // O tu paquete

import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fabian.practica_modulo7.R
import com.fabian.practica_modulo7.application.ExerciseApp
import com.fabian.practica_modulo7.data.remote.model.ExerciseDetailDto
import com.fabian.practica_modulo7.databinding.FragmentExerciseDetailBinding
import com.fabian.practica_modulo7.ui.viewmodels.DetailScreenState
import com.fabian.practica_modulo7.ui.viewmodels.ExerciseDetailViewModel
import com.fabian.practica_modulo7.ui.viewmodels.ViewModelFactory
import com.fabian.utils.Constants
import com.fabian.utils.isAtLeastAndroid

class ExerciseDetailFragment: Fragment() {

    private var _binding: FragmentExerciseDetailBinding? = null
    private val binding get() = _binding!!

    // Recibe los argumentos con SafeArgs
    private val args: ExerciseDetailFragmentArgs by navArgs()

    // Inyección del ViewModel usando la Factory
    private val viewModel: ExerciseDetailViewModel by viewModels {
        val app = requireActivity().application as ExerciseApp
        val repository = app.repository
        val preferencesRepository = app.preferencesRepository // Pasa ambos repos
        ViewModelFactory(repository, preferencesRepository)
    }

    // Variable para guardar los detalles actuales y usarlos en el listener del botón
    private var currentExerciseDetail: ExerciseDetailDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Listener para reintentar (llama al ViewModel)
        setFragmentResultListener("retry_key") { key, bundle ->
            if (bundle.getBoolean("retry")) {
                viewModel.loadExerciseDetail(args.exerciseId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener para el botón de regreso explícito que añadimos
        binding.btnBackExplicit.setOnClickListener {
            findNavController().popBackStack()
        }

        observeViewModelState() // Empieza a observar el LiveData

        // Llama a cargar el detalle si es necesario
        if (viewModel.uiState.value == null || viewModel.uiState.value is DetailScreenState.Loading) {
            viewModel.loadExerciseDetail(args.exerciseId)
        }
    }

    // Observa los cambios en el LiveData del ViewModel
    private fun observeViewModelState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            handleUiState(state) // Llama a la función para actualizar la UI
        }
    }

    // Actualiza la UI según el estado recibido
    private fun handleUiState(state: DetailScreenState) {
        when (state) {
            is DetailScreenState.Loading -> {
                binding.pbLoading.visibility = View.VISIBLE
                binding.btnMarkCompleted.isEnabled = false // Deshabilita botón mientras carga
                currentExerciseDetail = null // Limpia detalles viejos
            }
            is DetailScreenState.Success -> {
                binding.pbLoading.visibility = View.GONE
                updateUiWithDetails(state.exerciseDetail)
                // Guarda los detalles actuales
                currentExerciseDetail = state.exerciseDetail
                // Habilita el botón AHORA que tenemos los datos
                binding.btnMarkCompleted.isEnabled = true

                // --- CONFIGURA EL ONCLICKLISTENER DEL BOTÓN COMPLETADO ---
                binding.btnMarkCompleted.setOnClickListener {
                    // Llama al ViewModel para guardar, usando los datos guardados
                    currentExerciseDetail?.let { detail ->
                        // Asegúrate que llamas a la función correcta en el ViewModel
                        viewModel.markExerciseAsCompleted(args.exerciseId, detail.puntos)
                        // OPCIONAL: Deshabilitar botón después de guardar
                        binding.btnMarkCompleted.isEnabled = false
                        // OPCIONAL: Mostrar un Toast de confirmación
                        Toast.makeText(requireContext(), "¡Progreso guardado!", Toast.LENGTH_SHORT).show()
                    }
                }
                // --- FIN ONCLICKLISTENER ---
            }
            is DetailScreenState.Error -> {
                binding.pbLoading.visibility = View.GONE
                binding.btnMarkCompleted.isEnabled = false // Deshabilita si hay error
                currentExerciseDetail = null

                // Muestra mensaje de error (opcional)
                val errorMessage = if (state.args != null) getString(state.messageResId, *state.args.toTypedArray()) else getString(state.messageResId)
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()

                // Navega a la pantalla de error
                val action = ExerciseDetailFragmentDirections.actionDetailToError()
                findNavController().navigate(action)
            }
        }
    }
    private fun updateUiWithDetails(exerciseDetail: ExerciseDetailDto) {
        binding.tvTitle.text = exerciseDetail.name
        binding.tvDesc.text = exerciseDetail.descripcion
        binding.tvType.text = getString(R.string.format_tipo, exerciseDetail.tipoEjercicio)
        binding.tvRep.text = getString(R.string.format_repetitions, exerciseDetail.reps)
        binding.tvDifficulty.text = getString(R.string.format_difficulty, exerciseDetail.dificultad)
        binding.tvCal.text = getString(R.string.format_calories, exerciseDetail.caloriasQuemadas)
        binding.tvPoints.text = getString(R.string.format_points, exerciseDetail.puntos)

        // Justificación del texto (si aplica)
        isAtLeastAndroid(Build.VERSION_CODES.Q){
            binding.tvDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        // Carga de imagen con Glide
        Glide.with(requireActivity())
            .load(exerciseDetail.imagen)
            .into(binding.ivImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpia el binding
    }
}