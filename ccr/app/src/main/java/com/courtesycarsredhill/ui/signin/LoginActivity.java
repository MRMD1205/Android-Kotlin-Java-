package com.courtesycarsredhill.ui.signin;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ActivityLoginBinding;
import com.courtesycarsredhill.model.LoginData;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.tvCallOffice.setPaintFlags(mBinding.tvCallOffice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setClicks();
    }

    private void setClicks() {
        mBinding.tvCallOffice.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
        mBinding.btSave.setOnClickListener(view -> {
            validate();
        });
    }

    private void validate() {
        try {
            if (mBinding.etUserName.getText().toString().isEmpty()) {
                showSnackBar(mBinding.etUserName, getString(R.string.validate_username));
                mBinding.etUserName.requestFocus();
            }else if (mBinding.etPin.getText().toString().isEmpty()) {
                showSnackBar(mBinding.etPin, getString(R.string.validate_pin));
                mBinding.etPin.requestFocus();
            } else if (mBinding.etPin.getText().toString().trim().length() < 4) {
                showSnackBar(mBinding.etPin, getString(R.string.you_must_enter_4_digit_pin));
                mBinding.etPin.requestFocus();
            } else {
                // Get token
                callApi(mBinding.etUserName.getText().toString(),mBinding.etPin.getText().toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callApi(String UserName, String password) {

        HashMap<String, String> params = new HashMap<>();
        params.put("UserName", UserName);
        params.put("Password", password);
        params.put("DeviceType", "android");
        params.put("DeviceId", "");

        try {
            Retrofit.with(this)
                    .setParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_LOGIN, session))
                    .setCallBackListener(new JSONCallback(this, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {

                                    Gson gson = new Gson();
                                    final LoginData userInfo = gson.fromJson(jsonObject.optJSONObject("Data").toString(),
                                            new TypeToken<LoginData>() {
                                            }.getType());

                                    Logger.e(userInfo.toString());
                                    showShortToast(getString(R.string.login_successful));

                                    session.storeUserDetail(userInfo);

                                    Intent intent = new Intent(LoginActivity.this, NavigationMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

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

}
