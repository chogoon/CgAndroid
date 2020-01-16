package com.chogoon.cgbase.ui.main

import android.os.Bundle
import android.util.Base64
import com.chogoon.cgbase.R
import com.chogoon.cgbase.databinding.ActivityMainBinding
import com.chogoon.cglib.BaseActivity
import com.chogoon.cglib.CgLog
import com.chogoon.cglib.KCgUtils.toCommaFormat
import com.chogoon.cglib.SEEDCBC

class KMainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun setup() {
        setBinding(R.layout.activity_main, MainViewModel::class.java)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        val temp : Int = 32432432
        CgLog.e(temp.toCommaFormat())

    }

    companion object {

        @JvmStatic
        fun encryptBase(text: String): String {
            val plainText = text.toByteArray()
            val cipherText = ByteArray(text.length + 16)
            var outputLength: Int

            val seed = SEEDCBC()
            seed.init(SEEDCBC.ENC, SEEDCBC.KEY, SEEDCBC.IV)
            outputLength = seed.process(plainText, 0, plainText.size, cipherText, 0)
            outputLength += seed.close(cipherText, outputLength)

            val sliceCipher = ByteArray(outputLength)
            System.arraycopy(cipherText, 0, sliceCipher, 0, outputLength)
            return Base64.encodeToString(sliceCipher, 0)

        }

        @JvmStatic
        fun decryptBase(text: String?): String {
            if (text.isNullOrEmpty()) return ""

            val plainText = ByteArray(144)
            val cipherText = Base64.decode(text, 0)
            val outputLength: Int
            val seed = SEEDCBC()
            seed.init(SEEDCBC.DEC, SEEDCBC.KEY, SEEDCBC.IV)
            outputLength = seed.process(cipherText, 0, cipherText.size, plainText, 0)
            seed.close(plainText, outputLength)
            return String(plainText)

        }
    }
}