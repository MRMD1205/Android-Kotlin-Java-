package com.courtesycarsredhill.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.DialogFeedbackFormBinding;
import com.courtesycarsredhill.interfaces.OnDismissedCall;
import com.courtesycarsredhill.util.AppConstants;


public class FeedbackDialog extends AppCompatDialogFragment {

    DialogFeedbackFormBinding mBinding;
    View view;
    private OnDismissedCall listener;
    int selectedId = -1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (view == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_feedback_form, null, false);
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

    private void prepareLayout() {

        mBinding.layoutButtons.negativeButton.setText(getString(R.string.cancel));
        mBinding.layoutButtons.negativeButton.setVisibility(View.GONE);
        mBinding.layoutButtons.positiveButton.setText(getString(R.string.save));

        enableDisableButton();

        mBinding.layoutButtons.positiveButton.setOnClickListener(view -> {

            getSelectedId();
            hideSoftKeyboard();
            listener.onFeedbackGiven(selectedId, mBinding.etMessage.getText().toString().trim());
            dismiss();
        });

        mBinding.layoutButtons.negativeButton.setOnClickListener(view -> dismiss());
        mBinding.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableDisableButton();
            }
        });

        mBinding.radioSmiley.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (mBinding.radioSmiley.getCheckedRadioButtonId()) {
                    case R.id.rdHappy:
                        selectedId = AppConstants.HAPPYID;
                        break;
                    case R.id.rdSmile:
                        selectedId = AppConstants.SMILEID;
                        break;
                    case R.id.rdSad:
                        selectedId = AppConstants.SADID;
                        break;
                }
                enableDisableButton();
            }
        });
    }

    private void enableDisableButton() {

        boolean isEnableButton = false;
        switch (selectedId) {
            case AppConstants.HAPPYID:
                isEnableButton = true;
                break;
            case AppConstants.SMILEID:
                isEnableButton = true;
                break;
            case AppConstants.SADID:
                if (mBinding.etMessage.getText().toString().trim().length() > 0)
                    isEnableButton = true;
                else isEnableButton = false;
                break;
        }
        mBinding.layoutButtons.positiveButton.setEnabled(isEnableButton);
        mBinding.layoutButtons.positiveButton.setClickable(isEnableButton);
        mBinding.layoutButtons.positiveButton.setBackgroundResource(isEnableButton ? R.drawable.button_bg : R.drawable.button_bg_disable);
    }

    private void getSelectedId() {
        switch (mBinding.radioSmiley.getCheckedRadioButtonId()) {
            case R.id.rdHappy:
                selectedId = AppConstants.HAPPYID;
                break;
            case R.id.rdSmile:
                selectedId = AppConstants.SMILEID;
                break;
            case R.id.rdSad:
                selectedId = AppConstants.SADID;
                break;
        }
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener(OnDismissedCall listener) {
        this.listener = listener;
    }
}
