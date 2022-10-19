package com.courtesycarsredhill.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ItemDrawerRowBinding;
import com.courtesycarsredhill.interfaces.OnItemSelected;
import com.courtesycarsredhill.model.DrawerItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class DrawerItemAdapter extends RecyclerView.Adapter {

    private List<DrawerItem> mData;
    private int selectItem;
    private Context mContext;
    private OnItemSelected<String> listener;

    public DrawerItemAdapter(Context context, ArrayList<DrawerItem> list, OnItemSelected<String> listener, int pos) {
        this.mData = list;
        this.selectItem = pos;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDrawerRowBinding mBinder = DataBindingUtil.inflate(LayoutInflater
                .from(parent.getContext()), R.layout.item_drawer_row, parent, false);
        return new MyViewHolder(mBinder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).mBinder.ivIcon.setImageResource(mData.get(position).icon);
        ((MyViewHolder) holder).mBinder.tvName.setText(mData.get(position).name);
        if (selectItem == position) {
            ((MyViewHolder) holder).mBinder.ivIcon.setImageResource(mData.get(position).iconSelected);
            ((MyViewHolder) holder).mBinder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        } else {
            ((MyViewHolder) holder).mBinder.ivIcon.setImageResource(mData.get(position).icon);
            ((MyViewHolder) holder).mBinder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

        }
        holder.itemView.setOnClickListener(view -> {
            if (!((MyViewHolder) holder).mBinder.tvName.getText().toString().isEmpty())
                listener.onItemSelected(((MyViewHolder) holder).mBinder.tvName.getText().toString());
            setPosition(position);

        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ItemDrawerRowBinding mBinder;

        MyViewHolder(ItemDrawerRowBinding mBinder) {
            super(mBinder.getRoot());
            this.mBinder = mBinder;


        }
    }

    public void setPosition(int position) {
        selectItem = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
