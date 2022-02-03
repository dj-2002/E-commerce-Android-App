package com.sdp.ecommerce

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.sdp.ecommerce.ui.LaunchActivity

private const val TAG = "NotifyService"
class NotifyService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

         context?.let {
                //showdummynotification(it)
         }

        Log.e(TAG, "onReceive: Running Successfully", )
    }
    private fun showdummynotification(context: Context) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, LaunchActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "101"
            val description = "Order"
            val notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            val builder = Notification.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setContentText("Hello Here WE go")
                .setContentTitle("TITLE")
            notificationManager.notify(1234, builder.build())
        } else {

            val  builder = Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
            notificationManager.notify(1234, builder.build())
        }

    }


}