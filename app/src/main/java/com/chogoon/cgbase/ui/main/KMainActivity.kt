package com.chogoon.cgbase.ui.main

import android.os.Bundle
import android.util.Base64
import com.chogoon.cgbase.R
import com.chogoon.cgbase.databinding.ActivityMainBinding
import com.chogoon.cglib.BaseActivity
import com.chogoon.cglib.CgLog
import com.chogoon.cglib.KCgUtils.toCommaFormat
import com.chogoon.cglib.crytpto.SEEDCBC

class KMainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun setup() {
        setBinding(R.layout.activity_main, MainViewModel::class.java)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        val temp : Int = 32432432
        CgLog.e(temp.toCommaFormat())

    }

    companion object {


    }
}