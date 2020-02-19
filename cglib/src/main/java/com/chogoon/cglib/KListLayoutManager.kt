package com.chogoon.cglib

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class KListLayoutManager : LinearLayoutManager {
    private val MILLISECONDS_PER_INCH = 400f
    private val context :Context

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {
        this.context = context
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val smoothScroller = object : LinearSmoothScroller(context){

            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@KListLayoutManager.computeScrollVectorForPosition(targetPosition)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics) : Float{
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }
        
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }


}