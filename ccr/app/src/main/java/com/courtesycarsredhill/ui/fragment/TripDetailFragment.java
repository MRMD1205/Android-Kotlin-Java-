package com.courtesycarsredhill.ui.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.adapters.ChildListAdapter;
import com.courtesycarsredhill.databinding.FragmentTripDetailBinding;
import com.courtesycarsredhill.dialogs.FeedbackDialog;
import com.courtesycarsredhill.dialogs.FilterDialog;
import com.courtesycarsredhill.dialogs.MessageDialog;
import com.courtesycarsredhill.interfaces.OnDismissedCall;
import com.courtesycarsredhill.model.CheckListData;
import com.courtesycarsredhill.model.TripListData;
import com.courtesycarsredhill.model.TripdetailData;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.ui.base.BaseBinder;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.ui.base.ProgressDialog;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.util.PermissionUtils;
import com.courtesycarsredhill.util.Utils;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class TripDetailFragment extends BaseFragment implements OnDismissedCall {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REFRESH_DELAY = 4000;

    FragmentTripDetailBinding mBinding;
    ChildListAdapter mAdapter;
    private TripListData tripData;
    private boolean isHistory = false;
    TripdetailData studendetailData;
    FusedLocationProviderClient fusedLocationProviderClient;
    private double lat, lng;
    LocationRequest mLocationRequest;
    private boolean isFromAPI = false, isDisplayMap = false;
    private ProgressDialog progressDialog;
    private int lastPosition = -2;
    private Boolean sosSuccess = false;

    private View view;

    //ProgressDialog progressDialog=new ProgressDialog(getContext());

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            Log.e("isLocationAvailable ", "=  " + locationAvailability.isLocationAvailable());
        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.e("onLocationResult call ", "=  " + locationResult.getLastLocation());
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.hide();
            }
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location fuselocation = locationList.get(locationList.size() - 1);
                Log.e("MapsActivity", "Location: " + fuselocation.getLatitude() + " " + fuselocation.getLongitude());
                lat = fuselocation.getLatitude();
                lng = fuselocation.getLongitude();
                if (isDisplayMap) {
                    isDisplayMap = false;
                    isFromAPI = false;
                    ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, MapFragment.newInstance(tripData.getDailyroutetripid()), true, true);
                } else if (isFromAPI) {
                    if (studendetailData.getTripStatus().equals(AppConstants.TRIP_ON_GOING)) {
                        new MessageDialog(getContext())
                                .setMessage(getString(R.string.alert_end_trip))
                                .cancelable(true)
                                .setPositiveButton(getString(android.R.string.yes), (dialog, which) -> {
                                    dialog.dismiss();
                                    // API_Logout(session.getUserDetail().getLoginId());
                                    callTripStatusApi();
                                    isFromAPI = false;
                                    isDisplayMap = false;
                                })
                                .setNegativeButton(getString(android.R.string.no), (dialog, which) -> dialog.dismiss()).show();
                    } else {
                        callTripStatusApi();
                        isFromAPI = false;
                        isDisplayMap = false;
                    }
                }
            }
        }
    };

    public TripDetailFragment() {
        // Required empty public constructor
    }


    public static TripDetailFragment newInstance(TripListData tripData, boolean isHistory) {
        TripDetailFragment fragment = new TripDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("tripData", tripData);
        args.putBoolean("IS_HISTORY", isHistory);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("tripData")) {
                tripData = (TripListData) getArguments().get("tripData");
            }
            if (getArguments().containsKey("IS_HISTORY")) {
                isHistory = getArguments().getBoolean("IS_HISTORY");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_detail, container, false);
            view = mBinding.getRoot();
       /* if (!isHistory)
            setHasOptionsMenu(true);*/

            setupToolBarWithBackArrow(mBinding.toolbar.toolbar, tripData.getTripname());
            fusedLocationProviderClient = new FusedLocationProviderClient(mContext);
            buildLocationSettingsRequest();
            mBinding.icLayoutTripDetail.getRoot().setVisibility(View.GONE);
            callApi();
            mBinding.pullToRefresh.setOnRefreshListener(() -> {
                mBinding.pullToRefresh.setRefreshing(false);
                callApi();
            });
            setClick();
        }
        return view;
    }

    private void setButtonLayout() {
        if (studendetailData.getTripStatus().equals(AppConstants.TRIP_COMPLETED)) {
            mBinding.icLayoutTripDetail.layoutTripTiming.btTripStatus.setBackgroundColor(getResources().getColor(R.color.grey));
            if (isHistory) {
                mBinding.icLayoutTripDetail.layoutTripTiming.btTripStatus.setText(getString(R.string.str_earn) + " " + getString(R.string.str_currency) + studendetailData.getDriverCost());
            } else {
                mBinding.icLayoutTripDetail.layoutTripTiming.btTripStatus.setText(getString(R.string.completed));
            }
        } else if (studendetailData.getTripStatus().equals(AppConstants.TRIP_ON_GOING)) {
            mBinding.icLayoutTripDetail.layoutTripTiming.btTripStatus.setBackgroundColor(getResources().getColor(R.color.red));
            mBinding.icLayoutTripDetail.layoutTripTiming.btTripStatus.setText(getString(R.string.end_trip));
        }

        if (isHistory) {
            mBinding.icLayoutTripDetail.layoutSchoolHistory.cvMain.setVisibility(View.VISIBLE);
            mBinding.icLayoutTripDetail.layoutSchool.cvMain.setVisibility(View.GONE);
        } else {
            mBinding.icLayoutTripDetail.layoutSchoolHistory.cvMain.setVisibility(View.GONE);
            mBinding.icLayoutTripDetail.layoutSchool.cvMain.setVisibility(View.VISIBLE);
        }
    }

    private void callApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("DailyRouteTripID", String.valueOf(tripData.getDailyroutetripid()));
        params.put("StudentID", "null");
        try {
            Retrofit.with(mContext)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setGetParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_TRIP_DETAIL, session))
                    .setHeaderCallBackListener(new JSONCallback(mContext, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    Gson gson = new Gson();
                                    studendetailData = gson.fromJson(jsonObject.optJSONObject("Data").toString(),
                                            new TypeToken<TripdetailData>() {
                                            }.getType());

                                    Logger.e(studendetailData.toString());
                                    for (int i = 0; i < studendetailData.getDailyRouteStudentMasterModelList().size(); i++) {
                                        studendetailData.getDailyRouteStudentMasterModelList().get(i)
                                                .setTriptype(studendetailData.getTripType());
                                        studendetailData.getDailyRouteStudentMasterModelList().get(i)
                                                .setTripstatus(studendetailData.getTripStatus());
                                    }
                                    if (studendetailData != null) {
                                        setData(studendetailData);
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

    private void setData(TripdetailData studendetailData) {
        mBinding.icLayoutTripDetail.getRoot().setVisibility(View.VISIBLE);
        if (studendetailData.getTripStatus().equals(AppConstants.TRIP_READY_TO_GO)) {
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.
                    setBackgroundColor(mContext.getResources().getColor(R.color.yellow_ec));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.
                    setTextColor(mContext.getResources().getColor(R.color.black));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTripType.
                    setTextColor(mContext.getResources().getColor(R.color.black));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvStatus.
                    setTextColor(mContext.getResources().getColor(R.color.black));

        } else if (studendetailData.getTripStatus().equals(AppConstants.TRIP_ON_GOING)) {
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.
                    setBackgroundColor(mContext.getResources().getColor(R.color.sky));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.
                    setTextColor(mContext.getResources().getColor(R.color.white));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTripType.
                    setTextColor(mContext.getResources().getColor(R.color.white));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvStatus.
                    setTextColor(mContext.getResources().getColor(R.color.white));
        } else if (studendetailData.getTripStatus().equals(AppConstants.TRIP_COMPLETED)) {
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.
                    setBackgroundColor(mContext.getResources().getColor(R.color.green));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.
                    setTextColor(mContext.getResources().getColor(R.color.white));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvStatus.
                    setTextColor(mContext.getResources().getColor(R.color.white));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTripType.
                    setTextColor(mContext.getResources().getColor(R.color.white));
        }
        setButtonLayout();
        setDriverData();
        setEscortData();
        setHomeData();
        setAdapter();

        mBinding.icLayoutTripDetail.setItem(studendetailData);
        mBinding.icLayoutTripDetail.layoutTripTiming.setItem(studendetailData);
        mBinding.icLayoutTripDetail.layoutTripTiming.layoutSourceTime.setItem(studendetailData);
        mBinding.icLayoutTripDetail.layoutTripTiming.layoutSourceTime.executePendingBindings();

        mBinding.icLayoutTripDetail.layoutTripTiming.layoutDestinationTime.setItem(studendetailData);
        mBinding.icLayoutTripDetail.layoutTripTiming.layoutDestinationTime.executePendingBindings();
        if (((BaseActivity) getContext()).isParent()) {
            mBinding.icLayoutTripDetail.layoutTripTiming.layoutSourceTime.
                    tvTime.setText(studendetailData.getNewPickupTime());
        } else {
            mBinding.icLayoutTripDetail.layoutTripTiming.layoutSourceTime.
                    tvTime.setText(studendetailData.getArrivalTime());
        }
        mBinding.icLayoutTripDetail.layoutTripTiming.layoutDestinationTime.
                tvTime.setText(studendetailData.getLeaveTime());

        mBinding.executePendingBindings();

        if (!((BaseActivity) getContext()).isParent()) {
            if (studendetailData.getTripStatus().equals(AppConstants.TRIP_COMPLETED)) {
                mBinding.icLayoutTripDetail.btGiveTripFeedback.setVisibility(View.VISIBLE);
            }
        } else {
            mBinding.icLayoutTripDetail.btGiveTripFeedback.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        if (studendetailData.getDailyRouteStudentMasterModelList() != null && studendetailData.getDailyRouteStudentMasterModelList().size() > 0) {
            mAdapter = new ChildListAdapter(mContext, isHistory, studendetailData.getDailyRouteStudentMasterModelList());
            mBinding.icLayoutTripDetail.layoutChildrenList.recyclerView.setAdapter(mAdapter);

            mBinding.icLayoutTripDetail.nestedScrollView.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        final float y = lastPosition == -2 ? 0 : mBinding.icLayoutTripDetail.layoutChildrenList.cvMain.getY() + mBinding.icLayoutTripDetail.layoutChildrenList.recyclerView.getChildAt(lastPosition + 1).getY();
                        mBinding.icLayoutTripDetail.nestedScrollView.fling(0);
                        mBinding.icLayoutTripDetail.nestedScrollView.smoothScrollTo(0, (int) y);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setHomeData() {
        mBinding.icLayoutTripDetail.layoutSchool.layoutImgText.ivUserImage.setVisibility(View.INVISIBLE);
        mBinding.icLayoutTripDetail.layoutSchool.layoutImgText.tvLetter.setVisibility(View.VISIBLE);
        mBinding.icLayoutTripDetail.layoutSchool.layoutImgText.tvLetter.setText(studendetailData.getSchoolName().substring(0, 2));
        mBinding.icLayoutTripDetail.layoutSchool.tvTitle.setText(getString(R.string.str_school));
        mBinding.icLayoutTripDetail.layoutSchool.tvName.setText(studendetailData.getSchoolName());
        mBinding.icLayoutTripDetail.layoutSchool.tvAddress.setText(studendetailData.getSchoolAddress());
        mBinding.icLayoutTripDetail.layoutSchool.ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.icLayoutTripDetail.layoutSchool.ivPhone.getVisibility() == View.VISIBLE)
                    Utils.dialdNumber(getContext(), studendetailData.getSchoolContactNo());
            }
        });

        mBinding.icLayoutTripDetail.layoutSchool.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f ", studendetailData.getDailyRouteStudentMasterModelList().get(0).getSchoolLatitude(), studendetailData.getDailyRouteStudentMasterModelList().get(0).getSchoolLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    try {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    } catch (ActivityNotFoundException innerEx) {
                        Toast.makeText(getContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mBinding.icLayoutTripDetail.layoutSchoolHistory.layoutImgText.ivUserImage.setVisibility(View.INVISIBLE);
        mBinding.icLayoutTripDetail.layoutSchoolHistory.layoutImgText.tvLetter.setVisibility(View.VISIBLE);
        mBinding.icLayoutTripDetail.layoutSchoolHistory.layoutImgText.tvLetter.setText(studendetailData.getSchoolName().substring(0, 2));
        mBinding.icLayoutTripDetail.layoutSchoolHistory.tvTitle.setText(getString(R.string.str_school));
        mBinding.icLayoutTripDetail.layoutSchoolHistory.tvName.setText(studendetailData.getSchoolName());
        mBinding.icLayoutTripDetail.layoutSchoolHistory.tvAddress.setText(studendetailData.getSchoolAddress());
        mBinding.icLayoutTripDetail.layoutSchoolHistory.ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.icLayoutTripDetail.layoutSchoolHistory.ivPhone.getVisibility() == View.VISIBLE)
                    Utils.dialdNumber(getContext(), studendetailData.getSchoolContactNo());
            }
        });

    }

    private void setEscortData() {
        if (studendetailData.getEscortID() != null && studendetailData.getEscortName() != null) {
            mBinding.icLayoutTripDetail.layoutEscort.getRoot().setVisibility(View.VISIBLE);
            mBinding.icLayoutTripDetail.layoutEscort.tvTitle.setText(getString(R.string.str_escort));
            mBinding.icLayoutTripDetail.layoutEscort.tvName.setText(studendetailData.getEscortName());
            mBinding.icLayoutTripDetail.layoutEscort.tvAddress.setText(studendetailData.getEscortContactNo());
            mBinding.icLayoutTripDetail.layoutEscort.layoutImgText.ivUserImage.setVisibility(View.VISIBLE);
            mBinding.icLayoutTripDetail.layoutEscort.layoutImgText.tvLetter.setVisibility(View.INVISIBLE);
            BaseBinder.setImageUrl(mBinding.icLayoutTripDetail.layoutEscort.layoutImgText.ivUserImage, studendetailData.getEscortimg());
            mBinding.icLayoutTripDetail.layoutEscort.ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBinding.icLayoutTripDetail.layoutEscort.ivPhone.getVisibility() == View.VISIBLE)
                        Utils.dialdNumber(getContext(), studendetailData.getEscortContactNo());
                }
            });
        } else {
            mBinding.icLayoutTripDetail.layoutEscort.getRoot().setVisibility(View.GONE);
        }
    }

    private void setDriverData() {

        mBinding.icLayoutTripDetail.layoutDriver.tvTitle.setText(getString(R.string.str_driver));
        mBinding.icLayoutTripDetail.layoutDriver.tvName.setText(studendetailData.getDriverName());
        mBinding.icLayoutTripDetail.layoutDriver.tvAddress.setText(studendetailData.getDriverContactNo());
        mBinding.icLayoutTripDetail.layoutDriver.ivPhone.setVisibility(View.GONE);
        if (isHistory) {
            mBinding.icLayoutTripDetail.layoutDriver.tvPrice.setVisibility(View.VISIBLE);
            mBinding.icLayoutTripDetail.layoutDriver.tvPrice.setText(getString(R.string.str_currency) + studendetailData.getDriverCost());
        } else {
            mBinding.icLayoutTripDetail.layoutDriver.tvPrice.setVisibility(View.GONE);
        }
        mBinding.icLayoutTripDetail.layoutDriver.layoutImgText.ivUserImage.setVisibility(View.VISIBLE);
        mBinding.icLayoutTripDetail.layoutDriver.layoutImgText.tvLetter.setVisibility(View.INVISIBLE);
        BaseBinder.setImageUrl(mBinding.icLayoutTripDetail.layoutDriver.layoutImgText.ivUserImage, studendetailData.getDriverimg());
    }

    private void setClick() {
        mBinding.icLayoutTripDetail.layoutTripTiming.btTripStatus.setOnClickListener(view -> {
            if (studendetailData != null)
                if (!studendetailData.getTripStatus().equals(AppConstants.TRIP_COMPLETED)) {
                    if (lat == 0.0 || lng == 0.0) {
                        isFromAPI = true;
                        isDisplayMap = false;
                        buildLocationSettingsRequest();
                    } else {
                        if (studendetailData.getTripStatus().equals(AppConstants.TRIP_ON_GOING)) {
                            new MessageDialog(getContext())
                                    .setMessage(getString(R.string.alert_end_trip))
                                    .cancelable(true)
                                    .setPositiveButton(getString(android.R.string.yes), (dialog, which) -> {
                                        dialog.dismiss();
                                        // API_Logout(session.getUserDetail().getLoginId());
                                        callTripStatusApi();
                                        ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, new HomeFragment(), false, true);
                                    })
                                    .setNegativeButton(getString(android.R.string.no), (dialog, which) -> dialog.dismiss()).show();
                        } else {
                            callTripStatusApi();
                        }
                    }
                }
            //  ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, ChildShortDetailFragment.newInstance(), true, true);
        });


        mBinding.icLayoutTripDetail.btGiveTripFeedback.setOnClickListener(view -> {
            showFeedbackDialog();
        });

        mBinding.toolbar.ivSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOSDialog();
            }
        });
    }


    private void callTripStatusApi() {

        HashMap<String, String> params = new HashMap<>();
        params.put("DailyRouteTripID", studendetailData.getDailyRouteTripID());
        if (studendetailData.getTripStatus().equals(AppConstants.TRIP_READY_TO_GO))
            params.put("TripStatusId", String.valueOf(AppConstants.TRIP_START_STATUS));
        else if (studendetailData.getTripStatus().equals(AppConstants.TRIP_ON_GOING))
            params.put("TripStatusId", String.valueOf(AppConstants.TRIP_END_STATUS));

        params.put("Latitude", String.valueOf(lat));
        params.put("Longitude", String.valueOf(lng));
        params.put("UserType", session.getUserDetail().getUsertype());
        params.put("UserId", String.valueOf(session.getUserDetail().getUserid()));

        try {
            Retrofit.with(mContext)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_CHANGE_TRIP_STATUS, session))
                    .setHeaderCallBackListener(new JSONCallback(mContext, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    showShortToast(jsonObject.optString("Message"));
                                    if (studendetailData.getTripStatus().equals(AppConstants.TRIP_READY_TO_GO)) {
                                        lastPosition = -1;
                                    }
                                    callApi();
                                    if (studendetailData.getTripStatus().equals(AppConstants.TRIP_ON_GOING)) {
                                        showFeedbackDialog();
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

    private void showFeedbackDialog() {
        FeedbackDialog feedbackDialog = new FeedbackDialog();
        feedbackDialog.setListener(this);
        feedbackDialog.setCancelable(false);
        feedbackDialog.show(((NavigationMainActivity) getActivity()).getSupportFragmentManager(), FilterDialog.class.getName());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_navigation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_navigation:
                if (studendetailData != null && PermissionUtils.hasPermissions(mContext, PermissionUtils.locationPermissions)) {
                    ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, MapFragment.newInstance(tripData.getDailyroutetripid()), true, true);
                } else {
                    isFromAPI = false;
                    isDisplayMap = true;
                    requestPermissions(PermissionUtils.locationPermissions, PermissionUtils.ACCESS_LOCATION_CODE);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void buildLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(mActivity).addApi(LocationServices.API).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(mActivity).checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {
            try {
                task.getResult(ApiException.class);
                // All location settings are satisfied. The client can initialize location
                // requests here.
                Log.e("OK CLICK", "All location settings are satisfied.");
                getLocation();

            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            //Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException | ClassCastException e) {
                            // Ignore the error.
                            //Log.e(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        // Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void getLocation() {
        if (PermissionUtils.hasPermissions(mContext, PermissionUtils.locationPermissions)) {
            getCurrentLocation();
        } else {
            requestPermissions(PermissionUtils.locationPermissions, PermissionUtils.ACCESS_LOCATION_CODE);
        }
//        PermissionUtils.isCheck(mContext, PermissionUtils.locationPermissions, PermissionUtils.ACCESS_LOCATION_CODE, permissionListener);
        /*if (hasAppPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})) {
            getCurrentLocation();
        }else{
            getLocationPermission();
        }*/
    }

    private void getCurrentLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
    }

    /*private void getLocationPermission() {
        requestAppPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION, new setPermissionListener() {
            @Override
            public void onPermissionGranted(int requestCode) {
                getCurrentLocation();
            }

            @Override
            public void onPermissionDenied(int requestCode) {
                getLocationPermission();
            }

        });
    }*/

    private PermissionUtils.permissionListener permissionListener = new PermissionUtils.permissionListener() {
        @Override
        public void onAllow(int requestCode) {
            getCurrentLocation();
        }

        @Override
        public void onDeny(int requestCode) {
            PermissionUtils.showPermissionAllowDialog(mContext);
        }

        @Override
        public void onDenyNeverAskAgain(int requestCode) {
            PermissionUtils.showPermissionAllowDialog(mContext, true);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(mActivity, requestCode, permissions, grantResults, permissionListener);
    }


    @Override
    public void onDismissCalled(int val1, int val2) {

    }

    @Override
    public void onEquipmentSelected(String action, ArrayList<CheckListData> values) {

    }

    @Override
    public void onFeedbackGiven(int val1, String Text) {
        callFeedbackApi(val1, Text);
    }

    private void callFeedbackApi(int val1, String text) {
        JSONObject object = new JSONObject();
        try {
            object.put("DriverID", String.valueOf(session.getUserDetail().getUserid()));
            object.put("DailyRouteTripID", studendetailData.getDailyRouteTripID());
            object.put("Comment", text);
            object.put("TripFeedbackType", val1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Retrofit.with(mContext)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setParameters(object)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_SAVE_FEEDBACK, session))
                    .setHeaderCallBackListener(new JSONCallback(mContext, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    Toast.makeText(mContext, jsonObject.optString(getString(R.string.message)), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
            Log.e("OK CLICKED", ":::");
            if (isFromAPI) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
            }
            getLocation();
        }
    }

    public void scrollPage(int status, int position, boolean isLast) {
        lastPosition = isLast ? -2 : position;
        callApi();
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
