package com.courtesycarsredhill.ui.fragment;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.adapters.ContactListAdapter;
import com.courtesycarsredhill.adapters.TripListAdapter;
import com.courtesycarsredhill.databinding.FragmentContactBinding;
import com.courtesycarsredhill.model.ContactListModel;
import com.courtesycarsredhill.model.TripListData;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.util.Utils;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment {

    FragmentContactBinding mBinding;
    ContactListAdapter mAdapter;
    ArrayList<ContactListModel> contactList = new ArrayList<>();
    private Boolean sosSuccess = false;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_contact, container, false);
        mBinding.toolbar.ivSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOSDialog();
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBarWithMenu(mBinding.toolbar.toolbar, AppConstants.HELP);
        callApi();
    }

    private void callApi() {

        HashMap<String, String> params = new HashMap<>();
        params.put("","");

        try {
            Retrofit.with(mContext)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setGetParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_GET_ACTIVE_CONTACT_LIST, session))
                    .setHeaderCallBackListener(new JSONCallback(mContext, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {

                                    Gson gson = new Gson();
                                    contactList = gson.fromJson(jsonObject.optJSONArray("Data").toString(), new TypeToken<List<ContactListModel>>() {
                                    }.getType());

                                    Logger.e(contactList.toString());
                                    if (contactList != null && contactList.size() > 0) {
                                        mBinding.layoutContactList.recyclerView.setVisibility(View.VISIBLE);
                                        mBinding.layoutContactList.txtNoContacts.setVisibility(View.GONE);
                                        setAdapter(contactList);
                                    } else {
                                        mBinding.layoutContactList.recyclerView.setVisibility(View.GONE);
                                        mBinding.layoutContactList.txtNoContacts.setVisibility(View.VISIBLE);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showShortToast(jsonObject.optString("Message"));
                            }
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            hideProgressBar();
                            showShortToast(message);
                        }
                    });
        } catch (Exception e) {
            hideProgressBar();

        }
    }

    private void setAdapter(ArrayList<ContactListModel> contactList) {
        mAdapter = new ContactListAdapter(mContext,  contactList);
        mBinding.layoutContactList.recyclerView.setAdapter(mAdapter);
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
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    sosSuccess = true;
                                    Log.e("TAG", "onActive Swipe Button: " + getString(R.string.notification_sent));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showShortToast(jsonObject.optString(getString(R.string.message)));
                            }
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            hideProgressBar();
                            showShortToast(message);
                        }
                    });
        } catch (Exception e) {
            hideProgressBar();

        }
    }

    private void openSOSDialog() {
        Dialog dialog = new Dialog(mActivity, R.style.DialogWithAnimation);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_sos_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        SwipeButton swipeBtn = dialog.findViewById(R.id.swipe_btn);

        swipeBtn.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                callSOSApi();
                swipeBtn.setEnabled(false);
            }
        });

        swipeBtn.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                TextView tvSOSTitle = dialog.findViewById(R.id.tvSosTitle);
                if (sosSuccess) {
                    swipeBtn.setHasActivationState(true);
                    tvSOSTitle.setText(getString(R.string.notification_sent));
                } else {
                    swipeBtn.setHasActivationState(false);
                    tvSOSTitle.setText(getString(R.string.notification_sent));
                }
            }
        });

        dialog.show();
    }

}
