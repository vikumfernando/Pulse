package com.example.pulse

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class HomeScreen : SensorFunction() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        // Status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.accentColor)
        }

        // Navigation buttons
        val homeBtn = findViewById<ImageView>(R.id.nav_icon1)
        val habitBtn = findViewById<ImageView>(R.id.nav_icon2)
        val mentalBtn = findViewById<ImageView>(R.id.nav_icon4)
        val hydrationBtn = findViewById<ImageView>(R.id.nav_icon5)
        val hydrationWidget = findViewById<ImageView>(R.id.watercup_image)
        val mind1 = findViewById<FrameLayout>(R.id.mind1)
        val mind2 = findViewById<FrameLayout>(R.id.mind2)

        // Hydration widget labels
        val addWaterBtn = findViewById<ImageView>(R.id.addWaterBtn)
        val completeWaterLabel = findViewById<TextView>(R.id.hydration_complete)
        val targetWaterLabel = findViewById<TextView>(R.id.hydration_target)
        val prefs = getSharedPreferences("hydration", Context.MODE_PRIVATE)

        fun updateHydrationLabels() {
            val completeWater = prefs.getInt("currentWater", 0)
            val targetWater = prefs.getInt("targetWater", 0)
            completeWaterLabel.text = completeWater.toString()
            targetWaterLabel.text = targetWater.toString()
        }

        updateHydrationLabels()

        addWaterBtn.setOnClickListener {
            val perDrink = 100
            val currentWater = prefs.getInt("currentWater", 0) + perDrink
            prefs.edit { putInt("currentWater", currentWater) }
            updateHydrationLabels()
            Toast.makeText(this, "Water added: $perDrink ml", Toast.LENGTH_SHORT).show()
        }

        //Buttons when clicked
        hydrationWidget.setOnClickListener {
            startActivity(Intent(this, HydrationScreen::class.java))
        }

        mind2.setOnClickListener {
            startActivity(Intent(this, MoodHistoryActivity::class.java))
        }

        homeBtn.setOnClickListener {
            startActivity(Intent(this, HomeScreen::class.java))
        }

        habitBtn.setOnClickListener {
            startActivity(Intent(this, HabitScreen::class.java))
        }

        mentalBtn.setOnClickListener {
            startActivity(Intent(this, MoodActivity::class.java))
        }

        hydrationBtn.setOnClickListener {
            startActivity(Intent(this, HydrationScreen::class.java))
        }

        mind1.setOnClickListener {
            startActivity(Intent(this, MoodActivity::class.java))
        }

        //Bar chart
        val habitManager = HabitManager(this)
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val weekCounts = IntArray(7) { 0 }

        // Count habits per day of week
        for (habit in habitManager.getHabits()) {
            try {
                val date = sdf.parse(habit.date.trim()) ?: continue
                calendar.time = date
                val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Monday=0, Sunday=6
                weekCounts[dayOfWeek] += 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val values = ArrayList<BarEntry>()
        for (i in 0..6) {
            values.add(BarEntry(i.toFloat(), weekCounts[i].toFloat()))
        }

        val barChart = findViewById<BarChart>(R.id.barChart)
        val dataSet = BarDataSet(values, "Habits done per day")
        dataSet.color = "#FDCA78".toColorInt()

        val data = BarData(dataSet)
        data.barWidth = 0.9f

        barChart.data = data
        barChart.setFitBars(true)
        barChart.animateY(1000)

        val xAxis = barChart.xAxis
        xAxis.valueFormatter =
            IndexAxisValueFormatter(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false


        val yAxis = barChart.axisLeft
        yAxis.granularity = 1f
        yAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

    }
}