package com.chogoon.cglib

import com.chibatching.kotpref.KotprefModel

object PreferencesManager : KotprefModel() {


    private var _baseKey by stringPref(default = CgUtils.getSHA256(context.getString(R.string.app_name)))

    @JvmStatic
    var baseKey
        get() = _baseKey
        set(value) {
            _baseKey = CgUtils.getSHA256(value)
        }

}