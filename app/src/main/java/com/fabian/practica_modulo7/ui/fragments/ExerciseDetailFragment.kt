package com.fabian.practica_modulo7.ui.fragments

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
import com.fabian.utils.isAtLeastAndroid

class ExerciseDetailFragment: Fragment() {

    private var _binding: FragmentExerciseDetailBinding? = null
    private val binding get() = _binding!!

    // Recibe los argumentos con SafeArgs
    private val args: ExerciseDetailFragmentArgs by navArgs()
    private val viewModel: ExerciseDetailViewModel by viewModels {
        val app = requireActivity().application as ExerciseApp
        val repository = app.repository
        val preferencesRepository = app.preferencesRepository
        ViewModelFactory(repository, preferencesRepository)
    }
    private var currentExerciseDetail: ExerciseDetailDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.btnBackExplicit.setOnClickListener {
            findNavController().popBackStack()
        }

        observeViewModelState()

        if (viewModel.uiState.value == null || viewModel.uiState.value is DetailScreenState.Loading) {
            viewModel.loadExerciseDetail(args.exerciseId)
        }
    }

    private fun observeViewModelState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            handleUiState(state)
        }
    }

    private fun handleUiState(state: DetailScreenState) {
        when (state) {
            is DetailScreenState.Loading -> {
                binding.pbLoading.visibility = View.VISIBLE
                binding.btnMarkCompleted.isEnabled = false
                currentExerciseDetail = null
            }
            is DetailScreenState.Success -> {
                binding.pbLoading.visibility = View.GONE
                updateUiWithDetails(state.exerciseDetail)
                currentExerciseDetail = state.exerciseDetail
                binding.btnMarkCompleted.isEnabled = true
                binding.btnMarkCompleted.setOnClickListener {
                    // Llama al ViewModel para guardar, usando los datos guardados
                    currentExerciseDetail?.let { detail ->
                        viewModel.markExerciseAsCompleted(args.exerciseId, detail.puntos)
                        binding.btnMarkCompleted.isEnabled = false
                        //Toast de confirmación
                        Toast.makeText(requireContext(), "¡Progreso guardado!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is DetailScreenState.Error -> {
                binding.pbLoading.visibility = View.GONE
                binding.btnMarkCompleted.isEnabled = false
                currentExerciseDetail = null

                val errorMessage = if (state.args != null) getString(state.messageResId, *state.args.toTypedArray()) else getString(state.messageResId)
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()

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

        isAtLeastAndroid(Build.VERSION_CODES.Q){
            binding.tvDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        Glide.with(requireActivity())
            .load(exerciseDetail.imagen)
            .into(binding.ivImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}