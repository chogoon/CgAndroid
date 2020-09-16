package com.chogoon.cglib

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class CheckLiveData<T> : MediatorLiveData<T>() {

    private val liveDataList = ArrayList<MutableLiveData<*>>()

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        super.addSource(source, onChanged)
        if (source is MutableLiveData<*>) {
            liveDataList.add(source)
        }
    }

    public fun getLiveDataList() : List<MutableLiveData<*>> {
        return liveDataList
    }

    fun size(): Int {
        return liveDataList.size
    }

    operator fun get(i: Int): MutableLiveData<*>? {
        return if (i < size()) liveDataList[i] else null
    }

    override fun <S : Any?> removeSource(toRemote: LiveData<S>) {
        super.removeSource(toRemote)
        if (toRemote is MutableLiveData){
            liveDataList.remove(toRemote)
        }
    }



}