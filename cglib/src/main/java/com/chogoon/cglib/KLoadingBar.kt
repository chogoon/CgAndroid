package com.chogoon.cglib

import android.content.Context
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat

/** only loading view */
class KLoadingBar : LinearLayout {

    private val inAnimation: Animation
    private val outAnimation: Animation
    private val progressBar: ProgressBar

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        outAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        progressBar = ProgressBar(context)
        progressBar.indeterminateDrawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY)
        addView(progressBar)
    }

    override fun setVisibility(visibility: Int) {
        when (visibility) {
            View.GONE, View.INVISIBLE -> hide(visibility)
            else -> show()
        }
    }

    override fun setSelected(selected: Boolean) {
        hide(selected)
        super.setSelected(selected)
    }

    public fun show() {
        show(true)
    }

    public fun show(withAnimation: Boolean) {
        if (withAnimation) startAnimation(inAnimation)
        super.setVisibility(View.VISIBLE)
    }

    private fun hide(visibility: Int) {
        if (!isVisible()) return
        hide(true, visibility)
    }

    private fun hide(withAnimation: Boolean, visibility: Int) {
        if (withAnimation) {
            outAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    super@KLoadingBar.setVisibility(visibility)
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
            startAnimation(outAnimation)
        } else {
            super.setVisibility(visibility)
        }
    }

    private fun hide(withAnimation: Boolean) {
        if (withAnimation) {
            outAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    super@KLoadingBar.setVisibility(View.GONE)
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
            startAnimation(outAnimation)
        } else {
            super.setVisibility(View.GONE)
        }
    }

    private fun isVisible(): Boolean {
        return visibility == View.VISIBLE
    }
}