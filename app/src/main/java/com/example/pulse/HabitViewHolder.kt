package com.example.pulse

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    val habitName: TextView = itemView.findViewById(R.id.habitName)
    val habitDescription: TextView = itemView.findViewById(R.id.habitDescription)
    val habitImg : ImageView = itemView.findViewById(R.id.habitImg)
    val deleteBtn : ImageView = itemView.findViewById(R.id.deleteHabitBtn)
}