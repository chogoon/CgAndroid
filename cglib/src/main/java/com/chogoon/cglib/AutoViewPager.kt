package com.chogoon.cglib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.animation.Interpolator
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


class AutoViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private val AUTO_SCROLL = 332211
    private val INTERNAL_IM_MILLIS = 3000
    private val SCROLL_TIME = 700

    private var scroller: ScrollerDuration? = null
    private var selfHandler = SelfCallBackHandler()

    private var heightMeasure = 0
    private var autoScroll = false
    private var curPosition: Int = 0
    private var interval = INTERNAL_IM_MILLIS

    init {
        addOnPageChangeListener(InnerOnPageChangeListener())
        setScrollDuration(SCROLL_TIME)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (heightMeasure == 0) {
            heightMeasure = heightMeasureSpec
            try {
                val child = getChildAt(0)
                if (child != null) {
                    child.measure(
                            widthMeasureSpec,
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    )
                    val h = child.measuredHeight
                    heightMeasure = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasure)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoScroll()
    }


    fun startAutoScroll() {
        if (!autoScroll) startAutoScroll(if (interval > 0) interval else INTERNAL_IM_MILLIS)
    }

    private fun startAutoScroll(interval: Int) {
        if (adapter != null && adapter?.count!! > 1) {
            this.interval = interval
            autoScroll = true
            selfHandler.removeMessages(AUTO_SCROLL)
            selfHandler.sendEmptyMessageDelayed(AUTO_SCROLL, interval.toLong())
        }
    }

    fun stopAutoScroll() {
        autoScroll = false
        selfHandler.removeMessages(AUTO_SCROLL)
    }

    private fun setScrollDuration(duration: Int) {
        setScrollerIfNeeded()
        scroller?.setScrollDurationFactor(duration.toDouble())
    }

    private fun setScrollerIfNeeded() {
        if (scroller != null) return
        try {
            val viewPager = ViewPager::class.java
            val scrollerField = viewPager.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            val interpolatorField = viewPager.getDeclaredField("sInterpolator")
            interpolatorField.isAccessible = true
            scroller = ScrollerDuration(context, interpolatorField.get(null) as Interpolator)
            scrollerField.set(this, scroller)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("HandlerLeak")
    private inner class SelfCallBackHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                AUTO_SCROLL -> {
                    val position = getPosition(currentItem)
                    if (autoScroll) currentItem = position + 1
                    removeMessages(AUTO_SCROLL)
                    sendEmptyMessageDelayed(AUTO_SCROLL, interval.toLong())
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    private inner class InnerOnPageChangeListener : OnPageChangeListener {

        override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            curPosition = position
            if (curPosition == SCROLL_STATE_IDLE) {
                postDelayed({ onPageScrollStateChanged(0) }, 0)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == SCROLL_STATE_IDLE) {
                startAutoScroll()
            } else if (state == SCROLL_STATE_DRAGGING || state == SCROLL_STATE_SETTLING) {
                stopAutoScroll()
            }
        }
    }

    private fun getPosition(position: Int): Int {
        val adapter = adapter as PagerAdapter
        val count = adapter.count
        var temp = position
        if (temp >= count - 1) temp -= count
        return temp
    }

}
