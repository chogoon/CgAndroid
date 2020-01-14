package com.chogoon.cglib;

import androidx.databinding.ListChangeRegistry;
import androidx.databinding.ObservableArrayList;
import java.util.Collection;

public class CgObservableArrayList<T> extends ObservableArrayList<T> {

    private transient ListChangeRegistry listeners = new ListChangeRegistry();

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if(c.size() == 0) {
            listeners.notifyInserted(this, 0, 0);
        }
        return super.addAll(c);
    }

    @Override
    public void addOnListChangedCallback(OnListChangedCallback listener) {
        listeners.add(listener);
        super.addOnListChangedCallback(listener);
    }

    @Override
    public void removeOnListChangedCallback(OnListChangedCallback listener) {
        listeners.remove(listener);
        super.removeOnListChangedCallback(listener);
    }
}
