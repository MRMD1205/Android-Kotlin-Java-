package com.courtesycarsredhill.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ItemChildHistoryBinding;
import com.courtesycarsredhill.databinding.ItemStudentListBinding;
import com.courtesycarsredhill.model.TripdetailData;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.ui.fragment.ChildShortDetailFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Utils;

import java.util.ArrayList;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class ChildListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private boolean isHistory = false;
    ArrayList<TripdetailData.Dailyroutestudentmastermodellist> studentList;

    public ChildListAdapter(Context context, boolean isHistory, ArrayList<TripdetailData.Dailyroutestudentmastermodellist> studentList) {
        this.context = context;
        this.isHistory = isHistory;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHistory) {
            ItemChildHistoryBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_child_history, parent, false);
            return new ViewHistoryHolder(mBinding);
        } else {
            ItemStudentListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_student_list, parent, false);
            return new ViewHolder(mBinding);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TripdetailData.Dailyroutestudentmastermodellist studentData = studentList.get(position);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bind(studentData);
            ((ViewHolder) holder).mBinder.layoutImgText.tvLetter.setText(studentData.getStudentName());
            ((ViewHolder) holder).mBinder.tvStudentNo.setText(studentData.getPickupOrderNo());
            if (studentData.getTriptype().equalsIgnoreCase(AppConstants.TRIP_PICKUP_TYPE) ||
                    studentData.getTriptype().equalsIgnoreCase(AppConstants.TRIP_LUNCH_IN_TYPE)) {
                ((ViewHolder) holder).mBinder.tvEstimatedTime.setText("Estimated pickup time: ");
                if (studentData.getNewPickupTime() != null && !studentData.getNewPickupTime().isEmpty()) {
                    ((ViewHolder) holder).mBinder.tvEstimatedTimeValue.setText(studentData.getNewPickupTime());
                } else {
                    ((ViewHolder) holder).mBinder.tvEstimatedTimeValue.setText("-");
                }
            } else {
                ((ViewHolder) holder).mBinder.tvEstimatedTime.setText("Estimated drop time: ");

                if (studentData.getNewDropTime() != null && !studentData.getNewDropTime().isEmpty()) {
                    ((ViewHolder) holder).mBinder.tvEstimatedTimeValue.setText(studentData.getNewDropTime());
                } else {
                    ((ViewHolder) holder).mBinder.tvEstimatedTimeValue.setText("-");
                }
            }

            if (studentData.getStudentEquipment() != null && !studentData.getStudentEquipment().isEmpty()) {
                ((ViewHolder) holder).mBinder.tvEquipmentValue.setText(studentData.getStudentEquipment());
            } else {
                ((ViewHolder) holder).mBinder.tvEquipmentValue.setText("No equipment required");
            }

            if (studentData.getIsNext()) {
                ((ViewHolder) holder).mBinder.tvStudentNo.setBackgroundResource(R.drawable.shape_circle_red);
            } else {
                ((ViewHolder) holder).mBinder.tvStudentNo.setBackgroundResource(R.drawable.shape_circle_blue);

            }
            ((ViewHolder) holder).mBinder.tvParentName.setPaintFlags(((ViewHolder) holder).mBinder.tvParentName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            ((ViewHolder) holder).mBinder.tvAddress.setPaintFlags(((ViewHolder) holder).mBinder.tvAddress.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            ((ViewHolder) holder).mBinder.tvmErgencyContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((ViewHolder) holder).mBinder.tvmErgencyContact.getVisibility() == View.VISIBLE)
                        Utils.dialdNumber(context, studentData.getEmergencycontact());
                }
            });

            ((ViewHolder) holder).mBinder.tvParent1Contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((ViewHolder) holder).mBinder.tvParent1Contact.getVisibility() == View.VISIBLE)
                        Utils.dialdNumber(context, studentData.getContactparent1());
                }
            });

            ((ViewHolder) holder).mBinder.tvParent2Contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((ViewHolder) holder).mBinder.tvParent2Contact.getVisibility() == View.VISIBLE)
                        Utils.dialdNumber(context, studentData.getContactparent2());
                }
            });
            ((ViewHolder) holder).mBinder.cvMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", studentData.getLatitude(), studentData.getLongitude(), studentData.getParentsaddress1());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        try {
                            Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            context.startActivity(unrestrictedIntent);
                        } catch (ActivityNotFoundException innerEx) {
                            Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
        if (holder instanceof ViewHistoryHolder) {
            ((ViewHistoryHolder) holder).bind(studentData);
            ((ViewHistoryHolder) holder).mBinder.layoutImgText.tvLetter.setText(studentData.getStudentName());


        }
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).mBinder.layoutImgText.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SimpleTooltip.Builder(context)
                            .anchorView(v)
                            .text(studentData.getStudentFullName())
                            .gravity(Gravity.TOP)
                            .animated(false)
                            .build()
                            .show();
                }
            });
        }
        /*holder.itemView.setOnClickListener(view -> {
            ((NavigationMainActivity) context).replaceFragment(AppConstants.NAVIGATION_KEY, ChildShortDetailFragment.newInstance(studentList.get(position), position, studentList.size() == position + 1), true, true);

        });*/
    }

    @Override
    public int getItemCount() {
        if (studentList != null)
            return studentList.size();
        else return 0;
    }

    @Override
    public void onClick(View v) {


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemStudentListBinding mBinder;

        ViewHolder(ItemStudentListBinding mBinder) {
            super(mBinder.getRoot());
            this.mBinder = mBinder;
        }

        void bind(TripdetailData.Dailyroutestudentmastermodellist item) {
            mBinder.setItem(item);
            mBinder.executePendingBindings();

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                ((NavigationMainActivity) context).replaceFragment(AppConstants.NAVIGATION_KEY, ChildShortDetailFragment.newInstance(studentList.get(position), position, studentList.size() == position + 1), true, true);
            });
        }
    }

    public class ViewHistoryHolder extends RecyclerView.ViewHolder {

        ItemChildHistoryBinding mBinder;

        ViewHistoryHolder(ItemChildHistoryBinding mBinder) {
            super(mBinder.getRoot());
            this.mBinder = mBinder;

        }

        void bind(TripdetailData.Dailyroutestudentmastermodellist item) {
            mBinder.setItem(item);
            mBinder.executePendingBindings();
        }

    }

    public void updateStudentStatus(int position, boolean isLast) {
        TripdetailData.Dailyroutestudentmastermodellist mStudent = studentList.get(position);
        mStudent.setIsNext(false);
        studentList.set(position, mStudent);
        notifyItemChanged(position);
        try {
            if (!isLast) {
                position += 1;
                mStudent = studentList.get(position);
                mStudent.setIsNext(true);
                studentList.set(position, mStudent);
                notifyItemChanged(position);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}