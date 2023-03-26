package com.hifacy.nelikeme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.hifacy.nelikeme.R


class SliderAdapter(private val context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private val slide_images = intArrayOf(
        R.drawable.onboard1,
        R.drawable.onboard2,
//        R.drawable.ic_launcher_background
    )
    private val slide_headings = arrayOf(
        "Do you want to find your twin?",
        "Just send a selfie of yourself to see your twin.",
//        "CODE"
    )

    override fun getCount(): Int {
        return slide_headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        assert(layoutInflater != null)
        val view: View = layoutInflater!!.inflate(R.layout.slide_layout, container, false)
        val slideImageView = view.findViewById<ImageView>(R.id.imageView)
        val slideHeading = view.findViewById<TextView>(R.id.heading)
        val slideText = view.findViewById<TextView>(R.id.textPage)
        slideImageView.setImageResource(slide_images[position])
        slideHeading.text = slide_headings[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout?)
    }
}


//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.viewpager.widget.PagerAdapter
//import com.hifacy.nelikeme.R
//import com.hifacy.nelikeme.databinding.SlideLayoutBinding
//
//
//class SliderAdapter(private val context: Context) : PagerAdapter() {
//    private var layoutInflater: LayoutInflater? = null
//
//    var binding: SlideLayoutBinding? = null
//
//    private val slide_images = intArrayOf(
//        R.drawable.ic_launcher_background,
//        R.drawable.ic_launcher_foreground,
//    )
//    private val slide_headings = arrayOf(
//        "EAT",
//        "SLEEP",
//    )
//
//    override fun getCount(): Int {
//        return slide_headings.size
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view == `object`
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        assert(layoutInflater != null)
//        binding = SlideLayoutBinding.inflate(LayoutInflater.from(container.context), container, false)
////        binding = SlideLayoutBinding.inflate(layoutInflater!!,container,false)
////        val view: View = binding!!.inflate()/*(R.layout.slide_layout, container, false)*/
//
//        binding?.imageView?.setImageResource(slide_images[position])
//        binding?.textPage?.text = slide_headings[position]
//        container.addView(binding!!.root)
//        return binding as SlideLayoutBinding
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
////        container.removeView(`object` as ConstraintLayout?)
//        binding = null
//    }
//}


