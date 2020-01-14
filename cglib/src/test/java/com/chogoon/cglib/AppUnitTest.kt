package com.chogoon.cglib

import android.os.SystemClock
import org.junit.Assert.*
import org.junit.Test

class AppUnitTest {

    @Test
    fun baseKey(){
        assert(System.currentTimeMillis().toString() is String)
        assertEquals(SystemClock.uptimeMillis().toString() , "0")
//        Assert.assert(System.currentTimeMillis().toString() as String)

    }
}