package com.chogoon.cglib

import androidx.databinding.ObservableArrayList

interface CgObservable<T> {

    fun getObservableArrayList(): ObservableArrayList<*>

    fun getData(any: Any?)
    fun onData(data: T)
    fun submit(any: Any?)
    fun onComplete()
    fun onClear()
    fun isNoData(flag: Boolean)

}