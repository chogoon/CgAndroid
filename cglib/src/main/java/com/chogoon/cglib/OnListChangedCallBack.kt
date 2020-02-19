package com.chogoon.cglib

import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback

abstract class OnListChangedCallBack<T : ObservableList<*>> : OnListChangedCallback<T>() {

    override fun onChanged(sender: T) {

    }

    override fun onItemRangeRemoved(sender: T, positionStart: Int, itemCount: Int) {

    }

    override fun onItemRangeMoved(sender: T, fromPosition: Int, toPosition: Int, itemCount: Int) {

    }

    override fun onItemRangeInserted(sender: T, positionStart: Int, itemCount: Int) {

    }

    override fun onItemRangeChanged(sender: T, positionStart: Int, itemCount: Int) {}
}