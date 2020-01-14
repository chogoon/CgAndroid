package com.chogoon.cglib

import android.os.SystemClock
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.chibatching.kotpref.Kotpref
import java.util.*

open class CgApplication : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Kotpref.init(this)
        PreferencesManager.baseKey = SystemClock.uptimeMillis().toString()
    }
}