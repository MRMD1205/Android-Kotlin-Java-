package com.courtesycarsredhill.ui.signin;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.adapters.ContactListAdapter;
import com.courtesycarsredhill.databinding.ActivityForgotPasswordBinding;
import com.courtesycarsredhill.model.ContactListModel;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.util.Utils;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForgotPasswordActivity extends BaseActivity {
    ActivityForgotPasswordBinding mBinding;
    ContactListAdapter mAdapter;
    ArrayList<ContactListModel> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        setClick();
        callApi();
    }

    private void callApi() {

        HashMap<String, String> params = new HashMap<>();
        params.put("","");

        try {
            Retrofit.with(this)
                    .setGetParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_GET_ACTIVE_CONTACT_LIST, session))
                    .setHeaderCallBackListener(new JSONCallback(this, showProgressBar()) {
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
                                        mBinding.cvApiList.setVisibility(View.VISIBLE);
                                        mBinding.cvStaticContactList.setVisibility(View.GONE);
                                        setAdapter(contactList);
                                    } else {
                                        mBinding.cvApiList.setVisibility(View.GONE);
                                        mBinding.cvStaticContactList.setVisibility(View.VISIBLE);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mBinding.cvApiList.setVisibility(View.GONE);
                                mBinding.cvStaticContactList.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            hideProgressBar();
                            Log.e("TAG", "onFailed: "+message );
                            mBinding.cvApiList.setVisibility(View.GONE);
                            mBinding.cvStaticContactList.setVisibility(View.VISIBLE);
                        }
                    });
        } catch (Exception e) {
            hideProgressBar();
            mBinding.cvApiList.setVisibility(View.GONE);
            mBinding.cvStaticContactList.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(ArrayList<ContactListModel> contactList) {
        mAdapter = new ContactListAdapter(this,  contactList);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void setClick() {

        mBinding.ivBack.setOnClickListener(view -> onBackPressed());
        mBinding.tvAliNumber.setOnClickListener(view -> {
            Utils.dialdNumber(this, mBinding.tvAliNumber.getText().toString());
        });

        mBinding.tvAsterTrainingNumber.setOnClickListener(view -> {
            Utils.dialdNumber(this, mBinding.tvAsterTrainingNumber.getText().toString());
        });

        mBinding.tvOfficeNumber.setOnClickListener(view -> {
            Utils.dialdNumber(this,mBinding.tvOfficeNumber.getText().toString());
        });

        mBinding.tvDocumentNumber.setOnClickListener(view -> {
            Utils.dialdNumber(this,mBinding.tvDocumentNumber.getText().toString());
        });

        mBinding.tvGhajalaNumber.setOnClickListener(view -> {
            Utils.dialdNumber(this,mBinding.tvGhajalaNumber.getText().toString());
        });

        mBinding.tvWorkshopNumber.setOnClickListener(view -> {
            Utils.dialdNumber(this,mBinding.tvWorkshopNumber.getText().toString());
        });
    }
}




