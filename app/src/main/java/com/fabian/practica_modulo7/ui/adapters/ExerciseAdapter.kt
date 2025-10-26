package com.fabian.practica_modulo7.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabian.practica_modulo7.data.remote.model.ExerciseDto

class ExerciseAdapter(
    private var exercises: List<ExerciseDto>,
    private val onExerciseClicked: (ExerciseDto) -> Unit
) : RecyclerView.Adapter<ExerciseViewHolder>() {

    private var completedIds: Set<String> = emptySet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder.create(parent, onExerciseClicked)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise, completedIds.contains(exercise.id))
        holder.itemView.setOnClickListener { onExerciseClicked(exercise) }
    }

    fun updateData(newExercises: List<ExerciseDto>, newCompletedIds: Set<String>) {
        exercises = newExercises
        completedIds = newCompletedIds
        notifyDataSetChanged()
    }
}