package com.example.pulse

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitManager(context: Context) {

    private val prefs = context.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "habit_list"
    private var habits: MutableList<Habit> = mutableListOf()

    // Returning all habits
    fun getHabits(): MutableList<Habit> {
        val json = prefs.getString(key, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Habit>>() {}.type
        return gson.fromJson(json, type)
    }

    // Saving habits to local storage
    private fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        prefs.edit().putString(key, json).apply()
    }

    // Adding new habit
    fun addHabit(habit: Habit) {
        val currentHabits = getHabits().toMutableList()
        currentHabits.add(habit)
        saveHabits(currentHabits)
    }

    // Updating habits
    fun updateHabit(updatedHabit: Habit) {
        val habits = getHabits()
        val index = habits.indexOfFirst { it.id == updatedHabit.id }
        if (index != -1) {
            habits[index] = updatedHabit
            saveHabits(habits)
        }
    }

    // Deleting habit
    fun deleteHabit(id: Int) {
        habits = habits.filter { it.id != id }.toMutableList()
        saveHabits(habits)
    }

    // Get habits for the given date
    fun getHabitsByDate(date: String): List<Habit> {
        return getHabits().filter { it.date == date }
    }

}