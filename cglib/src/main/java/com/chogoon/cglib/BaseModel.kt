package com.chogoon.cglib

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson

open class BaseModel : Parcelable {

    val tag: String get() = this::class.java.simpleName.decapitalize()

    constructor()

    constructor(source: Parcel) : this()

    open fun isSuccess(): Boolean {
        return false
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return Gson().toJson(this) + this::class.java.simpleName
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {

    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<BaseModel> = object : Parcelable.Creator<BaseModel> {
            override fun createFromParcel(source: Parcel): BaseModel = BaseModel(source)
            override fun newArray(size: Int): Array<BaseModel?> = arrayOfNulls(size)
        }
    }


}
