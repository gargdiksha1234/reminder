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
import ru.ifr0z.notify.work.NotifyWork.Companion.TASK_DESC
import ru.ifr0z.notify.work.NotifyWork.Companion.TASK_TITLE

class WorkDetailsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWorkDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.text=intent.getStringExtra(TASK_TITLE)
        binding.des.text=intent.getStringExtra(TASK_DESC)

    }


}