package com.courtesycarsredhill.ui.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.courtesycarsredhill.ApplicationClass;
import com.courtesycarsredhill.R;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.util.GlideUtils;
import com.courtesycarsredhill.util.SessionManager;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {

    public Context mContext;
    public Activity mActivity;

    public TextView title;
    public SessionManager session;
    private Snackbar snackbar;

    protected GlideUtils glideUtils;

    private ProgressDialog dialog;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
        session = new SessionManager(mContext);
        glideUtils = new GlideUtils(ApplicationClass.getAppContext());
    }

    public void setupToolBar(Toolbar toolbar, @Nullable String Title) {
        ActionBar actionBar;

        ((BaseActivity) mActivity).setSupportActionBar(toolbar);
        actionBar = ((BaseActivity) mActivity).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        title = toolbar.findViewById(R.id.tvTitle);
        title.setText(Title != null ? Title : "");
    }


    public void setupToolBarWithMenu(Toolbar toolbar) {
        setupToolBarWithMenu(toolbar, null);
    }

    public void setupToolBarWithMenu(Toolbar toolbar, @Nullable String Title) {
        ActionBar actionBar;

        ((BaseActivity) mActivity).setSupportActionBar(toolbar);
        actionBar = ((BaseActivity) mActivity).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        toolbar.setNavigationOnClickListener(view -> {
            ((NavigationMainActivity) mActivity).mBinding.drawer.openDrawer(GravityCompat.START);

        });
        title = toolbar.findViewById(R.id.tvTitle);
        title.setText(Title != null ? Title : "");
    }
    public void setupToolBarWithBackArrow(Toolbar toolbar) {
        setupToolBarWithBackArrow(toolbar, null);
    }

    public void setupToolBarWithBackArrow(Toolbar toolbar, @Nullable String Title) {
        ActionBar actionBar;

        ((BaseActivity) mActivity).setSupportActionBar(toolbar);
        actionBar = ((BaseActivity) mActivity).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        toolbar.setNavigationOnClickListener(view -> mActivity.onBackPressed());
        title = toolbar.findViewById(R.id.tvTitle);
        title.setText(Title != null ? Title : "");
    }
    public void updateToolBarTitle(@Nullable String Title) {
        title.setText(Title != null ? Title : mContext.getResources().getString(R.string.app_name));
    }

    public boolean onBackPressed() {
        return false;
    }

    public void showShortToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(View view, String msg) {
        showSnackBar(view, msg, Snackbar.LENGTH_SHORT);
    }

    public void showSnackBar(View view, String msg, int LENGTH) {
        if (view == null) return;
        snackbar = Snackbar.make(view, msg, LENGTH);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        snackbar.show();
    }

    public void showSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            if (mActivity.getWindow().getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(mActivity.getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProgressDialog showProgressBar() {
        return showProgressBar(null);
    }

    public ProgressDialog showProgressBar(String message) {
        if (dialog == null) dialog = new ProgressDialog(mContext, message);
        return dialog;
    }

    public void hideProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
    }
}