//package com.chogoon.cglib
//
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import org.joda.time.DateTime
//
//class CgGsonIdontKnow private constructor(){
//
//    companion object {
//        val instance: Gson = TODO(
//                com.chogoon.cglib.CgGsonIdontKnow.Companion.instance = GsonBuilder().registerTypeAdapter(DateTime::class.java, DateTimeTypeConverter()).create()
//        )
//
//        fun <T> getClassObjFromObject(any: Any, classOf: Class<T>): T {
//            return getInstance().fromJson(getInstance().toJson(any), classOf)
//        }
//
//        @JvmStatic
//        @Synchronized
//        fun getInstance(): Gson {
//            if(instance == null)
//                instance = GsonBuilder().registerTypeAdapter(DateTime::class.java, DateTimeTypeConverter()).create()
//            return CgGsonIdontKnow.instance
//        }
//
//    }
//
//    init {
//        CgLog.e("init")
//        instance = GsonBuilder().registerTypeAdapter(DateTime::class.java, DateTimeTypeConverter()).create()
//    }
//
//}