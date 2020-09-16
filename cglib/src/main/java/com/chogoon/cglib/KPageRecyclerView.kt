package com.chogoon.cglib

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class KPageRecyclerView : RecyclerView {

    private var observable: CgObservable<*>? = null
    private var smoothScroller: LinearSmoothScroller? = null
    private var listCallback: OnListChangedCallBack<*>? = null
    private var page: Int = 1

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        itemAnimator = DefaultItemAnimator()
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(SCROLL_STATE_DRAGGING)) {
                        recyclerView.tag?.let {
                            observable?.getData(recyclerView.tag)
                        }
                    }
                }
            }
        })

        smoothScroller = object : LinearSmoothScroller(context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 120f / displayMetrics.densityDpi
            }
        }

        listCallback = object : OnListChangedCallBack<ObservableList<*>>() {
            override fun onItemRangeInserted(sender: ObservableList<*>, positionStart: Int, itemCount: Int) {
                if(itemCount > 0){
                    startSmoothScroll(itemCount)
                    observable?.let {
                        tag = 1.let { page += it; page }
                    }
                }else{
                    tag = null
                    isEmpty()
                }

            }
        }
    }

    private fun isEmpty() {
        observable?.isNoData(adapter?.itemCount == 0)
    }

    public fun setObservable(observable: CgObservable<*>) {
        this.observable = observable
    }

    private fun addOnListChangedCallback() {
        observable!!.getObservableArrayList().addOnListChangedCallback(listCallback)
    }

    private fun removeOnListChangedCallback() {
        observable!!.getObservableArrayList().removeOnListChangedCallback(listCallback)
    }

    private fun startSmoothScroll(itemCount: Int) {
        adapter?.let {
            if (it.itemCount > 0 && it.itemCount - itemCount > 0) {
                smoothScroller?.targetPosition = it.itemCount - itemCount
                handler.postDelayed({ layoutManager?.startSmoothScroll(smoothScroller) }, 250)
            }
        }
    }

    override fun onDetachedFromWindow() {
        observable?.let { removeOnListChangedCallback() }
        super.onDetachedFromWindow()
    }
}