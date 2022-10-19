package com.courtesycarsredhill.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import com.courtesycarsredhill.ui.NavigationMainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class DatePicker extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar mCalender = Calendar.getInstance();
        int year = mCalender.get(Calendar.YEAR);
        int month = mCalender.get(Calendar.MONTH);
        int dayOfMonth = mCalender.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(((NavigationMainActivity) getActivity()), (DatePickerDialog.OnDateSetListener) (NavigationMainActivity) getActivity(), year, month, dayOfMonth);
    }
}