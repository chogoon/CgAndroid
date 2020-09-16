package com.chogoon.cglib;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CheckLiveData2<T, V> extends MediatorLiveData<T> {

    private List<MutableLiveData<V>> liveDatas = new ArrayList<>();

    @Override
    public <S> void addSource(@NonNull LiveData<S> source, @NonNull Observer<? super S> onChanged) {
        super.addSource(source, onChanged);
        if(source instanceof MutableLiveData){
            liveDatas.add((MutableLiveData<V>) source);
        }
    }

    public List<MutableLiveData<V>> getLiveDatas() {
        return liveDatas;
    }

    public int size(){
        return liveDatas.size();
    }

    public MutableLiveData<V> get(int i) {
        if( i < size()) return liveDatas.get(i);
        return null;
    }

    @Override
    public <S> void removeSource(@NonNull LiveData<S> toRemote) {
        super.removeSource(toRemote);
        if(toRemote instanceof MutableLiveData){
            liveDatas.remove(toRemote);
        }
    }

}
