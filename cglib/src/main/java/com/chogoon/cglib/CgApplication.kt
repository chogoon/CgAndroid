package com.chogoon.cglib

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.chibatching.kotpref.Kotpref

open class CgApplication : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Kotpref.init(this)

    }
}