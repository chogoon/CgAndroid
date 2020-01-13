package com.chogoon.cglib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;


public class CgGson {
	private static Gson _instance;

	private CgGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());
		_instance = builder.create();
	}

	public synchronized static Gson getInstance() {
		if (_instance == null) new CgGson();
		return _instance;
	}

	public static <T> T getClassObjFromObject(Object object, Class<T> classOf) {
		return CgGson.getInstance().fromJson(CgGson.getInstance().toJson(object), classOf);
	}
}
