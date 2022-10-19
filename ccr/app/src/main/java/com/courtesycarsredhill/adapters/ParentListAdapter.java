package com.courtesycarsredhill.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ItemParentInfoBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ParentListAdapter extends RecyclerView.Adapter<ParentListAdapter.ViewHolder> {
    private Context context;

    public ParentListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemParentInfoBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_parent_info, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemParentInfoBinding mBinder;

        ViewHolder(ItemParentInfoBinding mBinder) {
            super(mBinder.getRoot());
            this.mBinder = mBinder;

        }
    }
}