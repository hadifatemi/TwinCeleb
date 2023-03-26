package com.hifacy.nelikeme.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.hifacy.nelikeme.R
import com.hifacy.nelikeme.database.SharedPreference
import com.hifacy.nelikeme.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {

    private var resultBinding: ActivityResultBinding? = null
    private var sharedPreference: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(resultBinding!!.root)

        sharedPreference = SharedPreference(this)

        resultBinding!!.txtTwinName.text = sharedPreference!!.getValueString("twinName")

        Glide.with(this).asGif().load(R.raw.celebration).into(resultBinding!!.celebrateImage)

        Glide.with(this)
            .load(convertBase64ToBitmap(sharedPreference!!.getValueString("userImage")?.split(",")!![1]))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .centerInside()
            .into(resultBinding!!.imgUserImage)

        Glide.with(this)
            .load(convertBase64ToBitmap(sharedPreference!!.getValueString("twinImage")?.split(",")!![1]))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .centerInside()
            .into(resultBinding!!.imgTwinImage)

        resultBinding!!.findAgain.setOnClickListener {
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
        }

        resultBinding!!.txtRate.setOnClickListener {
            askRatings()
        }

    }


    private fun convertBase64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes = Base64.decode(b64.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }

    fun askRatings() {
        val manager: ReviewManager = ReviewManagerFactory.create(this)
        val request: Task<ReviewInfo> = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                val reviewInfo: ReviewInfo = task.result
                val flow: Task<Void> = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { task2 -> }
            } else {
                // There was some problem, continue regardless of the result.
            }
        }
    }

}