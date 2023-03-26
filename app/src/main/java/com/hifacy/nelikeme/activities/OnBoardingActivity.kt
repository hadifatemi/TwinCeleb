package com.hifacy.nelikeme.activities

import android.R.*
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.hifacy.nelikeme.R
import com.hifacy.nelikeme.adapters.SliderAdapter
import com.hifacy.nelikeme.database.SharedPreference
import com.hifacy.nelikeme.databinding.ActivityOnBoardingBinding


class OnBoardingActivity : AppCompatActivity() {

    var sharedPreference: SharedPreference? = null

    var onBoardBinding: ActivityOnBoardingBinding? = null

    private var sliderAdapter: SliderAdapter? = null

    private var currentPage = 0
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBoardBinding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(onBoardBinding!!.root)

        sharedPreference = SharedPreference(this)

        sliderAdapter = SliderAdapter(this)
        onBoardBinding!!.slideViewPager.setAdapter(sliderAdapter)
        addDots()

        onBoardBinding!!.slideViewPager.setCurrentItem(currentPage)
        onBoardBinding!!.backButton.setOnClickListener { onBoardBinding!!.slideViewPager.setCurrentItem(currentPage - 1) }
        onBoardBinding!!.nextButton.setOnClickListener {
            if (currentPage == 0) {
                onBoardBinding!!.slideViewPager.setCurrentItem(currentPage + 1)
            }
            else {
                sharedPreference!!.saveBoolean("firstStartBoarding", false)
                val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
        }

        onBoardBinding!!.nextButton.isEnabled = true
        onBoardBinding!!.backButton.isEnabled = false
        onBoardBinding!!.backButton.visibility = View.INVISIBLE
        onBoardBinding!!.nextButton.setText(getResources().getText(R.string.next))
        onBoardBinding!!.backButton.setText("nothing")

        val res: Resources = resources
        val drawableId: Int = R.drawable.selected_item_dot
        val drawable: Drawable = res.getDrawable(drawableId)
        dots!![0].setImageDrawable(drawable)
        dots!![0].updatePadding(right =  8, left = 8)
    }

    private val NUM_PAGES: Int = 2
    private val mViewPager: ViewPager? = null
    private var dots: List<ImageView>? = null
    @SuppressLint("UseCompatLoadingForDrawables")
    fun addDots() {
        dots = ArrayList()
        currentPage = 0
        for (i in 0 until NUM_PAGES) {
            val dot = ImageView(this)
            dot.setImageDrawable(resources.getDrawable(R.drawable.non_selected_item_dot))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            onBoardBinding?.dotsLayout?.addView(dot, params)
            (dots as ArrayList<ImageView>).add(dot)
        }
        onBoardBinding!!.slideViewPager.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                selectDot(position)
                currentPage = position

                if (currentPage == 0) {
                    onBoardBinding!!.nextButton.isEnabled = true
                    onBoardBinding!!.backButton.isEnabled = false
                    onBoardBinding!!.backButton.visibility = View.INVISIBLE
                    onBoardBinding!!.nextButton.text = resources.getText(R.string.next)
                    onBoardBinding!!.backButton.text = "nothing"
                } else {
                    onBoardBinding!!.nextButton.isEnabled = true
                    onBoardBinding!!.backButton.isEnabled = true
                    onBoardBinding!!.backButton.visibility = View.VISIBLE
                    onBoardBinding!!.nextButton.text = resources.getText(R.string.next)
                    onBoardBinding!!.backButton.text = resources.getText(R.string.back)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun selectDot(idx: Int) {
        val res: Resources = resources
        for (i in 0 until NUM_PAGES) {
            val drawableId: Int =
                if (i == idx) R.drawable.selected_item_dot else R.drawable.non_selected_item_dot
            val drawable: Drawable = res.getDrawable(drawableId)
            dots!![i].setImageDrawable(drawable)
            dots!![i].updatePadding(right =  8, left = 8)
        }
    }

}
