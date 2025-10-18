package com.example.pulse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MoodAdapter (

    private val items: List<MoodHistoryItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_MOOD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is MoodHistoryItem.DateHeader -> TYPE_HEADER
            is MoodHistoryItem.MoodLogItem -> TYPE_MOOD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.date_header_item, parent, false)
            DateHeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.mood_item, parent, false)
            MoodViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is MoodHistoryItem.DateHeader -> {
                (holder as DateHeaderViewHolder).dateText.text = item.date
            }
            is MoodHistoryItem.MoodLogItem -> {
                val moodHolder = holder as MoodViewHolder
                moodHolder.moodType.text = item.mood.moodType
                moodHolder.moodTime.text = "${item.mood.date}  ${item.mood.time}"

                val iconRes = when (item.mood.moodType) {
                    "Awesome" -> R.drawable.awesome_icon
                    "Good" -> R.drawable.good_icon
                    "Fine" -> R.drawable.fine_icon
                    "Bad" -> R.drawable.bad_icon
                    "Terrible" -> R.drawable.terrible_icon
                    else -> R.drawable.good_icon
                }
                moodHolder.moodIcon.setImageResource(iconRes)
            }
        }
    }

    override fun getItemCount(): Int = items.size

}

