package com.courtesycarsredhill.ui.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.FragmentMapBinding;
import com.courtesycarsredhill.databinding.LayoutInfoWindowBinding;
import com.courtesycarsredhill.model.DirectionModel;
import com.courtesycarsredhill.model.TripdetailData;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.util.PermissionUtils;
import com.courtesycarsredhill.util.Utils;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.JSONCallbackForRoute;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Boolean sosSuccess = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentMapBinding mBinding;

    private final static int REQUEST_PERMISSIONS_LOCATION = 12;
    private GoogleMap mMap;

    SupportMapFragment mapFragment;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private TripdetailData studendetailData;
    private String tripId;
    private ArrayList<LatLng> studentLatLongList = new ArrayList<>();
    ArrayList<LatLng> allLatLongList = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private LatLng mSourceLocation = new LatLng(51.509865, -0.118092);
    private static final int DEFAULT_ZOOM = 11;


    Marker agentMarker;
    LatLngBounds.Builder builder;

    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment newInstance(String studendetailData) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("TRIP_ID", studendetailData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tripId = getArguments().getString("TRIP_ID");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (PermissionUtils.hasPermissions(mContext, PermissionUtils.locationPermissions)) {
            callDetailAPI();
        } else {
            requestPermissions(PermissionUtils.locationPermissions, PermissionUtils.ACCESS_LOCATION_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mBinding.toolbar.ivSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOSDialog();
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();
            // add markers from database to the map
        }
    }

    private void callDetailAPI() {
        HashMap<String, String> params = new HashMap<>();
        params.put("DailyRouteTripID", tripId);
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
                                        studendetailData.getDailyRouteStudentMasterModelList().get(i).setTriptype(studendetailData.getTripType());
                                        studendetailData.getDailyRouteStudentMasterModelList().get(i).setTripstatus(studendetailData.getTripStatus());
                                    }
                                    if (studendetailData != null) {
                                        setupToolBarWithBackArrow(mBinding.toolbar.toolbar, studendetailData.getTripName());
                                        setMap();
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

    private void setMap() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (studendetailData != null && studendetailData.getDailyRouteStudentMasterModelList() != null) {
                for (int i = 0; i < studendetailData.getDailyRouteStudentMasterModelList().size(); i++) {
                    studentLatLongList.add(new LatLng(studendetailData.getDailyRouteStudentMasterModelList().get(i).getLatitude(),
                            studendetailData.getDailyRouteStudentMasterModelList().get(i).getLongitude()));
                }
                for (int i = 0; i < studentLatLongList.size(); i++) {

                    // NOTE this code is to show dynamic text on marker
                    IconGenerator icg = new IconGenerator(getContext());
                    if (studendetailData.getDailyRouteStudentMasterModelList().get(i).getIsNext())
                        icg.setColor(ContextCompat.getColor(getContext(), R.color.red));
                    else
                        icg.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

                    icg.setTextAppearance(R.style.BlackText);
                    Bitmap bm = icg.makeIcon(studendetailData.getDailyRouteStudentMasterModelList().get(i).getPickupOrderNo());

                    Marker marker = mMap.addMarker(new MarkerOptions().position(studentLatLongList.get(i))
                            .icon(BitmapDescriptorFactory.fromBitmap(bm))
                            .title(studendetailData.getDailyRouteStudentMasterModelList().get(i).getParentName1()).snippet("b"));

                    //set trip type and trip status in student list
                    studendetailData.getDailyRouteStudentMasterModelList().get(i).setTriptype(studendetailData.getTripType());
                    studendetailData.getDailyRouteStudentMasterModelList().get(i).setTripstatus(studendetailData.getTripStatus());
                    marker.setTag(studendetailData.getDailyRouteStudentMasterModelList().get(i));
                    markerList.add(marker);
                }
                mMap.setInfoWindowAdapter(new MyInfoWindow(studendetailData));
            }

          /* Code to Add Only Poly Line

           PolylineOptions polylineOptions = new PolylineOptions();
            // Create polyline options with existing LatLng ArrayList
            polylineOptions.addAll(studentLatLongList);
            polylineOptions
                    .width(10)
                    .color(Color.BLACK);
            mMap.addPolyline(polylineOptions);*/

            // Adding multiple points in map using polyline and arraylist
            mSourceLocation = new LatLng(studendetailData.getSourceLat(), studendetailData.getSourceLang());
            LatLng schoolMArkerPosition;
            if (studendetailData.getSourceAddress().equalsIgnoreCase(studendetailData.getSchoolAddress())) {
                schoolMArkerPosition = mSourceLocation;
            } else {
                schoolMArkerPosition = new LatLng(studendetailData.getDestinationLat(), studendetailData.getDestinationLang());
            }
            Marker schoolMarker = mMap.addMarker(new MarkerOptions()
                    .position(schoolMArkerPosition)
                    .title(studendetailData.getSchoolName())
                    .icon(Utils.BitmapDescriptorFromVector(mContext, R.drawable.ic_school)).snippet("a"));
            markerList.add(schoolMarker);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markerList) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 100; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            mMap.animateCamera(cu);
            markerList.clear();
            /*CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    mSourceLocation).zoom(DEFAULT_ZOOM).build();*/
            // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.setOnInfoWindowClickListener(this);

            getDirectionsUrl(mSourceLocation, new LatLng(studendetailData.getDestinationLat(), studendetailData.getDestinationLang()));

            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            if (PermissionUtils.hasPermissions(mContext, PermissionUtils.locationPermissions)) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                requestPermissions(PermissionUtils.locationPermissions, PermissionUtils.ACCESS_LOCATION_CODE);
            }
//            PermissionUtils.isCheck(mContext, PermissionUtils.locationPermissions, PermissionUtils.ACCESS_LOCATION_CODE, permissionListener);

            /*if (hasAppPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                //  getCurrentLocation();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }*/

            clearAllListData();
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void clearAllListData() {
        studentLatLongList.clear();
    }

    private void getDirectionsUrl(LatLng origin, LatLng dest) {

// Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

// Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

// Sensor enabled
        String sensor = "sensor=false";

// Waypoints
        String waypoints = "";
        for (int i = 0; i < studentLatLongList.size(); i++) {
            LatLng point = (LatLng) studentLatLongList.get(i);
            if (i == 0)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }
        String mode = "mode=driving";

        String key = "key=" + AppConstants.GOOGLE_PLACE_API_KEY;
// Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints + "&" + mode + "&" + key;

// Output format
        String output = "json";

// Building the url to the web service
        String url = APIs.GOOGLE_DIRECTION_API + output + "?" + parameters;
        Log.e("ROUTE API", url);

        try {
            Retrofit.with(mContext)
                    .setUrl(APIs.GOOGLE_DIRECTION_API)
                    .setRouteAPI(ResponseUtils.getRequestAPIURL(output + "?" + parameters, session))
                    .setCallBackListenerForRoute(new JSONCallbackForRoute(mContext, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            try {
                                Gson gson = new Gson();
                                DirectionModel directionModel = gson.fromJson(jsonObject.toString(),
                                        new TypeToken<DirectionModel>() {
                                        }.getType());

                                if (directionModel != null && directionModel.getStatus().equalsIgnoreCase("OK")
                                        && directionModel.getRoutes() != null && directionModel.getRoutes().size() > 0) {

                                    ArrayList points = new ArrayList();

                                    List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
                                    /** Traversing all routes */
                                    for (int i = 0; i < directionModel.getRoutes().size(); i++) {
                                        List path = new ArrayList<HashMap<String, String>>();

                                        for (int j = 0; j < directionModel.getRoutes().get(i).getLegs().size(); j++) {
                                            for (int k = 0; k < directionModel.getRoutes().get(i).getLegs().get(j).getSteps().size(); k++) {

                                                String polyline = "";

                                                polyline = (directionModel.getRoutes().get(i).getLegs().get(j).getSteps().get(k).getPolyline().getPoints());
                                                List<LatLng> list = decodePoly(polyline);

                                                /** Traversing all points */
                                                for (int l = 0; l < list.size(); l++) {
                                                    HashMap<String, String> hm = new HashMap<String, String>();
                                                    hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                                                    hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                                                    path.add(hm);
                                                }
                                            }
                                            routes.add(path);
                                        }
                                    }
                                    PolylineOptions lineOptions = new PolylineOptions();

                                    for (int i = 0; i < routes.size(); i++) {
                                        points = new ArrayList<LatLng>();
                                        lineOptions = new PolylineOptions();

                                        // Fetching i-th route
                                        List<HashMap<String, String>> path = routes.get(i);

                                        // Fetching all the points in i-th route
                                        for (int j = 0; j < path.size(); j++) {
                                            HashMap<String, String> point = path.get(j);

                                            double lat = Double.parseDouble(point.get("lat"));
                                            double lng = Double.parseDouble(point.get("lng"));
                                            LatLng position = new LatLng(lat, lng);

                                            points.add(position);
                                        }

                                        // Adding all the points in the route to LineOptions
                                        lineOptions.addAll(points);
                                        lineOptions.width(10);
                                        lineOptions.color(Color.RED);

                                    }
                                    mMap.addPolyline(lineOptions);

                                } else {
                                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            hideProgressBar();
                            showShortToast(message);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            hideProgressBar();
        }
        //    return url;
    }

    /**
     * Method to decode polyline points
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    /*private void getLocationPermission() {
        requestAppPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_LOCATION, new setPermissionListener() {
            @Override
            public void onPermissionGranted(int requestCode) {
                //getCurrentLocation();
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
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        @Override
        public void onDeny(int requestCode) {
            PermissionUtils.showPermissionAllowDialog(mContext, false, true);
        }

        @Override
        public void onDenyNeverAskAgain(int requestCode) {
            PermissionUtils.showPermissionAllowDialog(mContext, true, true);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(mActivity, requestCode, permissions, grantResults, permissionListener);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTitle().equalsIgnoreCase(studendetailData.getSchoolName())) return;

        TripdetailData.Dailyroutestudentmastermodellist tripDetail = (TripdetailData.Dailyroutestudentmastermodellist) marker.getTag();
        studentLatLongList.clear();
        // Todo: Set Position and iSLast if client want map again
        ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, ChildShortDetailFragment.newInstance(tripDetail,0,true), true, true);
    }


    class MyInfoWindow implements GoogleMap.InfoWindowAdapter {
        LayoutInfoWindowBinding mBindingWindow;
        TripdetailData locationDatas;

        MyInfoWindow(TripdetailData locationDatas) {
            mBindingWindow = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.layout_info_window, null, false);
            this.locationDatas = locationDatas;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            Log.e("getInfoWindow: ", marker.getTitle());
            Log.e("getInfoWindow: ", marker.getSnippet());

            if (marker.getSnippet().equalsIgnoreCase("b")) {
                TripdetailData.Dailyroutestudentmastermodellist studentData = (TripdetailData.Dailyroutestudentmastermodellist) marker.getTag();
                if (studentData != null) {
                    mBindingWindow.tvName.setText(studentData.getStudentFullName());
                    mBindingWindow.tvAddress.setText(studentData.getParentsaddress1());
                    mBindingWindow.layoutImgText.setText(studentData.getStudentName());
                    mBindingWindow.ivArrow.setVisibility(View.VISIBLE);
                    //  mBindingWindow.ivMap.setVisibility(View.VISIBLE);

                    if (studentData.getTriptype().equalsIgnoreCase(AppConstants.TRIP_PICKUP_TYPE) ||
                            studentData.getTriptype().equalsIgnoreCase(AppConstants.TRIP_LUNCH_IN_TYPE)) {
                        if (studentData.getNewPickupTime() != null && !studentData.getNewPickupTime().isEmpty()) {
                            mBindingWindow.tvEstimatedTime.setVisibility(View.VISIBLE);
                            mBindingWindow.tvEstimatedTime.setText("Estimated pickup time: " + studentData.getNewPickupTime());
                        } else {
                            mBindingWindow.tvEstimatedTime.setVisibility(View.GONE);
                        }
                    } else {
                        if (studentData.getNewDropTime() != null && !studentData.getNewDropTime().isEmpty()) {
                            mBindingWindow.tvEstimatedTime.setVisibility(View.VISIBLE);
                            mBindingWindow.tvEstimatedTime.setText("Estimated drop time: " + studentData.getNewDropTime());
                        } else {
                            mBindingWindow.tvEstimatedTime.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                mBindingWindow.layoutImgText.setText(locationDatas.getSchoolName().substring(0, 2));
                mBindingWindow.tvName.setText(locationDatas.getSchoolName());
                mBindingWindow.tvAddress.setText(locationDatas.getSchoolAddress());
                mBindingWindow.ivArrow.setVisibility(View.INVISIBLE);
            }
            return mBindingWindow.getRoot();
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        private void startGoogleMapApp(TripdetailData.Dailyroutestudentmastermodellist studentData) {
            String strUri = "http://maps.google.com/maps?q=loc:" + studentData.getLatitude() + "," + studentData.getLongitude() + " (" + "Label which you want" + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
