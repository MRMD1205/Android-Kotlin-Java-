package com.courtesycarsredhill.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.DialogAbsentReasonBinding;
import com.courtesycarsredhill.interfaces.OnItemSelected;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;


public class AddAbsentDialog extends AppCompatDialogFragment {

    DialogAbsentReasonBinding mBinding;
    View view;
    private OnItemSelected listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (view == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_absent_reason, null, false);
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
        mBinding.layoutButtons.positiveButton.setText(getString(R.string.submit));

        mBinding.layoutButtons.positiveButton.setOnClickListener(view -> {
            if(!mBinding.etMessage.getText().toString().trim().isEmpty()){
                listener.onItemSelected(mBinding.etMessage.getText().toString().trim());
                dismiss();
            }else {
                Toast.makeText(getContext(), getString(R.string.absent_alert), Toast.LENGTH_SHORT).show();
            }

        });

        mBinding.layoutButtons.negativeButton.setOnClickListener(view -> dismiss());
    }

    public void setListener(OnItemSelected listener) {
        this.listener = listener;
    }
}
