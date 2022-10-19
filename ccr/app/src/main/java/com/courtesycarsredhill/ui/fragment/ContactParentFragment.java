package com.courtesycarsredhill.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.FragmentContactParentBinding;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactParentFragment extends BaseFragment {

    FragmentContactParentBinding mBinding;
    private Boolean sosSuccess = false;

    public ContactParentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_contact_parent, container, false);
        setClick();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBarWithMenu(mBinding.toolbar.toolbar, AppConstants.HELP);
        mBinding.toolbar.ivSosButton.setVisibility(View.GONE);
    }

    private void setClick() {

        mBinding.tvAliNumber.setOnClickListener(view -> {
            Utils.dialdNumber(mContext, mBinding.tvAliNumber.getText().toString());
        });

        mBinding.tvOfficeNumber.setOnClickListener(view -> {
            Utils.dialdNumber(mContext, mBinding.tvOfficeNumber.getText().toString());
        });

        mBinding.tvGhajalaNumber.setOnClickListener(view -> {
            Utils.dialdNumber(mContext, mBinding.tvGhajalaNumber.getText().toString());
        });

    }
}
