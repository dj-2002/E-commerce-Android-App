package com.sdp.ecommerce.ui.home

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.sdp.ecommerce.R
import com.sdp.ecommerce.data.ShoppingAppSessionManager
import com.sdp.ecommerce.data.UserData
import com.sdp.ecommerce.data.source.remote.AuthRemoteDataSource
import com.sdp.ecommerce.databinding.ActivityMainBinding
import com.sdp.ecommerce.ui.LaunchActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		Log.e(TAG, "onCreate starts")
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// Bottom Navigation
		setUpNav()

		lifecycleScope.launch(Dispatchers.IO){
			Log.e(TAG, "onCreate: notify seller ", )
			val authRemoteDataSource = AuthRemoteDataSource()
			authRemoteDataSource.notifySeller(UserData().userId,"dsafdsafasdfdsfafdd");
		}

	}




	private fun setUpNav() {
		val navFragment =
			supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as NavHostFragment
		NavigationUI.setupWithNavController(binding.homeBottomNavigation, navFragment.navController)

		navFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
			when (destination.id) {
				R.id.homeFragment -> setBottomNavVisibility(View.VISIBLE)
				R.id.cartFragment -> setBottomNavVisibility(View.VISIBLE)
				R.id.accountFragment -> setBottomNavVisibility(View.VISIBLE)
				R.id.ordersFragment -> setBottomNavVisibility(View.VISIBLE)
				R.id.orderSuccessFragment -> setBottomNavVisibility(View.VISIBLE)
				else -> setBottomNavVisibility(View.GONE)
			}
		}

		val sessionManager = ShoppingAppSessionManager(this.applicationContext)
		if (sessionManager.isUserSeller()) {
			binding.homeBottomNavigation.menu.removeItem(R.id.cartFragment)
		}else {
			binding.homeBottomNavigation.menu.removeItem(R.id.ordersFragment)
		}
	}

	private fun setBottomNavVisibility(visibility: Int) {
		binding.homeBottomNavigation.visibility = visibility
	}
}