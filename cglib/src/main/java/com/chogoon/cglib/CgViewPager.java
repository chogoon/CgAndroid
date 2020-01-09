package com.chogoon.cglib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class CgViewPager extends ViewPager {

    public CgViewPager(Context context) {
        this(context, null);
    }

    public CgViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            Class<?> viewpager = ViewPager.class;
            Field scrollerField = viewpager.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = viewpager.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);
            scrollerField.set(this, new ScrollerDuration(context, (Interpolator) interpolatorField.get(null)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

}
