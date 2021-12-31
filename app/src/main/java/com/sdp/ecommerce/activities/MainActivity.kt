package com.sdp.ecommerce.activities
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sdp.ecommerce.R
import com.sdp.ecommerce.auth.LoginActivity

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private var btnLogout: Button? = null
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogout = findViewById(R.id.btnLogout)
        firebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = firebaseAuth.getCurrentUser()
        btnLogout!!.setOnClickListener(View.OnClickListener {
            firebaseAuth.signOut()
            finish()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        })

        if(firebaseAuth.currentUser==null){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
//        val intent = intent
//        val emailLink = intent.data.toString()

// Confirm the link is a sign-in with email link.
//        if (firebaseAuth != null && firebaseAuth!!.isSignInWithEmailLink(emailLink)) {
//            // Retrieve this from wherever you stored it
//            val email = "someemail@domain.com"
//
//            // The client SDK will parse the code from the link for you.
//            firebaseAuth!!.signInWithEmailLink(email, emailLink)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.e(TAG, "Successfully signed in with email link!")
//                        val result = task.result
//                        // You can access the new user via result.getUser()
//                        // Additional user info profile *not* available via:
//                        // result.getAdditionalUserInfo().getProfile() == null
//                        // You can check if the user is new or existing:
//                        // result.getAdditionalUserInfo().isNewUser()
//                    } else {
//                        Log.e(TAG, "Error signing in with email link", task.exception)
//                    }
//                }
//        }



        firebaseAuth?.currentUser?.apply {
//            val view = layoutInflater.inflate(R.layout.activity_main, null)

            if (!isEmailVerified) {
                Toast.makeText(this@MainActivity, "Not Verified", Toast.LENGTH_SHORT).show()
//                Snackbar.make(this@MainActivity, view, "Not Verified", Snackbar.LENGTH_LONG)
            }else{
                Toast.makeText(this@MainActivity, "Verified", Toast.LENGTH_SHORT).show()
//                Snackbar.make(this@MainActivity, view, "Verified", Snackbar.LENGTH_LONG)
            }
        }
    }
}