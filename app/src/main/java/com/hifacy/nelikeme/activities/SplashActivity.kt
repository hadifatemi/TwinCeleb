package com.hifacy.nelikeme.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.hifacy.nelikeme.database.SharedPreference
import com.hifacy.nelikeme.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    var splashBinding: ActivitySplashBinding? = null
    var sharedPreferences: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportActionBar?.hide()
        setContentView(splashBinding?.root)

        sharedPreferences = SharedPreference(this)
        val firstStart: Boolean = sharedPreferences!!.getValueBoolean("firstStartBoarding")
        Handler().postDelayed({
            if (firstStart) {
                firstRun()
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
        },2000)

    }

    private fun firstRun() {
        val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//        sharedPreferences?.saveBoolean("firstStartBoarding", true)
        startActivity(intent)
        finish()
    }

}