package com.courtesycarsredhill.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.DialogAbsentReasonBinding;
import com.courtesycarsredhill.databinding.DialogFilterBinding;
import com.courtesycarsredhill.interfaces.OnDismissedCall;
import com.courtesycarsredhill.util.TimeStamp;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;


public class FilterDialog extends AppCompatDialogFragment {

    DialogFilterBinding mBinding;
    View view;
    private OnDismissedCall listener;
    private ArrayList<String> yearList = new ArrayList<>();
    private String[] monthList ;

    int year, month;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (view == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter, null, false);
            view = mBinding.getRoot();
            prepareLayout();
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        alertBuilder.setView(view);
        AlertDialog dialog = alertBuilder.create();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 80);
        dialog.getWindow().setBackgroundDrawable(inset);

        dialog.show();

        return dialog;
    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(getContext(), null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }

    private void prepareLayout() {
        mBinding.layoutButtons.negativeButton.setText(getString(R.string.reset));
        mBinding.layoutButtons.positiveButton.setText(getString(R.string.apply));

        //Year
        for (int i = TimeStamp.getCurrentYear(); i > 2016; i--) {
            yearList.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), R.layout.layout_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        mBinding.spinnerYear.setAdapter(yearAdapter);
        mBinding.spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = Integer.parseInt(String.valueOf(parent.getItemAtPosition(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year = TimeStamp.getCurrentYear();
            }
        });

        //Month
        monthList=getContext().getResources().getStringArray(R.array.monthFullName);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), R.layout.layout_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        mBinding.spinnerMonth.setAdapter(monthAdapter);

        mBinding.spinnerMonth.setSelection(monthAdapter.getPosition(monthList[TimeStamp.getCurrentMonth()]));

        mBinding.spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position+1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                month = TimeStamp.getCurrentMonth() + 1;
            }
        });


        mBinding.layoutButtons.positiveButton.setOnClickListener(view -> {
            listener.onDismissCalled(month,year);
            dismiss();
        });

        mBinding.layoutButtons.negativeButton.setOnClickListener(view ->

        {
            listener.onDismissCalled(TimeStamp.getCurrentMonth() + 1,TimeStamp.getCurrentYear());
            dismiss();
        });
    }

    public void setListener(OnDismissedCall listener) {
        this.listener = listener;
    }
}
