package com.example.metube.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.metube.databinding.ActivityMainBinding
import com.example.metube.utils.NotificationHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationHelper.createNotificationChannel(this)

        checkNotificationPermission()

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isNotEmpty()) {
                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("SEARCH_QUERY", query)
                startActivity(intent)
            }
        }
    }

    private fun checkNotificationPermission() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val notificationSent = sharedPreferences.getBoolean("notification_sent", false)

        if (!notificationSent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    sendNotificationOnce()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                sendNotificationOnce()
            }
        }
    }

    private fun sendNotificationOnce() {
        NotificationHelper.sendVideoNotification(this)

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("notification_sent", true).apply()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                sendNotificationOnce()
            }
        }
}
