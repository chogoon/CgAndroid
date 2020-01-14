package com.chogoon.cglib

import android.content.Context
import android.content.Intent

open class BadgeNotificator {

    companion object{

        @JvmStatic
        fun broadcastBadgeEvent(context: Context, clazz : Class<*>, count: Int ){
            val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
            intent.putExtra("badge_count_package_name", context.packageName)
            intent.putExtra("badge_count_class_name", clazz.name)
            intent.putExtra("badge_count", count)
        }
    }
}