package com.example.materialconcepts.dialog;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.materialconcepts.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        com.example.materialconcepts.databinding.CustomBottomDialogBinding customBottomDialogBinding = DataBindingUtil.inflate(layoutInflater, R.layout.custom_bottom_dialog, container, false);

        customBottomDialogBinding.btnBottomSheetFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClick(R.drawable.car);
            }
        });

        customBottomDialogBinding.btnBottomSheetSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClick(R.drawable.car2);
            }
        });

        customBottomDialogBinding.btnBottomSheetThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClick(R.drawable.car3);
            }
        });

        return customBottomDialogBinding.getRoot();
    }

    public interface BottomSheetListener {
        void onButtonClick(int i);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (BottomSheetListener) context;
    }
}
