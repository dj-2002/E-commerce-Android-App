package com.sdp.ecommerce
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private var btnLogout: Button? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogout = findViewById(R.id.btnLogout)
        firebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = firebaseAuth!!.getCurrentUser()
        btnLogout!!.setOnClickListener(View.OnClickListener {
            firebaseAuth!!.signOut()
            finish()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        })
    }
}