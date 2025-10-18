package com.example.pulse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HabitAdapter(private val habitList: List<Habit>,
                   private val onDeleteClick: (Habit) -> Unit
) :
    RecyclerView.Adapter<HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.habitlist_layout, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habitList[position]
        holder.habitName.text = habit.title
        holder.habitDescription.text = habit.description

        val imageRes = when(habit.type ?: "") {
            "Workout" -> R.drawable.workout_icon
            "Food" -> R.drawable.salad_icon
            "Meditation" -> R.drawable.meditate2_icon
            "Water" -> R.drawable.watercup_icon
            else -> R.drawable.workout_icon
        }
        holder.habitImg.setImageResource(imageRes)

        holder.deleteBtn.setOnClickListener{
            onDeleteClick(habit)
        }
    }

    override fun getItemCount(): Int {
        return habitList.size
    }
}