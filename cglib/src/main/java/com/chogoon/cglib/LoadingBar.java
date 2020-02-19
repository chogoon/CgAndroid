package com.chogoon.cglib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

/** only loading view */
@Deprecated
public class LoadingBar extends LinearLayout {
	private Animation inAnimation;
	private Animation outAnimation;
    private ProgressBar progressBar;

	public LoadingBar(Context context) {
		this(context, null);
	}

    public LoadingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimations();
    }

    private void initAnimations(){
    	inAnimation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        outAnimation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        progressBar = new ProgressBar(getContext()) ;
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
        this.addView(progressBar);
    }

	@Override
	public void setVisibility(int flag) {
    	switch (flag) {
		case View.GONE:
		case View.INVISIBLE:
			hide(flag);
			break;
        case View.VISIBLE:
		default: show();
			break;
		}
	}

    @Override
    public void setSelected(boolean selected) {
        hide(selected);
        super.setSelected(selected);
    }

    public void show(){
        show(true);
    }

    public void show(boolean withAnimation){
        if (withAnimation) this.startAnimation(inAnimation);
        super.setVisibility(View.VISIBLE);
    }

    private void hide(int flag){
        if (!isVisible()) return;
        hide(true, flag);
    }

    public void hide(boolean withAnimation, final int flag) {
        if (withAnimation){
            outAnimation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LoadingBar.super.setVisibility(flag);
        		}
        	});
            this.startAnimation(outAnimation);
        }else{
            super.setVisibility(flag);
        }
    }

    public void hide(boolean withAnimation) {
        if (withAnimation){
        	outAnimation.setAnimationListener(new AnimationListener() {

        		@Override
        		public void onAnimationStart(Animation animation) {	}

        		@Override
        		public void onAnimationRepeat(Animation animation) { }

        		@Override
        		public void onAnimationEnd(Animation animation) {
                    LoadingBar.super.setVisibility(View.GONE);
        		}
        	});
        	this.startAnimation(outAnimation);
        }else{
        	super.setVisibility(View.GONE);
        }
    }

    public boolean isVisible(){
	    return (this.getVisibility() == View.VISIBLE);
    }

}