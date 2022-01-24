package com.sdp.ecommerce.ui.loginSignup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.sdp.ecommerce.ui.MyOnFocusChangeListener
import com.sdp.ecommerce.viewModels.AuthViewModel

private const val TAG = "LoginSignupBaseFragment"
abstract class LoginSignupBaseFragment<VBinding : ViewBinding> : Fragment() {

	protected lateinit var  viewModel: AuthViewModel

	protected lateinit var binding: VBinding
	protected abstract fun setViewBinding(): VBinding

	protected val focusChangeListener = MyOnFocusChangeListener()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		init()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		viewModel =  ViewModelProvider(requireActivity()).get(com.sdp.ecommerce.viewModels.AuthViewModel::class.java)
		setUpViews()
		observeView()
		return binding.root
	}

	fun launchOtpActivity(from: String, extras: Bundle) {
		Log.d(TAG, "launchOtpActivity: ")
		val intent = Intent(context, OtpActivity::class.java).putExtra(
			"from",
			from
		).putExtras(extras)
		startActivity(intent)
	}

	open fun setUpViews() {}

	open fun observeView() {}

	private fun init() {
		binding = setViewBinding()
	}

	interface OnClickListener : View.OnClickListener
}