package com.courtesycarsredhill.ui.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.courtesycarsredhill.ApplicationClass;
import com.courtesycarsredhill.R;
import com.courtesycarsredhill.util.GlideUtils;
import com.courtesycarsredhill.util.SessionManager;
import com.courtesycarsredhill.util.Utils;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;


public abstract class BaseActivity extends AppCompatActivity {

    public static final String BUSINESS_USER_TYPE_ID = "2";
    public static final String PERSONAL_USER_TYPE_ID = "3";

    protected boolean shouldPerformDispatchTouch = true;
    private long lastClickTime = 0;

    public Toolbar toolbar;
    TextView title;
    public SessionManager session;
    private ProgressDialog dialog;
    protected GlideUtils glideUtils;

    private Snackbar snackbar;

    private setPermissionListener permissionListener;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ApplicationClass.context = this;
        session = new SessionManager(this);
        glideUtils = new GlideUtils(ApplicationClass.getAppContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
    }

    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isParent() {
        if (session.getUserDetail().getUsertype().equals("Parents"))
            return true;
        else return false;
    }

    public void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(View view, String msg) {
        showSnackBar(view, msg, Snackbar.LENGTH_SHORT);
    }

    public void showSnackBar(View view, String msg, int LENGTH) {
        if (view == null) return;
        snackbar = Snackbar.make(view, msg, LENGTH);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(getColor(getApplicationContext(), R.color.white));
        snackbar.show();
    }

    /*public void showSnackBar(View view, String msg, int LENGTH,
                             String action, final OnSnackBarActionListener actionListener) {
        if (view == null) return;
        snackbar = Snackbar.make(view, msg, LENGTH);
        if (actionListener != null) {
            snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    actionListener.onAction();
                }
            });
        }
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(getColor(getApplicationContext(), R.color.white));
        snackbar.show();
    }*/

  /*  public void setUpToolbar(String strTitle) {
        setUpToolbarWithBackArrow(strTitle, false);
    }

    public void setUpToolbarWithBackArrow(String strTitle) {
        setUpToolbarWithBackArrow(strTitle, true);
    }
*/
  /*  private void setUpToolbarWithBackArrow(String strTitle, boolean isBackArrow) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            if (isBackArrow) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            }
        }

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        title = toolbar.findViewById(R.id.tvTitle);
        title.setText(strTitle);
    }

    public void setupToolBarWithBackArrow(Toolbar toolbar, @Nullable String Title) {
        ActionBar actionBar;

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        title = toolbar.findViewById(R.id.tvTitle);
        title.setText(Title != null ? Title : "");
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public ProgressDialog showProgressBar() {
        return showProgressBar(null);
    }

    public ProgressDialog showProgressBar(String message) {
        if (dialog == null) dialog = new ProgressDialog(this, message);
        return dialog;
    }

    public void hideProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void preventDoubleClick(View view) {
        // preventing double, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();
    }

    public int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public void showSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean ret = false;
        try {
            View view = getCurrentFocus();
            ret = super.dispatchTouchEvent(event);
            if (shouldPerformDispatchTouch) {
                if (view instanceof EditText) {
                    View w = getCurrentFocus();
                    int scrCords[] = new int[2];
                    if (w != null) {
                        w.getLocationOnScreen(scrCords);
                        float x = event.getRawX() + w.getLeft() - scrCords[0];
                        float y = event.getRawY() + w.getTop() - scrCords[1];

                        if (event.getAction() == MotionEvent.ACTION_UP
                                && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                }
            }
            return ret;
        } catch (Exception e) {
            return ret;
        }
    }

    public boolean hasAppPermissions(final String[] requestedPermissions) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
        }
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int requestCode, setPermissionListener listener) {
        this.permissionListener = listener;
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
        } else {
            if (permissionListener != null) permissionListener.onPermissionGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (permissionListener != null) permissionListener.onPermissionGranted(requestCode);
        } else {
            if (permissionListener != null) permissionListener.onPermissionDenied(requestCode);
        }
    }


    public interface setPermissionListener {
        void onPermissionGranted(int requestCode);

        void onPermissionDenied(int requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.setLocale(this, session.getDataByKey(SessionManager.KEY_LANGUAGE, "en"));
    }

    //EditText View Focus
    public void focusOnView(final NestedScrollView scroll, final View view) {
        new Handler().post(() -> {
            int vLeft = view.getTop();
            int vRight = view.getBottom();
            int sWidth = scroll.getWidth();
            scroll.smoothScrollTo(0, ((vLeft + vRight - sWidth) / 2));
        });
    }


  /*  //Set Toolbar with NEXT button and user details
    public void setAccountAppBarWithNext(AppCompatTextView userName, AppCompatTextView tvNext, AppCompatEditText etSearch,
                                         AppCompatImageView ivRight, AppCompatImageView ivSave) {

        setAccountAppBarWithNext(userName, null, tvNext, etSearch, ivRight, ivSave);
    }

    //Set Toolbar with NEXT button and user details
    *//*public void setAccountAppBarWithNext(AppCompatTextView userName, AppCompatImageView userImage, AppCompatTextView tvNext, AppCompatEditText etSearch,
                                         AppCompatImageView ivRight, AppCompatImageView ivSave) {
        //Custodian Name
        userName.setVisibility(View.VISIBLE);
        userName.setText(userData.getReferenceContact().getFullName());
        if (userImage != null) {
            //Custodian Image
            userImage.setImageResource(R.drawable.shape_circle_grey);
            userImage.setVisibility(View.VISIBLE);
        }
        //Next Text
        tvNext.setVisibility(View.VISIBLE);
        //Search Custodian
        etSearch.setVisibility(View.GONE);
        //Custodian Verified
        ivRight.setVisibility(View.VISIBLE);
        //Save Action
        ivSave.setVisibility(View.GONE);
    }

    //Set Toolbar with SAVE button and user details
    public void setAccountAppBarWithSave(AppCompatTextView userName, AppCompatImageView userImage, AppCompatTextView tvNext, AppCompatEditText etSearch,
                                         AppCompatImageView ivRight, AppCompatImageView ivSave) {
        //Custodian Name
        userName.setVisibility(View.VISIBLE);
        userName.setText(userData.getReferenceContact().getFullName());
        //Custodian Image
        userImage.setImageResource(R.drawable.shape_circle_grey);
        userImage.setVisibility(View.VISIBLE);
        //Next Text
        tvNext.setVisibility(View.GONE);
        //Search Custodian
        etSearch.setVisibility(View.GONE);
        //Custodian Verified
        ivRight.setVisibility(View.VISIBLE);
        //Save Action
        ivSave.setVisibility(View.VISIBLE);
    }*/
}
