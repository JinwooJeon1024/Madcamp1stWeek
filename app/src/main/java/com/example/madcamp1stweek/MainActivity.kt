package com.example.madcamp1stweek

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madcamp1stweek.databinding.ActivityMainBinding
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val recyclerView: RecyclerView = findViewById(R.id.galleryRecyclerView)

        val itemData = listOf(
            ItemData(R.drawable.image1, "Title 1", "Content 1"),
            ItemData(R.drawable.image2, "Title 2", "Content 2"),
            ItemData(R.drawable.image3, "Title 2", "Content 2"),
            ItemData(R.drawable.image4, "Title 2", "Content 2"),
            ItemData(R.drawable.image5, "Title 2", "Content 2"),
            ItemData(R.drawable.image6, "Title 1", "Content 1"),
            ItemData(R.drawable.image7, "Title 1", "Content 1"),
            ItemData(R.drawable.image8, "Title 2", "Content 2"),
            ItemData(R.drawable.image9, "Title 2", "Content 2"),
            ItemData(R.drawable.image10, "Title 2", "Content 2"),
            ItemData(R.drawable.image11, "Title 2", "Content 2"),
            ItemData(R.drawable.image12, "Title 2", "Content 2"),
            ItemData(R.drawable.image13, "Title 2", "Content 2"),
            ItemData(R.drawable.image14, "Title 2", "Content 2"),
            ItemData(R.drawable.image15, "Title 2", "Content 2"),
        )

        val numOfColumns = 3
        recyclerView.layoutManager = GridLayoutManager(this, numOfColumns)
        recyclerView.adapter = MyRecyclerAdapter(itemData)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, 50, true))
    }
}
