package com.sdp.ecommerce.data

import android.content.Context
import android.content.SharedPreferences

class ShoppingAppSessionManager(context: Context) {

	var userSession: SharedPreferences =
		context.getSharedPreferences("userSessionData", Context.MODE_PRIVATE)
	var editor: SharedPreferences.Editor = userSession.edit()


	fun createLoginSession(
		id: String,
		name: String,
		mobile: String,
		isRemOn: Boolean,
		isSeller: Boolean
	) {
		editor.putBoolean(IS_LOGIN, true)
		editor.putString(KEY_ID, id)
		editor.putString(KEY_NAME, name)
		editor.putString(KEY_MOBILE, mobile)
		editor.putBoolean(KEY_REMEMBER_ME, isRemOn)
		editor.putBoolean(KEY_IS_SELLER, isSeller)

		editor.commit()
	}

	fun isUserSeller(): Boolean = userSession.getBoolean(KEY_IS_SELLER, false)

	fun isRememberMeOn(): Boolean = userSession.getBoolean(KEY_REMEMBER_ME, true)

	fun getPhoneNumber(): String? = userSession.getString(KEY_MOBILE, null)

	fun getUserDataFromSession(): HashMap<String, String?> {
		return hashMapOf(
			KEY_ID to userSession.getString(KEY_ID, "0"),
			KEY_NAME to userSession.getString(KEY_NAME, "root"),
			KEY_MOBILE to userSession.getString(KEY_MOBILE, "9173185578")
		)
	}

	fun getUserIdFromSession(): String? = userSession.getString(KEY_ID, "0")

	fun isLoggedIn(): Boolean = userSession.getBoolean(IS_LOGIN, false)

	fun logoutFromSession() {
		editor.clear()
		editor.commit()
	}

	companion object {
		private const val IS_LOGIN = "isLoggedIn"
		private const val KEY_NAME = "userName"
		private const val KEY_MOBILE = "userMobile"
		private const val KEY_ID = "userId"
		private const val KEY_REMEMBER_ME = "isRemOn"
		private const val KEY_IS_SELLER = "isSeller"
	}
}