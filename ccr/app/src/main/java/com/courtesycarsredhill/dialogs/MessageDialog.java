package com.courtesycarsredhill.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.DialogMessageBinding;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;


public class MessageDialog extends AlertDialog implements View.OnClickListener {
    private boolean cancelable = true, onlyPositiveButton = false;
    private String title, message, positiveButtonText, negativeButtonText;
    private OnClickListener onPositiveButtonClick, onNegativeButtonClick;

    public MessageDialog(Context context) {
        super(context, R.style.DialogWithAnimation);
    }

    public MessageDialog(Context context, boolean onlyPositiveButton) {
        super(context, R.style.DialogWithAnimation);
        this.onlyPositiveButton = onlyPositiveButton;
    }

    public MessageDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MessageDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public MessageDialog cancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public MessageDialog setPositiveButton(String text, OnClickListener listener) {
        this.positiveButtonText = text;
        this.onPositiveButtonClick = listener;
        return this;
    }

    public MessageDialog setNegativeButton(String text, OnClickListener listener) {
        this.negativeButtonText = text;
        this.onNegativeButtonClick = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogMessageBinding mBinder = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_message, null, false);
        setContentView(mBinder.getRoot());
        setCanceledOnTouchOutside(cancelable);
        setCancelable(cancelable);

        mBinder.tvTitle.setVisibility(title != null ? View.VISIBLE : View.GONE);
        if (title != null) mBinder.tvTitle.setText(title);

        mBinder.tvMessage.setVisibility(message != null ? View.VISIBLE : View.GONE);
        if (message != null) mBinder.tvMessage.setText(message);

        if (positiveButtonText != null) mBinder.layoutButtons.positiveButton.setText(positiveButtonText);
        if (negativeButtonText != null) mBinder.layoutButtons.negativeButton.setText(negativeButtonText);

        mBinder.layoutButtons.positiveButton.setVisibility(onPositiveButtonClick != null ? View.VISIBLE : View.GONE);
        mBinder.layoutButtons.negativeButton.setVisibility(!onlyPositiveButton && onNegativeButtonClick != null ? View.VISIBLE : View.GONE);

        mBinder.layoutButtons.positiveButton.setOnClickListener(this);
        mBinder.layoutButtons.negativeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.positiveButton:
                if (onPositiveButtonClick != null)
                    onPositiveButtonClick.onClick(MessageDialog.this, 0);
                break;
            case R.id.negativeButton:
                if (onNegativeButtonClick != null)
                    onNegativeButtonClick.onClick(MessageDialog.this, 0);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}