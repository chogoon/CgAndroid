package com.chogoon.cglib

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager

class BaseViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    init {
        setScroller()
    }

    private fun setScroller() {
        try {
            val viewPager = ViewPager::class.java
            val scrollerField = viewPager.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            val interpolatorField = viewPager.getDeclaredField("sInterpolator")
            interpolatorField.isAccessible = true
            scrollerField.set(this, ScrollerDuration(context, interpolatorField.get(null) as Interpolator))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }


}