package com.chogoon.cglib;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chogoon.cglib.data.BaseModel;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, M extends BaseModel> extends RecyclerView.Adapter<VH> {

    private List<M> items;
    private Context context;
    private View.OnClickListener onClickListener;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    public void setData(List<M> listItems) {
        this.items = listItems;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public List<M> getData() {
        return items;
    }

    public M getItem(int index) {
        return items.get(index);
    }

    public void addData(int index, Object item) {
        List temp = this.items;
        temp.add(index, item);
        setData(temp);
    }

    public void addData(Object item) {
        List temp = this.items;
        temp.add(item);
        setData(temp);
    }

    public void addToFront(List<M> listItems) {
        List temp = this.items;
        temp.addAll(0, listItems);
        setData(temp);
    }

    public void updateData(int index, Object listItems) {
        List temp = this.items;
        temp.remove(index);
        temp.add(index, listItems);
        setData(temp);
    }

    public void addToBottom(List<M> listItems) {
        List temp = this.items;
        temp.addAll(listItems);
        setData(temp);
    }

    @Override
    public int getItemCount() {
        if(items != null) {
            return items.size();
       } else {
            return 0;
        }
    }

    public void clear() {
        if(items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public void removeData(int position){
        getData().remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}
