package com.example.pulse

sealed class MoodHistoryItem {

    data class DateHeader(val date: String) : MoodHistoryItem()
    data class MoodLogItem(val mood: Mood) : MoodHistoryItem()
}