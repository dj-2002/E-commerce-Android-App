package com.sdp.ecommerce.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sdp.ecommerce.R
import com.sdp.ecommerce.activities.MainActivity

class LoginActivity : AppCompatActivity() {
    private var logo: ImageView? = null
    private var ivSignIn: ImageView? = null
    private var btnTwitter: ImageView? = null
    private var email: AutoCompleteTextView? = null
    private var password: AutoCompleteTextView? = null
    private var forgotPass: TextView? = null
    private var signUp: TextView? = null
    private var btnSignIn: Button? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeGUI()
        user = firebaseAuth?.getCurrentUser()
        if (user != null) {
            finish()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        btnSignIn!!.setOnClickListener {
            val inEmail: String = email?.getText().toString()
            val inPassword: String = password?.getText().toString()
            if (validateInput(inEmail, inPassword)) {
                signUser(inEmail, inPassword)
            }
        }
        signUp?.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegistrationActivity::class.java
                )
            )
            finish()
        })
        forgotPass?.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    PWresetActivity::class.java
                )
            )
            finish()
        })
    }

    fun signUser(email: String?, password: String?) {
        progressDialog?.setMessage("Verificating...")
        progressDialog?.show()
        if (email != null) {
            if (password != null) {
                firebaseAuth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                        override fun onComplete(p0: Task<AuthResult?>) {
                            if (p0.isSuccessful()) {
                                progressDialog?.dismiss()
                                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                progressDialog?.dismiss()
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Invalid email or password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        }
    }

    private fun initializeGUI() {
        logo = findViewById(R.id.ivLogLogo)
        ivSignIn = findViewById(R.id.ivSignIn)
        btnTwitter = findViewById(R.id.ivFacebook)
        email = findViewById(R.id.atvEmailLog)
        password = findViewById(R.id.atvPasswordLog)
        forgotPass = findViewById(R.id.tvForgotPass)
        signUp = findViewById(R.id.tvSignIn)
        btnSignIn = findViewById(R.id.btnSignIn)
        progressDialog = ProgressDialog(this)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun validateInput(inemail: String, inpassword: String): Boolean {
        if (inemail.isEmpty()) {
            email?.setError("Email field is empty.")
            return false
        }
        if (inpassword.isEmpty()) {
            password?.setError("Password is empty.")
            return false
        }
        return true
    }
}