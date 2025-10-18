package com.example.pulse

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HabitScreen: SensorFunction()   {

    private lateinit var recyclerView: RecyclerView
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var habitManager: HabitManager
    private lateinit var allHabits: MutableList<Habit>
    private lateinit var habits: MutableList<Habit>

    @SuppressLint("DefaultLocale", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_habit_screen)

        //Navigation Bar
        val homeBtn = findViewById<ImageView>(R.id.nav_icon1)
        val habitBtn = findViewById<ImageView>(R.id.nav_icon2)
        val mentalBtn = findViewById<ImageView>(R.id.nav_icon4)
        val hydrationBtn = findViewById<ImageView>(R.id.nav_icon5)

        homeBtn.setOnClickListener {
            val intent = Intent(this,HomeScreen::class.java)
            startActivity(intent)
        }

        habitBtn.setOnClickListener{
            val intent = Intent(this,HabitScreen::class.java)
            startActivity(intent)
        }

        mentalBtn.setOnClickListener{
            val intent = Intent(this, MoodActivity::class.java)
            startActivity(intent)
        }

        hydrationBtn.setOnClickListener{
            val intent = Intent(this, HydrationScreen::class.java)
            startActivity(intent)
        }



        val poppinsbold = ResourcesCompat.getFont(this, R.font.poppinsbold)
        val poppinsregular = ResourcesCompat.getFont(this, R.font.poppinsregular)

        habitManager = HabitManager(this)
        allHabits = habitManager.getHabits().toMutableList()
        habits = habitManager.getHabits().toMutableList()
        Log.d("HabitScreen11", "Loaded habits: ${habits.size}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.mind1)
        }

        val dateContainer = findViewById<LinearLayout>(R.id.linear_daycontainer)
        val tdyBtn = findViewById<TextView>(R.id.todayBtn)
        val scrollView = findViewById<HorizontalScrollView>(R.id.daysScrollView)


        //Date slide part
        val calendar = Calendar.getInstance()
        val maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

        for (day in 1..maxDate) {
            val dayCalendar = Calendar.getInstance()
            dayCalendar.set(Calendar.DAY_OF_MONTH, day)

            val dayName = dayFormat.format(dayCalendar.time)
            val dateNumber = day.toString()

            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 25, 20, 16)

                gravity = Gravity.CENTER
                setBackgroundResource(R.drawable.daycontainer_background)
            }

            val params = LinearLayout.LayoutParams(
                180,
                250
            ).apply {
                setMargins(8, 0, 8, 0) // left, top, right, bottom
            }

            val dayText = TextView(this).apply {
                text = dayName
                textSize = 18f
                setTextColor(Color.WHITE)
                setTypeface(poppinsbold)
                gravity = Gravity.CENTER

            }

            val dateText = TextView(this).apply {
                text = dateNumber
                textSize = 20f
                setTypeface(poppinsregular)
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
            }

            itemLayout.addView(dayText)
            itemLayout.addView(dateText)
            itemLayout.layoutParams = params

            itemLayout.setOnClickListener {

                val selectedDate = String.format("%02d/%02d/%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    day
                )
                Log.d("HabitScreen1234", "Filtering habits for: $selectedDate")

                val filteredHabits = habitManager.getHabitsByDate(selectedDate)

                habits.clear()
                habits.addAll(filteredHabits)

                habitAdapter.notifyDataSetChanged()

            }

            dateContainer.addView(itemLayout)
        }

        //Sliding to the current day when tdy button is clicked
        tdyBtn.setOnClickListener {
            val today = calendar.get(Calendar.DAY_OF_MONTH)

            val todayView = dateContainer.getChildAt(today - 1)

            scrollView.post {
                val scrollX = todayView.left - (scrollView.width / 2) + (todayView.width / 2)
                scrollView.smoothScrollTo(scrollX, 0)
            }
        }


        //New habit adding part
        val addBtn = findViewById<ImageView>(R.id.addHabit_btn)

        addBtn.setOnClickListener {
            NewHabitForm{ newHabit -> habitManager.addHabit(newHabit)
                habits.add(newHabit)
                allHabits.add(newHabit)
                habitAdapter.notifyItemInserted(habits.size - 1) }.show(supportFragmentManager, "AddHabitDialog")
        }



        //Recycler view part to display saved habits

        recyclerView = findViewById(R.id.habit_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        habitAdapter = HabitAdapter(habits) { habitToDelete ->
            deleteHabit(habitToDelete)
        }

        recyclerView.adapter = habitAdapter

    }

    //Deleting Habit part
    private fun deleteHabit(habit: Habit) {

                habitManager.deleteHabit(habit.id)

                val position = habits.indexOfFirst { it.id == habit.id }
                if (position != -1) {
                    habits.removeAt(position)
                    habitAdapter.notifyItemRemoved(position)
                }

                Toast.makeText(this, "Habit deleted", Toast.LENGTH_SHORT).show()

    }

}