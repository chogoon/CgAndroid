package com.chogoon.cglib;

import android.app.Application;
import android.view.View;

import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@Deprecated
public abstract class CgViewModel2<T> extends BaseViewModel implements CgObservable<T>, Observable, View.OnClickListener {

    public final MutableLiveData<T> liveData = new MutableLiveData<>();
    public final ObservableArrayList<T> items = new CgObservableArrayList<>();

    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CgViewModel2(Application application) {
        super(application);
    }

    @Override
    public void onData(T data) {

    }

    @Override
    public void getData(Object data) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void submit(Object data) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public ObservableArrayList getObservableArrayList() {
        return items;
    }

    @Override
    public void isNoData(boolean value) {
        no_data.postValue(value);
    }

    @Override
    public void onClear() {
        items.clear();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }

    public void notifyChange() {
        callbacks.notifyCallbacks(this, 0, null);
    }

    public void notifyPropertyChanged(int fieldId) {
        callbacks.notifyCallbacks(this, fieldId, null);
    }

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        onClear();
        super.onCleared();
    }
}

