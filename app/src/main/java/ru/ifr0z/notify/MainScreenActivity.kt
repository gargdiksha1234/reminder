package ru.ifr0z.notify

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.workManagerr.notify.R
import com.workManagerr.notify.databinding.ActivityDetailsBinding
import com.workManagerr.notify.db.WorkListEntity
import com.workManagerr.notify.extension.vectorToBitmap
import com.workManagerr.notify.viewmodel.WorkViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ifr0z.notify.adapter.WorkAdapter
import ru.ifr0z.notify.utils.SwipeToDeleteCallback
import ru.ifr0z.notify.work.NotifyWork
import javax.inject.Inject

@AndroidEntryPoint

class MainScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    @Inject
    lateinit var mAdapter: WorkAdapter

    private val vm: WorkViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm.getUserList()
        setClicks()
        setAdapter()
        adapterCallBacks()
        setObserver()

    }

    private fun adapterCallBacks() {
        mAdapter.onWorkDoneClick{ pos: Int, isCompleted: Boolean ->
            if (!isCompleted) {
                showWorkDialog(pos)
            }
        }
         mAdapter.onDetailsClick { pos: Int ->

            val intent = Intent(this, WorkDetailsActivity::class.java)
            intent.putExtra("title", mAdapter.userList?.get(pos)?.title)
            intent.putExtra("des", mAdapter.userList?.get(pos)?.desc)
            startActivity(intent)
        }
         mAdapter.onDeleteCallBack { pos: Int ->
            vm.deleteAllData()
        }
        mAdapter.onTimeRemainingCallBack {
            Toast.makeText(this, "Time is Remaining ", Toast.LENGTH_LONG).show()
        }

    }

    private fun setObserver() {
        vm.getData.observe(this) {
            if (it != null) {
                mAdapter?.setData(it.toMutableList() as ArrayList<WorkListEntity>)
            }

        }
    }

    private fun setUI() {
        if (vm.userList.value?.size == 0 || vm.userList.value?.size == null) {
            binding.showPlaceholder.visibility = View.VISIBLE
        } else binding.showPlaceholder.visibility = View.GONE
    }

    private fun setClicks() {
        binding.createTask.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAdapter() {
        binding.rvLocations.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = WorkAdapter()
        binding.rvLocations.adapter = mAdapter
    }



    private fun showWorkDialog(
        pos: Int
    ) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Is Work Complete")
            .setCancelable(false)
            .setNegativeButton(
                "Cancel"
            ) { dialog, _ ->
                dialog.dismiss()


            }
            .setPositiveButton(
                "Ok"
            ) { _, _ ->
                val data = mAdapter?.userList?.get(pos)?.apply {
                    isWorkComplete = true
                }


                if (data != null) {

                    vm.updateData(data)
                    sendNotification(id = 0, data.title, data.desc)
                    Log.d("sdafasdfdas",data.title+"   "+data.desc)
                }
            }
        val alert = builder.create()
        alert.show()

    }

    private fun setSwipeControl() {
        val deleteIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_delete_notification)!!
        val color = ContextCompat.getColor(this, R.color.red_E0594D)
        val colorDrawable = ColorDrawable(color)
        val swipeToDeleteCallback = SwipeToDeleteCallback(deleteIcon, colorDrawable) { position ->
            // Handle the swipe-to-delete action
            vm.deleteData(mAdapter?.userList?.get(position)!!)


        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)

        itemTouchHelper.attachToRecyclerView(binding.rvLocations)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(id: Int? = 0, title: String, des: String) {
        val intent = Intent(applicationContext, WorkDetailsActivity::class.java)

        intent.putExtra(NotifyWork.NOTIFICATION_ID, id)
        intent.putExtra("title", title)
        intent.putExtra("des", des)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_schedule_black_24dp)
        val titleNotification = "Complete"
        val subtitleNotification = title

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val notification = NotificationCompat.Builder(
            applicationContext,
            NotifyWork.NOTIFICATION_CHANNEL
        )
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_schedule_white)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NotifyWork.NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    NotifyWork.NOTIFICATION_CHANNEL,
                    NotifyWork.NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id!!, notification.build())
    }
}

