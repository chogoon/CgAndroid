package com.chogoon.cgbase.ui.main

import android.os.Bundle
import com.chogoon.cgbase.R
import com.chogoon.cgbase.databinding.ActivityMainBinding
import com.chogoon.cglib.BaseActivity
import com.chogoon.cglib.CgLog
import com.chogoon.cglib.KCgUtils.toCommaFormat
import com.chogoon.cglib.crypto.KCrypto.Companion.getSHA256

class KMainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun setup() {
        setBinding(R.layout.activity_main, MainViewModel::class.java)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        val temp: Int = 32432432
        CgLog.e(temp.toCommaFormat())

//        val encrypt = encryptBase("chogoonAce")
//        binding.sample1.text = encrypt
//        binding.sample2.text = decryptBase(encrypt)

        CgLog.e(12345.toCommaFormat())

        binding.sample3.text = getSHA256("chogoon")

    }

    companion object {


    }
}