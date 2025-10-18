package com.example.pulse

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MoodHistoryActivity : AppCompatActivity() {

    private lateinit var moodManager: MoodManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mood_history)

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


        moodManager = MoodManager(this)
        recyclerView = findViewById(R.id.mood_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val groupItems = mutableListOf<MoodHistoryItem>()
        var lastDate = ""

        for (mood in moodManager.getMoods()) {
            if (mood.date != lastDate) {
                groupItems.add(MoodHistoryItem.DateHeader(mood.date))
                lastDate = mood.date
            }
            groupItems.add(MoodHistoryItem.MoodLogItem(mood))
        }

        adapter = MoodAdapter(groupItems)
        recyclerView.adapter = adapter
    }
}