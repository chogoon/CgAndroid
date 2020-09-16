package com.chogoon.cglib

import android.app.Application
import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class CgViewModel<T>(application: Application) : BaseViewModel(application), CgObservable<T>, Observable, View.OnClickListener {

    public val liveData = MutableLiveData<T>()
    public val items = CgObservableArrayList<T>()

    private val callbacks = PropertyChangeRegistry()
    private val compositeDisposable = CompositeDisposable()

    override fun onData(data: T) {}

    override fun getData(data: Any?) {}

    override fun onClick(v: View?) {}

    override fun submit(data: Any?) {}

    override fun onComplete() {}

    override fun getObservableArrayList(): ObservableArrayList<*> {
        return items
    }

    override fun isNoData(value: Boolean) {
        no_data.postValue(value)
    }

    override fun onClear() {
        items.clear()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    open fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    open fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

    open fun addDisposable(disposable: Disposable?) {
        disposable?.let {
            compositeDisposable.add(it)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        onClear()
        super.onCleared()
    }

}