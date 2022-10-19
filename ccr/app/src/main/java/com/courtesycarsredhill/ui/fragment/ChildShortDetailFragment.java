package com.courtesycarsredhill.ui.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.FragmentChildShortDetailBinding;
import com.courtesycarsredhill.dialogs.AddAbsentDialog;
import com.courtesycarsredhill.interfaces.OnItemSelected;
import com.courtesycarsredhill.interfaces.OnStudentStatusUpdated;
import com.courtesycarsredhill.model.TripdetailData;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.ui.base.ProgressDialog;
import com.courtesycarsredhill.util.AppConstants;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.courtesycarsredhill.ui.fragment.TripDetailFragment.REQUEST_CHECK_SETTINGS;
import static com.courtesycarsredhill.util.AppConstants.DRIVER_ARRIVED;
import static com.courtesycarsredhill.util.AppConstants.PARENT_DROP_AND_PICKUP;
import static com.courtesycarsredhill.util.AppConstants.STUDENT_ABSENT;
import static com.courtesycarsredhill.util.AppConstants.STUDENT_DROP;
import static com.courtesycarsredhill.util.AppConstants.STUDENT_PICKUP;


public class ChildShortDetailFragment extends BaseFragment implements OnItemSelected<String> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int studentStatus;
    private Boolean sosSuccess = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentChildShortDetailBinding mBinding;
    private TripdetailData.Dailyroutestudentmastermodellist tripStudentDetail;
    FusedLocationProviderClient fusedLocationProviderClient;
    private double lat, lng;
    private LocationRequest mLocationRequest;
    private int position;
    private boolean isFromAPI = false, isLast, isChildSelected;
    private String message = "";
    private ProgressDialog progressDialog;
    private OnStudentStatusUpdated mOnStudentStatusUpdatedListener;

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            Log.d("isLocationAvailable ", "=  " + locationAvailability.isLocationAvailable());

        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            //Log.d("onLocationResult call ", "=  " + locationResult.getLastLocation());
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.hide();
            }
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location fuselocation = locationList.get(locationList.size() - 1);
                lat = fuselocation.getLatitude();
                lng = fuselocation.getLongitude();
                if (isFromAPI) {
                    callApi();
                    isFromAPI = false;
                }
                Log.e("MapsActivity", "Location: " + lat + " " + lng);
//                Toast.makeText(getContext(), "Location: " + lat + " " + lng,Toast.LENGTH_LONG).show();

//                callApi("");

            }
        }
    };

    public ChildShortDetailFragment() {
    }


    public static ChildShortDetailFragment newInstance(TripdetailData.Dailyroutestudentmastermodellist tripStudentDetail, int position, boolean isLast) {
        ChildShortDetailFragment fragment = new ChildShortDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("STUDENT_DATA", tripStudentDetail);
        args.putInt("position", position);
        args.putBoolean("isLast", isLast);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("STUDENT_DATA")) {
            tripStudentDetail = (TripdetailData.Dailyroutestudentmastermodellist) getArguments().getSerializable("STUDENT_DATA");
            position = getArguments().getInt("position");
            isLast = getArguments().getBoolean("isLast");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mOnStudentStatusUpdatedListener = (OnStudentStatusUpdated) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("OnStudentStatusUpdated not found");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBarWithBackArrow(mBinding.toolbar.toolbar, getString(R.string.str_status));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_child_short_detail, container, false);
        fusedLocationProviderClient = new FusedLocationProviderClient(mContext);
        buildLocationSettingsRequest();
        if (tripStudentDetail != null) {
            setData();
            setClicks();
        }
        return mBinding.getRoot();
    }

    private void setClicks() {

        mBinding.toolbar.ivSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOSDialog();
            }
        });

        mBinding.layoutshortDetail.layoutParentInfo.tvTitle.setOnClickListener(v -> {
            startGoogleMapApp(tripStudentDetail);
        });
        mBinding.layoutshortDetail.layoutSchoolInfo.ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.layoutshortDetail.layoutSchoolInfo.ivPhone.getVisibility() == View.VISIBLE)
                    Utils.dialdNumber(mContext, tripStudentDetail.getSchoolcontactno());
            }
        });
        mBinding.layoutshortDetail.layoutParentInfo.ivEmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.layoutshortDetail.layoutParentInfo.ivEmergencyCall.getVisibility() == View.VISIBLE)
                    Utils.dialdNumber(mContext, tripStudentDetail.getEmergencycontact());
            }
        });
        mBinding.layoutshortDetail.layoutParentInfo.ivPhonePrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.layoutshortDetail.layoutParentInfo.ivPhonePrimary.getVisibility() == View.VISIBLE)
                    Utils.dialdNumber(mContext, tripStudentDetail.getContactparent1());
            }
        });
        mBinding.layoutshortDetail.layoutParentInfo.ivPhoneSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.layoutshortDetail.layoutParentInfo.ivPhoneSecondary.getVisibility() == View.VISIBLE)
                    Utils.dialdNumber(mContext, tripStudentDetail.getContactparent2());
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

    private void startGoogleMapApp(TripdetailData.Dailyroutestudentmastermodellist studentData) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", studentData.getLatitude(), studentData.getLongitude(), studentData.getParentsaddress1());
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

    private void setData() {
        mBinding.layoutshortDetail.layoutParentInfo.setItem(tripStudentDetail);
        mBinding.layoutshortDetail.layoutParentInfo.executePendingBindings();
        mBinding.layoutshortDetail.layoutSchoolInfo.setItem(tripStudentDetail);
        mBinding.layoutshortDetail.layoutSchoolInfo.executePendingBindings();
        if (tripStudentDetail.getStudentEquipment() != null && !tripStudentDetail.getStudentEquipment().isEmpty())
            mBinding.layoutshortDetail.layoutChildInfo.tvEquipmentValue.setText(tripStudentDetail.getStudentEquipment());
        else
            mBinding.layoutshortDetail.layoutChildInfo.tvEquipmentValue.setText(R.string.no_equipment);
        if (tripStudentDetail.getNotes() != null && !tripStudentDetail.getNotes().isEmpty())
            mBinding.layoutshortDetail.layoutChildInfo.tvNotesValue.setText(tripStudentDetail.getNotes());
        else
            mBinding.layoutshortDetail.layoutChildInfo.tvNotesValue.setText("-");

        mBinding.layoutshortDetail.layoutSchoolInfo.tvSchoolTimingValue.setText(tripStudentDetail.getSchoolStartTime()
                + " - " + tripStudentDetail.getSchoolEndTime());


        mBinding.layoutshortDetail.layoutSchoolInfo.layoutImgText.tvLetter.setText(tripStudentDetail.getSchoolname().substring(0, 2));
        if (tripStudentDetail.getTripstatus().equals(AppConstants.TRIP_ON_GOING)) {
            if (tripStudentDetail.getTriptype().equalsIgnoreCase(AppConstants.TRIP_PICKUP_TYPE) ||
                    tripStudentDetail.getTriptype().equalsIgnoreCase(AppConstants.TRIP_LUNCH_IN_TYPE)) {
                if (tripStudentDetail.getArrivalTime() != null) {
                    if (!tripStudentDetail.getIspickup() && !tripStudentDetail.getIsabsent() && !tripStudentDetail.getIsParentDropPickup()) {
                        mBinding.layoutshortDetail.tvStatus.setVisibility(View.GONE);
                        mBinding.layoutshortDetail.tvParentStatus.setVisibility(View.VISIBLE);
                        mBinding.layoutshortDetail.layoutButtons.getRoot().setVisibility(View.VISIBLE);
                        mBinding.layoutshortDetail.layoutButtons.negativeButton.setText(getString(R.string.absent));
                        mBinding.layoutshortDetail.layoutButtons.negativeButton.setVisibility(View.VISIBLE);
                        mBinding.layoutshortDetail.layoutButtons.positiveButton.setText(getString(R.string.pickup));
                        mBinding.layoutshortDetail.tvParentStatus.setText(getString(R.string.parent_drop));
                    } else {
                        mBinding.layoutshortDetail.tvStatus.setVisibility(View.VISIBLE);
                        mBinding.layoutshortDetail.tvParentStatus.setVisibility(View.GONE);
                        mBinding.layoutshortDetail.layoutButtons.getRoot().setVisibility(View.GONE);
                        if (tripStudentDetail.getIsParentDropPickup()) {
                            mBinding.layoutshortDetail.tvStatus.setText(getString(R.string.parent_drop_message));
                        } else if (tripStudentDetail.getIspickup()) {
                            mBinding.layoutshortDetail.tvStatus.setText(getString(R.string.pickup_message));
                        } else {
                            mBinding.layoutshortDetail.tvStatus.setText(getString(R.string.absent_message));
                            mBinding.layoutshortDetail.tvParentStatus.setVisibility(View.GONE);
                        }
                    }
                } else {
                    mBinding.layoutshortDetail.layoutButtons.negativeButton.setVisibility(View.GONE);
                    mBinding.layoutshortDetail.tvStatus.setVisibility(View.GONE);
                    mBinding.layoutshortDetail.tvParentStatus.setVisibility(View.GONE);
                    mBinding.layoutshortDetail.layoutButtons.getRoot().setVisibility(View.VISIBLE);
                    mBinding.layoutshortDetail.layoutButtons.positiveButton.setText(getString(R.string.arrive));
                }
            } else if (tripStudentDetail.getTriptype().equalsIgnoreCase(AppConstants.TRIP_DROP_TYPE) ||
                    tripStudentDetail.getTriptype().equalsIgnoreCase(AppConstants.TRIP_PICKUP_LUNCH_TYPE)) {
                if (!tripStudentDetail.getIsdrop() && !tripStudentDetail.getIsParentDropPickup()) {
                    mBinding.layoutshortDetail.tvStatus.setVisibility(View.GONE);
                    mBinding.layoutshortDetail.tvParentStatus.setVisibility(View.VISIBLE);
                    mBinding.layoutshortDetail.layoutButtons.getRoot().setVisibility(View.VISIBLE);
                    mBinding.layoutshortDetail.layoutButtons.positiveButton.setText(getString(R.string.str_drop));
                    mBinding.layoutshortDetail.layoutButtons.negativeButton.setText(getString(R.string.absent));
                    mBinding.layoutshortDetail.tvParentStatus.setText(getString(R.string.parent_pickup));
                } else {
                    if (tripStudentDetail.getIsParentDropPickup()) {
                        mBinding.layoutshortDetail.tvStatus.setText(getString(R.string.parent_pickup_message));
                    } else {
                        mBinding.layoutshortDetail.tvStatus.setText(getString(R.string.drop_message));
                    }
                    mBinding.layoutshortDetail.tvStatus.setVisibility(View.VISIBLE);
                    mBinding.layoutshortDetail.tvParentStatus.setVisibility(View.GONE);
                    mBinding.layoutshortDetail.layoutButtons.getRoot().setVisibility(View.GONE);

                }
            }
        } else {
            mBinding.layoutshortDetail.tvStatus.setVisibility(View.VISIBLE);
            mBinding.layoutshortDetail.layoutButtons.getRoot().setVisibility(View.GONE);
            mBinding.layoutshortDetail.tvStatus.setText(tripStudentDetail.getTripstatus()
                    .equals(AppConstants.TRIP_READY_TO_GO) ? getString(R.string.label_start_trip) :
                    getString(R.string.label_Complete_trip));
        }


        mBinding.layoutshortDetail.layoutButtons.negativeButton.setOnClickListener(view -> {
            studentStatus = STUDENT_ABSENT;
            if (PermissionUtils.hasPermissions(mContext, PermissionUtils.locationPermissions)) {
                getLocation();
                AddAbsentDialog absentDialog = new AddAbsentDialog();
                absentDialog.setListener(this);
                absentDialog.show(getActivity().getSupportFragmentManager(), AddAbsentDialog.class.getName());
            } else {
                getLocation();
            }
        });
        mBinding.layoutshortDetail.tvParentStatus.setOnClickListener(view -> {
            studentStatus = PARENT_DROP_AND_PICKUP;
            callApi();
        });

        mBinding.layoutshortDetail.layoutButtons.positiveButton.setOnClickListener(view -> {
            if (tripStudentDetail.getTriptype().equalsIgnoreCase(AppConstants.TRIP_PICKUP_TYPE)
                    || tripStudentDetail.getTriptype().equalsIgnoreCase(AppConstants.TRIP_LUNCH_IN_TYPE)) {
                studentStatus = (tripStudentDetail.getArrivalTime() != null) ? STUDENT_PICKUP : DRIVER_ARRIVED;
            } else
                studentStatus = STUDENT_DROP;
            if (lat == 0 || lng == 0) {
                buildLocationSettingsRequest();
                isFromAPI = true;
            } else {
                callApi();
            }
        });
    }

    private void callApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("DailyRouteStudentID", String.valueOf(tripStudentDetail.getDailyroutestudentid()));
        params.put("StudentStatus", String.valueOf(studentStatus));
        params.put("StudentID", String.valueOf(tripStudentDetail.getStudentid()));
        params.put("Latitude", String.valueOf(lat));
        params.put("Longitude", String.valueOf(lng));
        params.put("UserID", String.valueOf(session.getUserDetail().getUserid()));
        params.put("UserType", session.getUserDetail().getUsertype());
        if (studentStatus == STUDENT_ABSENT)
            params.put("AbsentReason", message);

        try {
            Retrofit.with(mContext)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_CHANGE_STUDENT_STATUS, session))
                    .setHeaderCallBackListener(new JSONCallback(mContext, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    if (studentStatus != DRIVER_ARRIVED && mOnStudentStatusUpdatedListener != null) {
                                        mOnStudentStatusUpdatedListener.onStatusUpdated(studentStatus, position, isLast);
                                    }
                                    changeStatus(jsonObject.optString("Message"));

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

    private void changeStatus(String message) {
        switch (studentStatus) {
            case DRIVER_ARRIVED:
                tripStudentDetail.setArrivalTime(String.valueOf(System.currentTimeMillis()));
                setData();
                break;
            case STUDENT_PICKUP:
                showShortToast(message);
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case STUDENT_ABSENT:
                showShortToast(message);
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case PARENT_DROP_AND_PICKUP:
                showShortToast(message);
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case STUDENT_DROP:
                showShortToast(message);
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }


    public void getLocation() {
        if (PermissionUtils.hasPermissions(mContext, PermissionUtils.locationPermissions)) {
            getCurrentLocation();
        } else {
            requestPermissions(PermissionUtils.locationPermissions, PermissionUtils.ACCESS_LOCATION_CODE);
        }
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
                //Log.e(TAG, "All location settings are satisfied.");
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

    private void getCurrentLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

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
    public void onItemSelected(String message) {
        this.message = message;
        callApi();
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
}
