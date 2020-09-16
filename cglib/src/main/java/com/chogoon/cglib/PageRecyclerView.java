package com.chogoon.cglib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

@Deprecated
public class PageRecyclerView extends RecyclerView {

    private CgObservable observable;
    private LinearSmoothScroller smoothScroller;
    private ObservableList.OnListChangedCallback listCallback;
    private int page = 1; //시작은 1부터

    public PageRecyclerView(Context context) {
        this(context, null);
    }

    public PageRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setItemAnimator(new DefaultItemAnimator());
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(SCROLL_STATE_DRAGGING)) {
                        if (recyclerView.getTag() != null) {
                            if (observable != null) observable.getData(recyclerView.getTag());
                        }
                    }
                }
            }
        });

        smoothScroller = new LinearSmoothScroller(getContext()) {
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 120f / displayMetrics.densityDpi;
            }
        };

        listCallback = new OnListChangedCallBack() {

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                if (itemCount > 0) {
                    startSmoothScroll(itemCount);
                    if (observable != null) {
                        setTag(page += 1);
                        observable.isNoData(false);
                    }
                } else {
                    setTag(null);
                    isEmpty();
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                isEmpty();
                getAdapter().notifyDataSetChanged();
            }
        };
    }

    private void isEmpty() {
        if (observable != null) observable.isNoData(getAdapter().getItemCount() == 0);
    }

    public void setObservable(CgObservable observable) {
        this.observable = observable;
        addOnListChangedCallback();
    }

    private void addOnListChangedCallback() {
        observable.getObservableArrayList().addOnListChangedCallback(listCallback);
    }

    private void removeOnListChangedCallback() {
        observable.getObservableArrayList().removeOnListChangedCallback(listCallback);
    }

    public void startSmoothScroll(int itemCount) {
        if (getAdapter().getItemCount() > 0 && getAdapter().getItemCount() - itemCount > 0) {
            smoothScroller.setTargetPosition(getAdapter().getItemCount() - itemCount);
            getHandler().postDelayed(() -> getLayoutManager().startSmoothScroll(smoothScroller), 250);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (observable != null) removeOnListChangedCallback();
        super.onDetachedFromWindow();
    }
}
