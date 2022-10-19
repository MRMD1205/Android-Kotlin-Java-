package com.courtesycarsredhill.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.ui.base.BaseFragment;

public class OurEquipmentFragment extends BaseFragment {

    public OurEquipmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_our_equipment, container, false);
    }
}