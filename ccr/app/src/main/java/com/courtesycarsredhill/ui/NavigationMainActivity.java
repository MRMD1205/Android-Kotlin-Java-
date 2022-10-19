package com.courtesycarsredhill.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.adapters.DrawerItemAdapter;
import com.courtesycarsredhill.databinding.ActivityNavigationMainBinding;
import com.courtesycarsredhill.dialogs.EquipmentDialog;
import com.courtesycarsredhill.dialogs.MessageDialog;
import com.courtesycarsredhill.interfaces.OnDismissedCall;
import com.courtesycarsredhill.interfaces.OnItemSelected;
import com.courtesycarsredhill.interfaces.OnStudentStatusUpdated;
import com.courtesycarsredhill.model.CheckListData;
import com.courtesycarsredhill.model.CurrentAppVersionModel;
import com.courtesycarsredhill.model.DrawerItem;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.ui.fragment.AboutFragment;
import com.courtesycarsredhill.ui.fragment.ContactFragment;
import com.courtesycarsredhill.ui.fragment.ContactParentFragment;
import com.courtesycarsredhill.ui.fragment.DocumentFragment;
import com.courtesycarsredhill.ui.fragment.DocumentListFragment;
import com.courtesycarsredhill.ui.fragment.HistoryFragment;
import com.courtesycarsredhill.ui.fragment.HomeFragment;
import com.courtesycarsredhill.ui.fragment.NotificationListFragment;
import com.courtesycarsredhill.ui.fragment.TermsnConditionFragment;
import com.courtesycarsredhill.ui.fragment.TripDetailFragment;
import com.courtesycarsredhill.ui.fragment.UpcomingTripFragment;
import com.courtesycarsredhill.ui.fragment.UploadDocumentFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.util.SessionManager;
import com.courtesycarsredhill.util.TimeStamp;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.firebase.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class NavigationMainActivity extends BaseActivity implements OnItemSelected<String>, OnDismissedCall, OnStudentStatusUpdated {
    public ActivityNavigationMainBinding mBinding;
    DrawerItemAdapter drawerItemCustomAdapter;
    public ArrayList<DrawerItem> objectDrawerItems;
    private int position = 0;
    public String mCurrentTab;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    /* A HashMap of stacks, where we use tab identifier as keys..*/
    private HashMap<String, Stack<Fragment>> mStacks;

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_navigation_main);
        prepareLayout();
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);
    }

    private void prepareLayout() {
        //  isSP = session.getBooleanDataByKey(KEY_IS_SP);
        if (session.getUserDetail().getUsertype().equals("Parents")) {
            mBinding.layoutUserDetail.ivLogin.setVisibility(View.VISIBLE);
            mBinding.layoutUserDetail.tvLogin.setVisibility(View.INVISIBLE);
        } else {
            mBinding.layoutUserDetail.tvLogin.setVisibility(View.VISIBLE);
        }
        mBinding.tvVersion.setText(getString(R.string.version_1_0_0) + BuildConfig.VERSION_NAME);
        mBinding.layoutUserDetail.setUserData(session.getUserDetail());
        mBinding.layoutUserDetail.executePendingBindings();
        setTitle("");
        mStacks = new HashMap<>();
        mStacks.put(AppConstants.NAVIGATION_KEY
                , new Stack<>());
        replaceFragment(AppConstants.NAVIGATION_KEY, new HomeFragment(), true, true);
        setupNavigation();
        callgetCurrentVersionApi();
        if (!isParent()) {
            callgetVehicleCheckListApi();
        }
    }

    private void callgetVehicleCheckListApi() {

        HashMap<String, String> params = new HashMap<>();
        params.put("DriverID", String.valueOf(session.getUserDetail().getUserid()));

        try {
            Retrofit.with(this)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setGetParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_GET_VEHICFLE_CHECKLIST, session))
                    .setHeaderCallBackListener(new JSONCallback(this) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {

                                    if (jsonObject.optJSONArray("Data") != null) {
                                        Gson gson = new Gson();
                                        ArrayList<CheckListData> vehicleCheckList = gson.fromJson(jsonObject.optJSONArray("Data").toString(),
                                                new TypeToken<List<CheckListData>>() {
                                                }.getType());

                                        Logger.e(vehicleCheckList.toString());
                                        if (vehicleCheckList.size() > 0) {
                                            showEquipmentDialog(vehicleCheckList);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showShortToast(jsonObject.optString(getString(R.string.message)));
                            }
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            showShortToast(message);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEquipmentDialog(ArrayList<CheckListData> vehicleCheckList) {
        EquipmentDialog equipmentDialog = new EquipmentDialog(vehicleCheckList);
        equipmentDialog.setListener(this);
        equipmentDialog.show(getSupportFragmentManager(), EquipmentDialog.class.getName());
    }

    private void callgetCurrentVersionApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("", "");

        try {
            Retrofit.with(this)
                    .setGetParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_GET_CURRENT_APP_VERSION, session))
                    .setHeaderCallBackListener(new JSONCallback(this) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    if (jsonObject.optJSONArray("Data") != null) {
                                        Gson gson = new Gson();
                                        ArrayList<CurrentAppVersionModel> model = gson.fromJson(jsonObject.optJSONArray("Data").toString(),
                                                new TypeToken<List<CurrentAppVersionModel>>() {
                                                }.getType());
                                        int appVersionCode = BuildConfig.VERSION_CODE;
                                        int apiVersionCode = Integer.parseInt(model.get(0).getCurrentAndroidVersion());
                                        if (apiVersionCode != appVersionCode) {
                                            if (model.get(0).getForceAndroidUpdate()) {
//                                            For In App Update
//                                            checkForInAppUpdate();

//                                            Custom Dialog Update
//                                                updateApp();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showShortToast(jsonObject.optString(getString(R.string.message)));
                            }
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            showShortToast(message);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateApp() {

        new MessageDialog(NavigationMainActivity.this)
                .setTitle(getString(R.string.update_required))
                .setMessage(getString(R.string.update_app_desc))
                .cancelable(false)
                .setPositiveButton(getString(R.string.update), (dialog, which) -> {
                    dialog.dismiss();
                    Intent update = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                    startActivity(update);
                })
                .setNegativeButton(getString(R.string.close_app), (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                    System.exit(0);
                }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mBinding.drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigation() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        mBinding.drawerList.setLayoutManager(lm);
        mBinding.drawerList.setNestedScrollingEnabled(false);
        drawerItemCustomAdapter = new DrawerItemAdapter(this, getObjectDrawerItemForSP(), this, position);
        mBinding.drawerList.setAdapter(drawerItemCustomAdapter);

    }

    private ArrayList<DrawerItem> getObjectDrawerItemForSP() {
        ArrayList<DrawerItem> objectDrawerItems = new ArrayList<>();
        objectDrawerItems.add(new DrawerItem(R.drawable.ic_home, R.drawable.ic_home, AppConstants.HOME));
        // objectDrawerItems.add(new DrawerItem(R.drawable.ic_trips, R.drawable.ic_trips, AppConstants.UPCOMING_TRIP));
        if (!isParent())
            objectDrawerItems.add(new DrawerItem(R.drawable.ic_history, R.drawable.ic_history, AppConstants.HISTORY));
        //objectDrawerItems.add(new DrawerItem(R.drawable.ic_notification, R.drawable.ic_notification, AppConstants.NOTIFICATION));
        objectDrawerItems.add(new DrawerItem(R.drawable.ic_documents, R.drawable.ic_documents, AppConstants.DOCUMENTS));
        objectDrawerItems.add(new DrawerItem(R.drawable.ic_aboutus, R.drawable.ic_aboutus, AppConstants.ABOUT_US));
        objectDrawerItems.add(new DrawerItem(R.drawable.ic_terms_, R.drawable.ic_terms_, AppConstants.TNC));
        objectDrawerItems.add(new DrawerItem(R.drawable.ic_help, R.drawable.ic_help, AppConstants.HELP));
        objectDrawerItems.add(new DrawerItem(R.drawable.ic_signout, R.drawable.ic_signout, AppConstants.SIGN_OUT));

        return objectDrawerItems;
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
                if (getSupportFragmentManager().findFragmentById(R.id.realTabContent) instanceof UploadDocumentFragment) {
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new DocumentListFragment(), true, true);
                }
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                if (getSupportFragmentManager().findFragmentById(R.id.realTabContent) instanceof HomeFragment) {
                    if (doubleBackToExitPressedOnce) {
                        finish();
                        return;
                    }

                    this.doubleBackToExitPressedOnce = true;
                    showShortToast(getString(R.string.alert_back));
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, AppConstants.BACK_DELAY);
                } else {
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, HomeFragment.newInstance(), true, true);
                }
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemSelected(String string) {

        drawerItemCustomAdapter.setPosition(position);
        if (mBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.realTabContent);
        switch (string) {

            case AppConstants.HOME:
                if (!(fragment instanceof HomeFragment))
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new HomeFragment(), false, true);
                break;

            case AppConstants.HISTORY:
                if (!(fragment instanceof HistoryFragment))
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new HistoryFragment(), false, true);
                break;

            case AppConstants.DOCUMENTS:
                if (!(fragment instanceof DocumentListFragment))
                    replaceFragment(AppConstants.NAVIGATION_KEY, new DocumentListFragment(), false, true);
                break;

            case AppConstants.UPCOMING_TRIP:
                if (!(fragment instanceof UpcomingTripFragment))
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new UpcomingTripFragment(), false, true);
                break;

            case AppConstants.NOTIFICATION:
                if (!(fragment instanceof NotificationListFragment))
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new NotificationListFragment(), false, true);
                break;

            case AppConstants.TNC:
                if (!(fragment instanceof TermsnConditionFragment))
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new TermsnConditionFragment(), false, true);
                break;

            case AppConstants.ABOUT_US:
                if (!(fragment instanceof AboutFragment))
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new AboutFragment(), false, true);
                break;

            case AppConstants.HELP:
                if (session.getUserDetail().getUsertype().equals("Parents")) {
                    if (!(fragment instanceof ContactParentFragment))
                        replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new ContactParentFragment(), false, true);
                } else if (!(fragment instanceof ContactFragment))
                    replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new ContactFragment(), false, true);
                break;

            case AppConstants.SIGN_OUT:

                new MessageDialog(NavigationMainActivity.this)
                        .setMessage(getString(R.string.alert_logout))
                        .cancelable(true)
                        .setPositiveButton(getString(android.R.string.yes), (dialog, which) -> {
                            dialog.dismiss();
                            // API_Logout(session.getUserDetail().getLoginId());
                            session.logoutUser(NavigationMainActivity.this);
                        })
                        .setNegativeButton(getString(android.R.string.no), (dialog, which) -> dialog.dismiss()).show();
                break;
        }
    }

    public void replaceFragment(String tag, Fragment selectedFragment, boolean shouldAdd, boolean shouldAnimate) {
     /*   if (shouldAdd)
            mStacks.get(tag).push(selectedFragment);*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        manager.getBackStackEntryCount();
        if (shouldAnimate)
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.realTabContent, selectedFragment, selectedFragment.getClass().getSimpleName());
        ft.addToBackStack(selectedFragment.getClass().getSimpleName());
        ft.commit();
    }

    public void navigateToFragment(
            Fragment fragment,
            boolean addToBackstack,
            String tag, boolean shouldAnimate

    ) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate)
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.realTabContent, fragment, tag);
        if (addToBackstack) ft.addToBackStack(tag);
        ft.commit();
    }

    public void replaceFragmentAndPop(String tag, Fragment selectedFragment, boolean shouldAdd, boolean shouldAnimate) {
        getSupportFragmentManager().popBackStackImmediate();
        replaceFragment(tag, selectedFragment, shouldAdd, shouldAnimate);
    }

    public void popFragment() {
        /*
         *    Select the second last fragment in current tab's stack..
         *    which will be shown after the fragment transaction given below
         */
        Fragment fragment = mStacks.get(AppConstants.NAVIGATION_KEY).elementAt(mStacks.get(mCurrentTab).size() - 2);

        /*pop current fragment from stack.. */
        mStacks.get(AppConstants.NAVIGATION_KEY).pop();

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    public void onDismissCalled(int val1, int val2) {

    }

    @Override
    public void onEquipmentSelected(String comment, ArrayList<CheckListData> values) {
        //Call Equipment API
        callSaveApi(comment, values);
    }

    private void callSaveApi(String comment, ArrayList<CheckListData> values) {
        JSONObject object = new JSONObject();
        try {
            object.put("DriverID", String.valueOf(session.getUserDetail().getUserid()));
            object.put("Comment", comment);
            object.put("ChecklistModelLst", getCheckList(values));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Retrofit.with(this)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setParameters(object)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_SAVE_VEHICFLE_CHECKLIST, session))
                    .setHeaderCallBackListener(new JSONCallback(this, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    session.storeDataByKey(SessionManager.KEY_LAST_EQIPMENT_DATE, TimeStamp.getCurrentDay());
                                    session.storeDataByKey(SessionManager.KEY_IS_SAVED_EQUIPMENT_DETAIL, true);
                                    Toast.makeText(NavigationMainActivity.this, jsonObject.optString(getString(R.string.message)), Toast.LENGTH_SHORT).show();
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

    private JSONArray getCheckList(ArrayList<CheckListData> values) {
        Gson gson = new Gson();
        JSONArray jsonArray = null;
        String listString = gson.toJson(
                values,
                new TypeToken<ArrayList<CheckListData>>() {
                }.getType());

        try {
            jsonArray = new JSONArray(listString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    @Override
    public void onFeedbackGiven(int val1, String val2) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.realTabContent);
        fragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
        if (mStacks.get(AppConstants.NAVIGATION_KEY).size() == 0) {
            return;
        }
        Log.e("OnActivity Result", "requet code" + requestCode + "result code " + resultCode);

        /*Now current fragment on screen gets onActivityResult callback..*/
        mStacks.get(AppConstants.NAVIGATION_KEY).lastElement().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStatusUpdated(int status, int position, boolean isLast) {
        Fragment mFragment = getSecondLastFragment();
        if (mFragment instanceof TripDetailFragment) {
            Logger.e("2nd last is TripDetailFragment");
            ((TripDetailFragment) mFragment).scrollPage(status, position, isLast);
        }
    }

    public Fragment getSecondLastFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            return null;
        }
        String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2).getName();
        return getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

/*    private void checkForInAppUpdate() {

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Log.d("TAG", "Update available");
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    // Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    // Start an update.
                } else if (appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    // Start an update.
                    startAppUpdateImmediate(appUpdateInfo);
                }
            } else {
                Log.d("TAG", "No Update available");
            }
        });
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    NavigationMainActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    // Checks that the update is not stalled during 'onResume()'.
    // However, you should execute this check at all entry points into the app.
    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            this,
                                            REQ_CODE_VERSION_UPDATE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }*/
}
