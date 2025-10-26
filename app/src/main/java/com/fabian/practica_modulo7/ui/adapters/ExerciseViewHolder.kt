package com.fabian.practica_modulo7.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fabian.practica_modulo7.data.remote.model.ExerciseDto
import com.fabian.practica_modulo7.databinding.ExerciseElementBinding

class ExerciseViewHolder (
    private val binding: ExerciseElementBinding,
    private val onExerciseClick: (ExerciseDto) -> Unit
): RecyclerView.ViewHolder(binding.root){

    private var currentItem: ExerciseDto? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let(onExerciseClick)
        }
    }

    fun bind(exercise: ExerciseDto, isCompleted: Boolean){
        currentItem = exercise
        binding.tvTitle.text = exercise.title

        Glide.with(binding.root.context)
            .load(exercise.thumbnail)
            .into(binding.ivThumbnail)
        // CHECKMARK
        if (isCompleted) {
            binding.ivCompletedCheckmark.visibility = View.VISIBLE
        } else {
            binding.ivCompletedCheckmark.visibility = View.GONE
        }
    }
    companion object{
        fun create(
            parent: ViewGroup,
            onGameClick: (ExerciseDto) -> Unit
        ): ExerciseViewHolder{
            val binding = ExerciseElementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ExerciseViewHolder(binding, onGameClick)
        }
    }
}