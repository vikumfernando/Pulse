package com.example.pulse

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MoodManager (context : Context) {

    private val prefs = context.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "mood_list"

    fun getMoods(): MutableList<Mood> {
        val json = prefs.getString(key, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Mood>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveMoods(moods: List<Mood>) {
        val json = gson.toJson(moods)
        prefs.edit().putString(key, json).apply()
    }

    fun addMood(mood: Mood) {
        val moods = getMoods().toMutableList()
        moods.add(mood)
        saveMoods(moods)
    }


}