package com.courtesycarsredhill.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ItemEquipmentQuestionBinding;
import com.courtesycarsredhill.model.CheckListData;

import java.util.ArrayList;

public class EquipmentListAdapter extends RecyclerView.Adapter<EquipmentListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CheckListData> vehicleCheckList;

    public EquipmentListAdapter(Context context, ArrayList<CheckListData> vehicleCheckList) {
        this.context = context;
        this.vehicleCheckList = vehicleCheckList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEquipmentQuestionBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_equipment_question, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckListData checkData = vehicleCheckList.get(position);
        if (checkData != null) {
            holder.bind(checkData);
            switch (checkData.getValue()) {
                case "Good":
                        holder.mBinder.tvGood.setSelected(true);
                        holder.mBinder.tvIssue.setSelected(false);
                    break;
                case "Bad":
                    holder.mBinder.tvGood.setSelected(false);
                    holder.mBinder.tvIssue.setSelected(true);
                    break;
                default:
                case "":
                    holder.mBinder.tvGood.setSelected(false);
                    holder.mBinder.tvIssue.setSelected(false);
                    break;
            }
        }
        holder.mBinder.tvGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleCheckList.get(position).setIsChecked(true);
                vehicleCheckList.get(position).setValue(context.getResources().getString(R.string.good));
                notifyItemChanged(position);
            }
        });
        holder.mBinder.tvIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleCheckList.get(position).setIsChecked(false);
                vehicleCheckList.get(position).setValue(context.getResources().getString(R.string.bad));
                notifyItemChanged(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (vehicleCheckList != null)
            return vehicleCheckList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemEquipmentQuestionBinding mBinder;

        ViewHolder(ItemEquipmentQuestionBinding mBinder) {
            super(mBinder.getRoot());
            this.mBinder = mBinder;
        }

        void bind(CheckListData item) {
            mBinder.setItem(item);
            mBinder.executePendingBindings();
        }
    }
}
