package com.chogoon.cglib;

import androidx.databinding.ObservableArrayList;

public interface CgObservable<T> {

    ObservableArrayList getObservableArrayList();

    void getData(Object data);

    void onData(T data);

    void submit(Object data);

    void onComplete();

    void onClear();

    void isNoData(boolean value);


}

