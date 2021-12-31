package com.sdp.ecommerce.auth

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.content.Intent
import android.widget.*
import com.sdp.ecommerce.R

class PWresetActivity : AppCompatActivity() {
    private var ivLogo: ImageView? = null
    private var ivPWreset: ImageView? = null
    private var tvInfo: TextView? = null
    private var tvSignin: TextView? = null
    private var atvEmail: AutoCompleteTextView? = null
    private var btnReset: Button? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwreset)
        initializeGUI()
        btnReset!!.setOnClickListener {
            val email = atvEmail!!.text.toString()
            if (email.isEmpty()) {
                atvEmail!!.setError("Please, fill the email field.", null)
            } else {
                firebaseAuth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@PWresetActivity,
                            "Email has been sent successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        startActivity(Intent(this@PWresetActivity, LoginActivity::class.java))
                    } else {
                        Toast.makeText(
                            this@PWresetActivity,
                            "Invalid email address.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        tvSignin!!.setOnClickListener {
            startActivity(
                Intent(
                    this@PWresetActivity,
                    LoginActivity::class.java
                )
            )
        }
    }

    private fun initializeGUI() {
        ivLogo = findViewById(R.id.ivLogLogo)
        ivPWreset = findViewById(R.id.ivPassReset)
        tvInfo = findViewById(R.id.tvPWinfo)
        tvSignin = findViewById(R.id.tvGoBack)
        atvEmail = findViewById(R.id.atvEmailRes)
        btnReset = findViewById(R.id.btnReset)
        firebaseAuth = FirebaseAuth.getInstance()
    }
}