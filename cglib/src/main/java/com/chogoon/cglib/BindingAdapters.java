package com.chogoon.cglib;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BindingAdapters {


    @BindingAdapter("gone")
    public static void gone(View v, Boolean visibility) {
        v.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("invisible")
    public static void invisible(View v, Boolean visibility) {
        v.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("adapter")
    public static void bindAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("observe")
    public static void bindObserve(PageRecyclerView recyclerView, CgObservable observable) {
        if (recyclerView != null) {
            recyclerView.setObservable(observable);
        }
    }

    @BindingAdapter("items")
    public static void bindItems(RecyclerView recyclerView, List data) {
        setData((BaseRecyclerViewAdapter) recyclerView.getAdapter(), data);
    }

    private static void setData(BaseRecyclerViewAdapter adapter, List data) {
        if (adapter != null) adapter.setData(data);
    }

    @BindingAdapter("text")
    public static void bindText(TextView v, String text) {
        if (text.equals(v.getText().toString())) return;
        v.setText(toHtml(text));
    }

    private static Spanned toHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }
}
