package com.sdp.ecommerce.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sdp.ecommerce.R

class SplashScrActivity : AppCompatActivity() {
    private lateinit var logoSplash: ImageView
    private lateinit var chmaraTech: ImageView
    private lateinit var logoWhite: ImageView
    private lateinit var anim1: Animation
    private lateinit var anim2: Animation
    private lateinit var anim3: Animation
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        getWindow().setFlags(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.MATCH_PARENT
//        )
        setContentView(R.layout.activity_splash_scr)
        init()
        logoSplash.startAnimation(anim1)
        anim1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                logoSplash.startAnimation(anim2)
                logoSplash.visibility = View.GONE
                logoWhite.startAnimation(anim3)
                chmaraTech.startAnimation(anim3)
                anim3.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        logoWhite.visibility = View.VISIBLE
                        chmaraTech.visibility = View.VISIBLE
                        finish()
                        startActivity(
                            Intent(
                                this@SplashScrActivity,
                                MainActivity::class.java
                            )
                        )
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun init() {
        logoSplash = findViewById(R.id.ivLogoSplash)
        logoWhite = findViewById(R.id.ivLogoWhite)
        chmaraTech = findViewById(R.id.ivCHTtext)
        anim1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate)
        anim2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout)
        anim3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein)
    }
}