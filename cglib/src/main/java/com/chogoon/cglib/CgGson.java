package com.chogoon.cglib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

public class CgGson {
	private static Gson _instance;

	private CgGson(){ }

	public synchronized static Gson getInstance() {
		if (_instance == null){
			CgLog.e("init");
			_instance = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
		}
		return _instance;
	}

	public static <T> T getClassObjFromObject(Object object, Class<T> classOf) {
		return CgGson.getInstance().fromJson(CgGson.getInstance().toJson(object), classOf);
	}
}
