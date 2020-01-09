package com.chogoon.cglib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.chogoon.cglib.databinding.DialogBaseBinding;

public class BaseDialog extends Dialog implements View.OnClickListener {

    public final MutableLiveData<String> message = new MutableLiveData<>();

    public final ObservableBoolean one = new ObservableBoolean(false);
    private DialogBaseBinding binding;
    private View.OnClickListener onClickListener;

    public BaseDialog(Context context) {
        super(context, R.style.CgDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_base, null, false);
        setContentView(binding.getRoot());
        binding.setView(this);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (onClickListener != null) onClickListener.onClick(v);
    }

    public void setText(@NonNull String message, String confirm) {
        setMessage(message, true);
        if (!TextUtils.isEmpty(confirm)) {
            binding.ok.setText(confirm);
        }
    }

    public void setText(String message) {
        setText(message, "");
    }

    public void setText(int message, int confirm) {
        setText(message > 0 ? getContext().getString(message) : "", getContext().getString(confirm));
    }

    public void setText(int message) {
        setText(message > 0 ? getContext().getString(message) : "", "");
    }

    private void setMessage(String text, boolean isOne){
        show();
        one.set(isOne);
        message.setValue(text);
    }

    public void setText(String message, String left_text, String right_text) {
        setMessage(message, false);
        binding.left.setText(left_text);
        binding.right.setText(right_text);
    }

    public void setText(int message, int left_text, int right_text) {
        setText(message > 0 ? getContext().getString(message) : "", getContext().getString(left_text), getContext().getString(right_text));
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
