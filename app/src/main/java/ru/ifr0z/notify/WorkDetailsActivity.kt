package ru.ifr0z.notify

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.workManagerr.notify.R
import com.workManagerr.notify.databinding.ActivityWorkDetailsBinding

class WorkDetailsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWorkDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
Log.d("fdsafasdfadsfsfd",intent.getStringExtra("title")+"  "+intent.getStringExtra("des"))
        binding.toolbar.text=intent.getStringExtra("title")
        binding.des.text=intent.getStringExtra("des")

    }


}