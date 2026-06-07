package com.example.cryptotest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cryptotest.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root){v,insets->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(

                systemBars.left,

                systemBars.top,

                systemBars.right,

                v.paddingBottom

            )

            insets
        }
        setContentView(binding.root)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController= navHostFragment.navController;
        binding.bottomNavigation.setupWithNavController(navController)

    }
}