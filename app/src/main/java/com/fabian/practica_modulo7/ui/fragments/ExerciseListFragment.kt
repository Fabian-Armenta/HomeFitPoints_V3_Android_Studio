package com.fabian.practica_modulo7.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabian.practica_modulo7.application.ExerciseApp
import com.fabian.practica_modulo7.databinding.FragmentExerciseListBinding
import com.fabian.practica_modulo7.ui.adapters.ExerciseAdapter
import com.fabian.practica_modulo7.ui.viewmodels.ListScreenState
import com.fabian.practica_modulo7.ui.viewmodels.ExerciseListViewModel
import com.fabian.practica_modulo7.ui.viewmodels.ViewModelFactory

class ExerciseListFragment: Fragment() {

    private var _binding: FragmentExerciseListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExerciseListViewModel by viewModels {
        val app = requireActivity().application as ExerciseApp
        val repository = app.repository
        val preferencesRepository = app.preferencesRepository
        ViewModelFactory(repository, preferencesRepository)
    }

    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("retry_key") { key, bundle ->
            if (bundle.getBoolean("retry")) {
                viewModel.loadExercises()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModelState()

        // Llama a cargar datos si es la primera vez o si el estado actual es Loading
        if (viewModel.uiState.value == null || viewModel.uiState.value is ListScreenState.Loading) {
            viewModel.loadExercises()
        }
    }

    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(emptyList()) { selectedExercise ->
            selectedExercise.id?.let { id ->
                val action = ExerciseListFragmentDirections.actionListToDetail(id)
                findNavController().navigate(action)
            }
        }
        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = exerciseAdapter
        }
    }

    private fun observeViewModelState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            handleUiState(state)
        }
    }

    private fun handleUiState(state: ListScreenState) {
        when (state) {
            is ListScreenState.Loading -> {
                binding.pbLoading.visibility = View.VISIBLE
                binding.rvGames.visibility = View.GONE
            }
            is ListScreenState.Success -> {
                binding.pbLoading.visibility = View.GONE
                binding.rvGames.visibility = View.VISIBLE
                exerciseAdapter.updateData(state.exercises, state.completedIds)
            }
            is ListScreenState.Error -> {
                binding.pbLoading.visibility = View.GONE

                val action = ExerciseListFragmentDirections.actionListToError()
                findNavController().navigate(action)

                val errorMessage = if (state.args != null) {
                    getString(state.messageResId, *state.args.toTypedArray())
                } else {
                    getString(state.messageResId)
                    }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpia el binding
    }
}