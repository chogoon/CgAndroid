package com.chogoon.cglib

import com.chibatching.kotpref.KotprefModel
import com.chogoon.cglib.crypto.KCrypto

object PreferencesManager : KotprefModel() {


    private var _baseKey by stringPref(default = KCrypto.getSHA256(context.getString(R.string.app_name)))

    @JvmStatic
    var baseKey
        get() = _baseKey
        set(value) {
            _baseKey = KCrypto.getSHA256(value)
        }

}