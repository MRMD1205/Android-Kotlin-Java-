package com.courtesycarsredhill.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.adapters.EquipmentListAdapter;
import com.courtesycarsredhill.databinding.DialogEquipmentBinding;
import com.courtesycarsredhill.interfaces.OnDismissedCall;
import com.courtesycarsredhill.model.CheckListData;
import com.courtesycarsredhill.util.TimeStamp;

import java.util.ArrayList;
import java.util.Objects;


public class EquipmentDialog extends AppCompatDialogFragment {

    DialogEquipmentBinding mBinding;
    View view;
    private OnDismissedCall listener;
    ArrayList<CheckListData> vehicleCheckList;

    public EquipmentDialog(ArrayList<CheckListData> vehicleCheckList) {
        this.vehicleCheckList = vehicleCheckList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (view == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_equipment, null, false);
            view = mBinding.getRoot();
            prepareLayout();
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        setCancelable(false);
        alertBuilder.setView(view);
        AlertDialog dialog = alertBuilder.create();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 80);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(inset);

        dialog.show();
        return dialog;
    }

    private void prepareLayout() {
        for (CheckListData checkdata : vehicleCheckList //set false value manualy to list
        ) {
            checkdata.setIsChecked(false);
        }
        mBinding.layoutButtons.negativeButton.setText(getString(R.string.cancel));
        mBinding.layoutButtons.negativeButton.setVisibility(View.GONE);
        mBinding.layoutButtons.positiveButton.setText(getString(R.string.save));
        mBinding.tvMessage.setText(getContext().getString(R.string.equipment_detail) + "\n" + TimeStamp.getCurrentDay());
        mBinding.rvQuestions.setHasFixedSize(true);
        mBinding.rvQuestions.setAdapter(new EquipmentListAdapter(getContext(), vehicleCheckList));
        mBinding.layoutButtons.positiveButton.setOnClickListener(view -> {
            if (isSelectedAll()) {
                listener.onEquipmentSelected(getContext().getString(R.string.equipment_detail), vehicleCheckList);
                dismiss();

            } else {
                new MessageDialog(getContext())
                        .setMessage(getString(R.string.aler_emplty_equipment))
                        .cancelable(true)
                        .setNegativeButton(getString(android.R.string.no), (dialog, which) -> {
                            dialog.dismiss();
                        }).show();
            }

        });

        mBinding.layoutButtons.negativeButton.setOnClickListener(view -> dismiss());
    }

    private boolean isSelectedAll() {
        for (CheckListData item : vehicleCheckList) {
            if (item.getValue().equals("")) {
                return false;
            }
        }
        return true;
    }

    public void setListener(OnDismissedCall listener) {
        this.listener = listener;
    }
}
