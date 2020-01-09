package com.chogoon.cglib;

import androidx.databinding.ObservableList;

public abstract class OnListChangedCallback extends ObservableList.OnListChangedCallback {
    public OnListChangedCallback() {
        super();
    }

    @Override
    public void onChanged(ObservableList sender) {

    }

    @Override
    public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {

    }

    @Override
    public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {

    }

    @Override
    public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

    }

    @Override
    public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {

    }
}
