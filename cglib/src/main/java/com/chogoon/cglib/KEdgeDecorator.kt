package com.chogoon.cglib

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class KEdgeDecorator(private val padding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if(itemPosition == RecyclerView.NO_POSITION){
            return
        }
        outRect.top = padding

        parent.adapter?.let {
            if(itemPosition == it.itemCount - 1) outRect.bottom = padding
        }

    }
}