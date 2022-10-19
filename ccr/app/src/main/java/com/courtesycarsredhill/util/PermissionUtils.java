package com.courtesycarsredhill.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PermissionUtils {

    public static final String READ_EXTERNAL = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public static final String ACCESS_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String WRITE_SETTINGS = Manifest.permission.WRITE_SETTINGS;


    public static final int REQUEST_CODE_READ_EXTERNAL = 101;
    public static final int WRITE_CODE_READ_EXTERNAL = 102;
    public static final int CAMERA_CODE = 103;
    public static final int PHONE_STATE_CODE = 104;
    public static final int RECORD_AUDIO_CODE = 105;
    public static final int MULTIPLE = 106;
    public static final int WRITE_CALENDAR_CODE = 107;
    public static final int ACCESS_LOCATION_CODE = 108;
    public static final int WRITE_SETTINGS_CODE = 109;

    public static final String[] locationPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    public static boolean checkPermission(Context mContext, String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void isCheck(Context mContext, String[] permission, int requestCode, permissionListener mListener) {
        if (hasPermissions(mContext, permission)) {
            if (mListener != null)
                mListener.onAllow(requestCode);
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, permission, requestCode);
        }
    }

    public static void isCheck(Context mContext, String permission, int requestCode, permissionListener mListener) {
        if (checkPermission(mContext, permission)) {
            if (mListener != null)
                mListener.onAllow(requestCode);
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, requestCode);
        }
    }

    public static void onRequestPermissionsResult(Context me, int requestCode, String[] permissions, int[] grantResults, permissionListener permissionListener) {
        boolean valid = true, showRationale = true, deny = true;

        for (int i = 0, len = grantResults.length; i < len; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                valid = valid && grantResults[i] == PackageManager.PERMISSION_GRANTED;
            } else {

                valid = valid && grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                boolean show = ActivityCompat.shouldShowRequestPermissionRationale((Activity) me, permissions[i]);
                if (show) {
                    // deny = deny && grantResults[i] == PackageManager.PERMISSION_DENIED;
                    showRationale = showRationale && !(grantResults[i] == PackageManager.PERMISSION_DENIED);
                } else {
                    // user also CHECKED "never ask again"
                    // you can either enable some fall back,
                    // disable features of your app
                    // or open another dialog explaining
                    // again the permission and directing to
                    // the app setting
                    //showRationale = show && grantResults[i] == PackageManager.PERMISSION_DENIED;

                    showRationale = showRationale && grantResults[i] == PackageManager.PERMISSION_DENIED;
                }
            }
        }

        if (valid)
            permissionListener.onAllow(requestCode);
        else if (showRationale)
            permissionListener.onDenyNeverAskAgain(requestCode);
        else
            permissionListener.onDeny(requestCode);

       /* for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                 boolean show = ActivityCompat.shouldShowRequestPermissionRationale((Activity) me,  permission);
                if (show) {
                    // user also CHECKED "never ask again"
                    // you can either enable some fall back,
                    // disable features of your app
                    // or open another dialog explaining
                    // again the permission and directing to
                    // the app setting

                    showRationale = showRationale && show;
                }
            }else
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
            {
                valid = valid && grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }



       *//* if (grantResults.length > 0) {
            boolean valid = true;
            for (int grantResult : grantResults) {
                valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(grantResults.);
                if (!showRationale) {
                    // user also CHECKED "never ask again"
                    // you can either enable some fall back,
                    // disable features of your app
                    // or open another dialog explaining
                    // again the permission and directing to
                    // the app setting
                    permissionListener.onDenyNeverAskAgain(requestCode);
                } else if (Manifest.permission.WRITE_CONTACTS.equals(permissions[0])) {
                    showRationale(permission, R.string.permission_denied_contacts);
                    // user did NOT check "never ask again"
                    // this is a good place to explain the user
                    // why you need the permission and ask if he wants
                    // to accept it (the rationale)
                    permissionListener.onDeny(requestCode);
                } else if (*//**//* possibly check more permissions...*//**//*) {
                }
               *//**//* if (valid)
                    permissionListener.onAllow(requestCode);
                else if(showRationale)
                    permissionListener.onDeny(requestCode);*//*

                if (valid)
                    permissionListener.onAllow(requestCode);
                else if(showRationale)
                    permissionListener.onDenyNeverAskAgain(requestCode);
                else 
                    permissionListener.onDeny(requestCode);
           *//* } else
            permissionListener.onDeny(requestCode);
        }*/
    }


    public interface permissionListener {
        public void onAllow(int requestCode);

        public void onDeny(int requestCode);

        public void onDenyNeverAskAgain(int requestCode);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void showPermissionAllowDialog(Context mContext) {
        showPermissionAllowDialog(mContext, false);
    }

    public static void showPermissionAllowDialog(Context mContext, boolean isHideCancel) {
        showPermissionAllowDialog(mContext, false, false);
    }

    public static void showPermissionAllowDialog(Context mContext, boolean isHideCancel, boolean isBackPress) {
        MaterialAlertDialogBuilder mMaterialAlertDialogBuilder = new MaterialAlertDialogBuilder(mContext)
                .setMessage(mContext.getResources().getString(R.string.location_permission_desc))
                .setPositiveButton(mContext.getResources().getString(R.string.go_to_setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        mContext.startActivity(intent);
                    }
                });
        if (!isHideCancel) {
            mMaterialAlertDialogBuilder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog1, int which) {
                    dialog1.dismiss();
                    if (isBackPress && mContext instanceof NavigationMainActivity) {
                        ((NavigationMainActivity) mContext).onBackPressed();
                    }
                }
            });
        }
        mMaterialAlertDialogBuilder.show();
    }
}
