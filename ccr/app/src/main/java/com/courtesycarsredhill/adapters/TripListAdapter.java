package com.courtesycarsredhill.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ItemTripListBinding;
import com.courtesycarsredhill.interfaces.OnItemSelected;
import com.courtesycarsredhill.model.TripListData;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.ui.base.BaseBinder;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.util.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder>  {
    private Context context;
    OnItemSelected<TripListData> listner;
    ArrayList<TripListData> tripListDataList;
    private boolean isHistory = false;
    private ItemClickListener clickListener;

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public TripListAdapter(Context context, OnItemSelected<TripListData> listner, ArrayList<TripListData> tripListDataList, boolean isHistory,ItemClickListener clickListener) {
        this.context = context;
        this.listner = listner;
        this.tripListDataList = tripListDataList;
        this.isHistory = isHistory;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_trip_list, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripListData item = tripListDataList.get(position);
        Logger.e(item.getTriptype());
        if (item != null) {
            holder.bind(item);
            if (((BaseActivity) context).isParent()) {
                holder.mBinder.layoutSourceTime.tvTime.setText(item.getNewPickupTime());
            } else {
                holder.mBinder.layoutSourceTime.tvTime.setText(item.getArrivaltime());
            }
            holder.mBinder.layoutDestinationTime.tvTime.setText(item.getLeavetime());
            if (((BaseActivity) context).isParent()) {
                BaseBinder.setImageUrl(holder.mBinder.layoutImgText.ivUserImage, item.getDriverimg());
                holder.mBinder.tvSchoolName.setText(item.getDrivername());
                holder.mBinder.tvSchoolAddress.setText(item.getSchooladdress());
                holder.mBinder.ivCall.setVisibility(View.VISIBLE);
                holder.mBinder.layoutImgText.ivUserImage.setVisibility(View.VISIBLE);
                holder.mBinder.layoutImgText.tvLetter.setVisibility(View.INVISIBLE);
                holder.mBinder.tvChildren.setText(context.getString(R.string.str_call_driver));
                holder.mBinder.tvChildrenCount.setVisibility(View.INVISIBLE);
            } else {
                holder.mBinder.layoutImgText.tvLetter.setText(item.getSchoolname().substring(0, 2));
                holder.mBinder.tvSchoolName.setText(item.getSchoolname());
                holder.mBinder.tvSchoolAddress.setText(item.getSchooladdress());
                holder.mBinder.ivCall.setVisibility(View.INVISIBLE);
                holder.mBinder.layoutImgText.ivUserImage.setVisibility(View.INVISIBLE);
                holder.mBinder.layoutImgText.tvLetter.setVisibility(View.VISIBLE);
                holder.mBinder.tvChildren.setText(context.getString(R.string.str_children));
                holder.mBinder.tvChildrenCount.setVisibility(View.VISIBLE);
            }
        }
        holder.mBinder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mBinder.ivCall.getVisibility() == View.VISIBLE)
                    Utils.dialdNumber(context, item.getDrivercontactno());
            }
        });
        if (item.getTripstatus().equals(AppConstants.TRIP_READY_TO_GO)) {
            holder.mBinder.layoutTripHeading.getRoot().setBackgroundColor(context.getResources().getColor(R.color.yellow_ec));
            holder.mBinder.layoutTripHeading.tvTripCode.setTextColor(context.getResources().getColor(R.color.grey_text));
            holder.mBinder.layoutTripHeading.tvTripCodeValue.setTextColor(context.getResources().getColor(R.color.black));
            holder.mBinder.layoutTripHeading.tvTripStatus.setTextColor(context.getResources().getColor(R.color.black));
            holder.mBinder.layoutTripHeading.tvTripType.setTextColor(context.getResources().getColor(R.color.black));

        } else if (item.getTripstatus().equals(AppConstants.TRIP_ON_GOING)) {
            holder.mBinder.layoutTripHeading.getRoot().setBackgroundColor(context.getResources().getColor(R.color.sky));
            holder.mBinder.layoutTripHeading.tvTripCode.setTextColor(context.getResources().getColor(R.color.white));
            holder.mBinder.layoutTripHeading.tvTripCodeValue.setTextColor(context.getResources().getColor(R.color.white));
            holder.mBinder.layoutTripHeading.tvTripStatus.setTextColor(context.getResources().getColor(R.color.white));
            holder.mBinder.layoutTripHeading.tvTripType.setTextColor(context.getResources().getColor(R.color.white));
        } else if (item.getTripstatus().equals(AppConstants.TRIP_COMPLETED)) {
            holder.mBinder.layoutTripHeading.getRoot().setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.mBinder.layoutTripHeading.tvTripCode.setTextColor(context.getResources().getColor(R.color.white));
            holder.mBinder.layoutTripHeading.tvTripCodeValue.setTextColor(context.getResources().getColor(R.color.white));
            holder.mBinder.layoutTripHeading.tvTripStatus.setTextColor(context.getResources().getColor(R.color.white));
            holder.mBinder.layoutTripHeading.tvTripType.setTextColor(context.getResources().getColor(R.color.white));
            if (isHistory) {
                holder.mBinder.tvEaring.setVisibility(View.VISIBLE);
                holder.mBinder.tvEarningCount.setVisibility(View.VISIBLE);
            } else {
                holder.mBinder.tvEaring.setVisibility(View.INVISIBLE);
                holder.mBinder.tvEarningCount.setVisibility(View.INVISIBLE);
            }

        }

        if (item.getTriptype().equals("AM")||item.getTriptype().equals("LT-IN")) {
            holder.mBinder.tvSourceAddress.setText(item.getSourceAddress());
            holder.mBinder.tvDestinationAddress.setText(item.getSchoolname());
        } else if (item.getTriptype().equals("PM")||item.getTriptype().equals("LT-OUT")) {
            holder.mBinder.tvSourceAddress.setText(item.getSchoolname());
            holder.mBinder.tvDestinationAddress.setText(item.getDestinationAddress());
        }
        holder.itemView.setOnClickListener(view -> {
            listner.onItemSelected(item);
        });
    }

    @Override
    public int getItemCount() {
        if (tripListDataList != null)
            return tripListDataList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        ItemTripListBinding mBinder;

        ViewHolder(ItemTripListBinding mBinder) {
            super(mBinder.getRoot());
            this.mBinder = mBinder;
        }

        void bind(TripListData item) {
            mBinder.setItem(item);
            mBinder.layoutTripHeading.setItem(item);
            mBinder.layoutSourceTime.setItem(item);
            mBinder.layoutDestinationTime.setItem(item);
            mBinder.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
                }
    }
}
