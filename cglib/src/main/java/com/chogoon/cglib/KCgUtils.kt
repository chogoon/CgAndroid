package com.chogoon.cglib

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    fun ByteArray.toHexString(): String {
        var hexString = ""
        forEachIndexed { i, byte ->
            hexString += if (i < size - 1) {
                "%02X:".format(byte)
            } else {
                "%02X".format(byte)
            }
        }
        return hexString
    }

    fun ByteArray.toHexString(length: Int): String {
        var hexString = ""
        forEachIndexed { i, byte ->
            hexString += if (i != length - 1) {
                "%02X:".format(byte)
            } else {
                "%02X".format(byte)
            }
        }
        return hexString
    }

    fun subBytes(src: ByteArray, begin: Int, end: Int): ByteArray {
        val length = end - begin + 1
        val result = ByteArray(length)
        System.arraycopy(src, begin, result, 0, length)
        return result
    }
}