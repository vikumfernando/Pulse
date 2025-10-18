package com.example.pulse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val moodIcon: ImageView = itemView.findViewById(R.id.mood_icon)
    val moodType: TextView = itemView.findViewById(R.id.mood_type)
    val moodTime: TextView = itemView.findViewById(R.id.mood_time)
}

class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dateText: TextView = itemView.findViewById(R.id.dateHeaderText)

}