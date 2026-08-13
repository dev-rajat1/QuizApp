package com.example.quizhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCategory)

        val list = listOf(
            Category("General Knowledge", 9, "🧠"),
            Category("Books", 10, "📚"),
            Category("Film", 11, "🎬"),
            Category("Music", 12, "🎵"),
            Category("Musicals & Theatres", 13, "🎭"),
            Category("Television", 14, "📺"),
            Category("Video Games", 15,  "🎮"),
            Category("Board Games", 16,  "♟️"),
            Category("Science & Nature", 17,  "🌿"),
            Category("Computers", 18,  "💻"),
            Category("Mathematics", 19,  "➗"),
            Category("Mythology", 20,  "⚡"),
            Category("Sports", 21,  "🏏"),
            Category("Geography", 22,  "🌍"),
            Category("History", 23,  "📜"),
            Category("Politics", 24,  "🏛️"),
            Category("Art", 25,  "🎨"),
            Category("Celebrities", 26,  "🌟"),
            Category("Animals", 27,  "🐶"),
            Category("Vehicles", 28,  "🚗"),
            Category("Comics", 29,  "🦸"),
            Category("Gadgets", 30,  "📱"),
            Category("Anime & Manga", 31,  "🎌"),
            Category("Cartoon & Animations", 32,  "🧸")
        )

        recycler.layoutManager = GridLayoutManager(this, 2)

        recycler.adapter = CategoryAdapter(list) { category ->

            val intent = Intent(this, SetupActivity::class.java)
            intent.putExtra("category", category.id)
            startActivity(intent)
        }
    }
}