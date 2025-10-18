package com.example.pulse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MoodActivity  : SensorFunction()  {

    private lateinit var moodManager: MoodManager
    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mood)

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


        moodManager = MoodManager(this)
        val moodGroup = findViewById<RadioGroup>(R.id.moodGroup)
        val saveBtn = findViewById<Button>(R.id.saveMoodBtn)

        moodGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedMood = when(checkedId){
                R.id.mood1_checkbox -> "Awesome"
                R.id.mood2_checkbox -> "Good"
                R.id.mood3_checkbox -> "Fine"
                R.id.mood4_checkbox -> "Bad"
                R.id.mood5_checkbox -> "Terrible"
                else -> "Unknown"
            }

            saveBtn.setOnClickListener {
                saveMood(selectedMood)
                val intent = Intent(this, MoodHistoryActivity::class.java)
                startActivity(intent)

            }
        }

    }

    private fun saveMood(moodType :String){
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        val imageId = when (moodType) {
            "Awesome" -> R.drawable.awesome_icon
            "Good" -> R.drawable.good_icon
            "Fine" -> R.drawable.fine_icon
            "Bad" -> R.drawable.bad_icon
            "Terrible" -> R.drawable.terrible_icon
            else -> R.drawable.fine_icon
        }

        val mood = Mood(
            id = (0..1000000).random(),
            moodType = moodType,
            imageId = imageId,
            date = currentDate,
            time = currentTime

        )

        moodManager.addMood(mood)
        Toast.makeText(this, "Mood saved: $moodType", Toast.LENGTH_SHORT).show()

        val allMoods = moodManager.getMoods()
        for (m in allMoods) {
            Log.d("MoodList", "ID: ${m.id}, Type: ${m.moodType}, Date: ${m.date}, Time: ${m.time}")
        }
    }
}
