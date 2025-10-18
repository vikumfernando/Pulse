package com.example.pulse

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import androidx.core.content.edit

class HydrationScreen  : SensorFunction()  {

    private var currentWater = 0
    private var dailyWater = 2000
    private var perDrink = 250
    private val prefs by lazy { getSharedPreferences("hydration", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hydration_screen)

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

        val completeWater = findViewById<EditText>(R.id.completeWater)
        val targetWater = findViewById<EditText>(R.id.targetWater)
        val notificationBtn = findViewById<ImageView>(R.id.notificationBtn)


        //bottle size buttons
        val bottle1  = findViewById<LinearLayout>(R.id.bottle1_btn)
        val bottle2  = findViewById<LinearLayout>(R.id.bottle2_btn)
        val bottle3  = findViewById<LinearLayout>(R.id.bottle3_btn)
        val bottle4  = findViewById<LinearLayout>(R.id.bottle4_btn)

        bottle1.setOnClickListener {

            perDrink = 100
            currentWater += perDrink
            completeWater.setText(currentWater.toString())
            prefs.edit {
                putInt("currentWater", currentWater)
            }


            it.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }

        bottle2.setOnClickListener{
            perDrink = 250
            currentWater += perDrink
            completeWater.setText(currentWater.toString())
            prefs.edit {
                putInt("currentWater", currentWater)
            }

            it.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }

        bottle3.setOnClickListener{
            perDrink = 450
            currentWater += perDrink
            completeWater.setText(currentWater.toString())
            prefs.edit {
                putInt("currentWater", currentWater)
            }

            it.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }

        bottle4.setOnClickListener{
            perDrink = 500
            currentWater += perDrink
            completeWater.setText(currentWater.toString())
            prefs.edit {
                putInt("currentWater", currentWater)
            }

            it.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()

        }


        targetWater.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                val value = s.toString().toIntOrNull() ?: dailyWater
                prefs.edit { putInt("targetWater", value) }
                dailyWater = value
            }
        })

        completeWater.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                val value = s.toString().toIntOrNull() ?: currentWater
                prefs.edit { putInt("currentWater", value) }
                currentWater = value
            }
        })

       //prefs.edit { putInt("currentWater", 0) }

        Log.d("hydrationTarget", dailyWater.toString())



        currentWater = prefs.getInt("currentWater", currentWater)
        completeWater.setText(currentWater.toString())

        dailyWater = prefs.getInt("targetWater", dailyWater)
        targetWater.setText(dailyWater.toString())


        notificationBtn.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            prefs.edit {
                putInt("reminderHour", selectedHour)
                    .putInt("reminderMinute", selectedMinute)
            }
            scheduleHydrationReminder(selectedHour, selectedMinute)
        }, hour, minute, true).show()
    }

    private fun scheduleHydrationReminder(hour: Int, minute: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
                return
            }
        }

        val intent = Intent(this, HydrationReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        Toast.makeText(
            this,
            "Hydration reminder set!",
            Toast.LENGTH_SHORT
        ).show()

        Log.d("HydrationReminder", "Exact reminder set for $hour:$minute")
    }
}