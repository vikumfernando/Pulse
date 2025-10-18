package com.example.pulse

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class NewHabitForm(
    private val onHabitAdded: (Habit) -> Unit
) : DialogFragment() {

    @SuppressLint("DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.add_habit_layout, null)

        val titleInput = view.findViewById<EditText>(R.id.title_input)
        val descInput = view.findViewById<EditText>(R.id.description_input)
        val saveBtn = view.findViewById<FrameLayout>(R.id.save_btn)


        //Date input from calendar
        val dialogView = layoutInflater.inflate(R.layout.add_habit_layout, null)
        val pickDateBtn = view.findViewById<ImageView>(R.id.selectDateBtn)
        var selectedDate = getTodayDate()
        var selectedType : String = ""


        //Function to decide which habit type
        val habitType1 = view.findViewById<FrameLayout>(R.id.habit_type1)
        val habitType2 = view.findViewById<FrameLayout>(R.id.habit_type2)
        val habitType3 = view.findViewById<FrameLayout>(R.id.habit_type3)
        val habitType4 = view.findViewById<FrameLayout>(R.id.habit_type4)

        //Function to decide which habit type
        fun selectHabitType(selected: FrameLayout, type: String) {

            habitType1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            habitType2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            habitType3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            habitType4.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)

            selected.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondaryColor)


            selectedType = type
        }

        habitType1.setOnClickListener { selectHabitType(habitType1, "Workout") }
        habitType2.setOnClickListener { selectHabitType(habitType2, "Food") }
        habitType3.setOnClickListener { selectHabitType(habitType3, "Meditation") }
        habitType4.setOnClickListener { selectHabitType(habitType4, "Water") }


        //date input part
        pickDateBtn.setOnClickListener {

            val calendarForPicker = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedDate = String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth)
                    Toast.makeText(requireContext(), "Selected: $selectedDate", Toast.LENGTH_SHORT).show()
                },
                calendarForPicker.get(Calendar.YEAR),
                calendarForPicker.get(Calendar.MONTH),
                calendarForPicker.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        //Saving data of the Habit
        saveBtn.setOnClickListener {
            val title = titleInput.text.toString()
            val desc = descInput.text.toString()

            if (title.isNotEmpty() && desc.isNotEmpty()) {
                val habit = Habit(
                    id = System.currentTimeMillis().toInt(),
                    title = title,
                    description = desc,
                    isDone = false,
                    date = selectedDate,
                    type = selectedType
                )
                onHabitAdded(habit)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setView(view)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

    //Function get the current date
    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }



}
