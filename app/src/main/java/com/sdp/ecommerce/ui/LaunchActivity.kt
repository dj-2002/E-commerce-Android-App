package com.sdp.ecommerce.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sdp.ecommerce.NotifyService
import com.sdp.ecommerce.R
import com.sdp.ecommerce.data.ShoppingAppSessionManager
import com.sdp.ecommerce.ui.loginSignup.LoginSignupActivity
import java.util.*

private const val TAG = "LaunchActivity"
class LaunchActivity : AppCompatActivity() {

	lateinit  var sessionManager : ShoppingAppSessionManager


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)

		sessionManager = ShoppingAppSessionManager(applicationContext)

		val intent = Intent(applicationContext,NotifyService::class.java)
		val pendingIntent = PendingIntent.getBroadcast(applicationContext,0,intent,0)
		val alarm:AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val frequency:Long = 60*100
		alarm.setRepeating(AlarmManager.RTC_WAKEUP,Calendar.getInstance().timeInMillis,frequency,pendingIntent)
		setLaunchScreenTimeOut()
	}



	private fun setLaunchScreenTimeOut() {
		Looper.myLooper()?.let {
			Handler(it).postDelayed({
				startPreferredActivity()
			}, TIME_OUT)
		}
	}

	private fun startPreferredActivity() {
		if (sessionManager.isLoggedIn()) {
			Log.e(TAG, "startPreferredActivity: user is already logged in ", )
			launchHome(this)
			finish()
		} else {
			val intent = Intent(this, LoginSignupActivity::class.java)
			startActivity(intent)
			finish()
		}
	}

	companion object {
		private const val TIME_OUT: Long = 1500
	}
}