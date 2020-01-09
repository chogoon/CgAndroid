package com.chogoon.cglib

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class ScrollerDuration(context: Context, interpolator: Interpolator) :
    Scroller(context, interpolator) {
    private var scrollFactor: Double = 0.toDouble()

    init {
        this.scrollFactor = 1.0
    }

    fun setScrollDurationFactor(scrollFactor: Double) {
        this.scrollFactor = scrollFactor
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, (1.0 * this.scrollFactor).toInt())
    }

}
