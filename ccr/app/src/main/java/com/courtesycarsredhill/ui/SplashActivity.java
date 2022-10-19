package com.courtesycarsredhill.ui;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ActivitySplashBinding;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.ui.signin.LoginActivity;

public class SplashActivity extends BaseActivity {
    ActivitySplashBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        runSplashActivity();


    }

    private void runSplashActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.isLoggedIn()) {
                    startActivity(NavigationMainActivity.class);
                } else
                    startActivity(LoginActivity.class);
            }
        }, 2000);
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(SplashActivity.this, cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
