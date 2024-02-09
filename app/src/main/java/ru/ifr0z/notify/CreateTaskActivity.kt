package ru.ifr0z.notify

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.work.Data
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.make
import com.workManagerr.notify.R
import com.workManagerr.notify.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

import com.workManagerr.notify.db.WorkListEntity
import com.workManagerr.notify.viewmodel.WorkViewModel
import ru.ifr0z.notify.work.NotifyWork
import ru.ifr0z.notify.work.NotifyWork.Companion.NOTIFICATION_ID
import ru.ifr0z.notify.work.NotifyWork.Companion.NOTIFICATION_WORK
import ru.ifr0z.notify.work.NotifyWork.Companion.TASK_DESC
import ru.ifr0z.notify.work.NotifyWork.Companion.TASK_TITLE
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.getDefault
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CreateTaskActivity : AppCompatActivity() {


    private val vm: WorkViewModel by viewModels()

    private lateinit var binding: MainActivityBinding

    private lateinit var checkNotificationPermission: ActivityResultLauncher<String>
    private var isPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkNotificationPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            isPermission = isGranted
        }


        userInterface()

        checkPermission()

    }


    private fun checkPermission() {
        if (SDK_INT >= TIRAMISU) {
            if (checkSelfPermission(this, POST_NOTIFICATIONS) == PERMISSION_GRANTED) {
                isPermission = true
            } else {
                isPermission = false

                checkNotificationPermission.launch(POST_NOTIFICATIONS)
            }
        } else {
            isPermission = true
        }
    }

    private fun userInterface() {


        binding.doneFab.setOnClickListener {

            val taskTitle = binding.textview.text.toString().trim()
            val taskDescription = binding.description.text.toString().trim()
            if (isPermission) {
                val customCalendar = Calendar.getInstance()
                customCalendar.set(
                    binding.datePicker.year,
                    binding.datePicker.month,
                    binding.datePicker.dayOfMonth,
                    binding.timePicker.hour,
                    binding.timePicker.minute,
                )

                val customTime = customCalendar.timeInMillis
                val currentTimeMillis = currentTimeMillis()
                val currentTime = currentTimeMillis - (currentTimeMillis % 1000)


                if (taskTitle.isEmpty()) {

                    Toast.makeText(this,
                        getString(R.string.title_should_not_be_empty), Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener

                } else if (taskDescription.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.description_should_not_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener

                }
                else {
                    val timeDifferenceMillis = customTime - currentTime
                    val timeDifferenceMinutes = timeDifferenceMillis / (1000 * 60)

                    //check time difference should be 7 min minimum.
                    if (timeDifferenceMinutes >= 7L) {


                        /**
                         * to get 5 min before notification
                         */
                        val delay = customTime - currentTime - (5 * 60 * 1000)

                        scheduleNotification(
                            taskTitle,
                            delay,

                            taskDescription,
                            customTime
                        )

                        val titleNotificationSchedule =
                            getString(R.string.notification_schedule_title)
                        val patternNotificationSchedule =
                            getString(R.string.notification_schedule_pattern)
                        make(
                            binding.coordinatorLayout,
                            titleNotificationSchedule + SimpleDateFormat(
                                patternNotificationSchedule, getDefault()
                            ).format(customCalendar.time).toString(),
                            LENGTH_LONG
                        ).show()

                        finish()
                    } else {
                        val errorNotificationSchedule =
                            getString(R.string.time_gap)
                        make(
                            binding.coordinatorLayout,
                            errorNotificationSchedule,
                            LENGTH_LONG
                        ).show()
                    }
                }



        }
        else {
            if (SDK_INT >= TIRAMISU) {
                checkNotificationPermission.launch(POST_NOTIFICATIONS)
            }
        }

    }
}

private fun scheduleNotification(
    title: String,
    delay: Long,

    des: String,
    currentTime: Long
) {

    vm.addWork(WorkListEntity(0, false, title, des, currentTime))
    val uniqueWorkName =
        NOTIFICATION_WORK + currentTimeMillis() // Unique name for each work request
    val data = Data.Builder().putString(TASK_TITLE, title).putString(TASK_DESC,des).putInt( NOTIFICATION_ID, 0)
        .build()


    val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)

        .setInputData(data)


        .build()

    val instanceWorkManager = WorkManager.getInstance(this)
    instanceWorkManager.enqueueUniqueWork(uniqueWorkName, REPLACE, notificationWork)
}
}
