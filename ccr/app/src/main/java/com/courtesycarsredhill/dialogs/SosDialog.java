package com.courtesycarsredhill.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.DialogFeedbackFormBinding;
import com.courtesycarsredhill.databinding.LayoutSosDialogBinding;
import com.courtesycarsredhill.interfaces.OnDismissedCall;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.util.SessionManager;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;

public class SosDialog  extends AppCompatDialogFragment {

    LayoutSosDialogBinding mBinding;
    View view;
    private OnDismissedCall listener;
    public SessionManager session;
    Boolean sosSuccess = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (view == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_sos_dialog, null, false);
            view = mBinding.getRoot();
            prepareLayout();
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        alertBuilder.setView(view);
        AlertDialog dialog = alertBuilder.create();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 80);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    private void prepareLayout() {

        mBinding.swipeBtn.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                callSOSApi();
                mBinding.swipeBtn.setEnabled(false);
            }
        });

        mBinding.swipeBtn.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {

                if (sosSuccess) {
                    mBinding.swipeBtn.setHasActivationState(true);
                    mBinding.tvSosTitle.setText(getString(R.string.notification_sent));
                } else {
                    mBinding.swipeBtn.setHasActivationState(false);
                    mBinding.tvSosTitle.setText(getString(R.string.notification_sent));
                }
            }
        });

    }

    private void callSOSApi() {

        try {
            Retrofit.with(getContext())
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setParameters(new JSONObject())
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_SOS_REMINDER + "?DriverID=" + session.getUserDetail().getUserid(), session))
                    .setHeaderCallBackListener(new JSONCallback(getContext()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            ((BaseActivity) getContext()).hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    sosSuccess = true;
                                    Log.e("TAG", "onActive Swipe Button: " + getString(R.string.notification_sent));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ((BaseActivity) getContext()).showShortToast(jsonObject.optString(getString(R.string.message)));
                            }
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            ((BaseActivity) getContext()).hideProgressBar();
                            ((BaseActivity) getContext()).showShortToast(message);
                        }
                    });
        } catch (Exception e) {
            ((BaseActivity) getContext()).hideProgressBar();

        }
    }

    public void setListener(OnDismissedCall listener) {
        this.listener = listener;
    }
}
