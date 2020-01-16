package com.chogoon.cglib

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.graphics.toColor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import java.math.BigInteger
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.*

object KCgUtils {

    @JvmName("commaFormat")
    fun Int.toCommaFormat(): String {
        return DecimalFormat("#,###").format(this)
    }

    @JvmName("pointFormat")
    fun Float.toPointFormat(): String {
        return String.format(Locale.getDefault(), "%.1f", this)
    }

    @JvmName("strformat")
    fun String.toFormat(vararg values: Any): String {
        return Formatter(Locale.getDefault()).format(this, values).toString()
    }

    @JvmName("createBody")
    fun String.toCreateBody(vararg values: Array<out Any>): RequestBody {
        return this.toFormat(values).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

    @JvmName("isNumber")
    fun String.isNumber() : Boolean{
        if (this.isEmpty()) return false
        return this.matches(Regex("-?\\d+(\\.\\d+)?"))
    }

    @JvmName("getDrawable")
    fun Context.toDrawable(@DrawableRes id : Int) : Drawable?{
        return ActivityCompat.getDrawable(this, id)
    }

    @JvmName("getColor")
    fun Context.toColor(@ColorRes id : Int) : Int{
        return ActivityCompat.getColor(this, id)
    }

    fun Class<*>.getTag() : String{
        return this.simpleName
    }

    fun String.getSHA256() : String{
        var encrypt = ""
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(this.toByteArray(), 0, this.length)
            encrypt = BigInteger(1, md.digest()).toString(16)
        }catch (e : Exception){
            e.printStackTrace()
        }
        return encrypt
    }

}