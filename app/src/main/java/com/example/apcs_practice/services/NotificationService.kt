package com.example.apcs_practice.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.apcs_practice.R
import com.example.apcs_practice.models.TestInfo
import com.example.apcs_practice.view.activities.settings
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class NotificationService : Service() {

    override fun onCreate() {
        super.onCreate()

        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("test_info").document("dates")
            .addSnapshotListener { querySnapshot, _ ->
                val dates = querySnapshot!!.toObject(TestInfo::class.java)!!
                sendNotification(dates)
            }
    }

    private fun sendNotification(dates: TestInfo) {
        val channel = NotificationChannel("Ch1", "APCS", NotificationManager.IMPORTANCE_HIGH)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val cal = Calendar.getInstance()
        cal.get(Calendar.YEAR)
        cal.get(Calendar.MONTH)
        cal.get(Calendar.DAY_OF_MONTH)
        val myFormat = "MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.TAIWAN)
        val date = sdf.format(cal.time)

        if (settings.getBoolean("notificationTest", true) && date == dates.test) {
            val notification = Notification.Builder(this, "Ch1")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle("APCS檢測日")
                .setContentText("今天是APCS檢測日，請注意考試時間")
                .setAutoCancel(true)
                .build()
            manager.notify(0, notification)
        }

        if (settings.getBoolean("notificationSignUpStart", true) && date == dates.signUpStart) {
            val notification = Notification.Builder(this, "Ch1")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle("APCS開始報名")
                .setContentText("APCS${dates.test}場次開始報名，報名時間${dates.signUpStart}~${dates.signUpDeadline}")
                .setAutoCancel(true)
                .build()
            manager.notify(1, notification)
        }

        if (settings.getBoolean("notificationSignUpEnd", true) && date == dates.signUpDeadline) {
            val notification = Notification.Builder(this, "Ch1")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle("APCS報名最後一天")
                .setContentText("今天是APCS${dates.test}場次報名最後一天，記得要報名喔")
                .setAutoCancel(true)
                .build()
            manager.notify(2, notification)
        }
        if (settings.getBoolean("queryResultsStart", true) && date == dates.queryResults) {
            val notification = Notification.Builder(this, "Ch1")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle("成績查詢開始")
                .setContentText("APCS${dates.test}場次開放查詢")
                .setAutoCancel(true)
                .build()
            manager.notify(3, notification)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_REDELIVER_INTENT
    }
}