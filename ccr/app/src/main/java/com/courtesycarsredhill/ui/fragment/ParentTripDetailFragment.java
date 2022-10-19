package com.courtesycarsredhill.ui.fragment;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.FragmentParentDetailBinding;
import com.courtesycarsredhill.model.TripListData;
import com.courtesycarsredhill.model.TripdetailData;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.ui.base.BaseBinder;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.util.Utils;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;


public class ParentTripDetailFragment extends BaseFragment {


    FragmentParentDetailBinding mBinding;
    private TripListData tripData;
    private boolean isHistory = false;
    private TripdetailData studendetailData;
    private Boolean sosSuccess = false;

    public ParentTripDetailFragment() {
        // Required empty public constructor
    }


    public static ParentTripDetailFragment newInstance(TripListData tripData, boolean isHistory) {
        ParentTripDetailFragment fragment = new ParentTripDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("tripData", tripData);
        args.putBoolean("IS_HISTORY", isHistory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBarWithBackArrow(mBinding.toolbar.toolbar, tripData.getTripname());
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_detail, container, false);
        setHasOptionsMenu(true);
        mBinding.icLayoutTripDetail.getRoot().setVisibility(View.GONE);

        callApi();
        mBinding.toolbar.ivSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOSDialog();
            }
        });
        return mBinding.getRoot();
    }

    private void callApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("DailyRouteTripID", String.valueOf(tripData.getDailyroutetripid()));
        params.put("StudentID", String.valueOf(session.getUserDetail().getUserid()));
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
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.setBackgroundColor(mContext.getResources().getColor(R.color.yellow_ec));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.setTextColor(mContext.getResources().getColor(R.color.black));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTripType.setTextColor(mContext.getResources().getColor(R.color.black));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvStatus.setTextColor(mContext.getResources().getColor(R.color.black));

        } else if (studendetailData.getTripStatus().equals(AppConstants.TRIP_ON_GOING)) {
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.setBackgroundColor(mContext.getResources().getColor(R.color.sky));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.setTextColor(mContext.getResources().getColor(R.color.white));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTripType.setTextColor(mContext.getResources().getColor(R.color.white));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvStatus.setTextColor(mContext.getResources().getColor(R.color.white));
        } else if (studendetailData.getTripStatus().equals(AppConstants.TRIP_COMPLETED)) {
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTrip.setTextColor(mContext.getResources().getColor(R.color.white));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvStatus.setTextColor(mContext.getResources().getColor(R.color.white));
            mBinding.icLayoutTripDetail.layoutTripTiming.tvTripType.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        setDriverData();
        setEscortData();
        setHomeData();
        mBinding.icLayoutTripDetail.setItem(studendetailData);
        mBinding.icLayoutTripDetail.layoutTripTiming.setItem(studendetailData);
        mBinding.icLayoutTripDetail.layoutTripTiming.layoutSourceTime.setItem(studendetailData);
        mBinding.icLayoutTripDetail.layoutTripTiming.layoutSourceTime.executePendingBindings();

        mBinding.icLayoutTripDetail.layoutTripTiming.layoutDestinationTime.setItem(studendetailData);
        mBinding.icLayoutTripDetail.layoutTripTiming.layoutDestinationTime.executePendingBindings();
        if (((BaseActivity) getContext()).isParent()) {
            mBinding.icLayoutTripDetail.layoutTripTiming.layoutSourceTime.tvTime.setText(tripData.getNewPickupTime());
        } else {
            mBinding.icLayoutTripDetail.layoutTripTiming.layoutSourceTime.tvTime.setText(studendetailData.getArrivalTime());
        }
        mBinding.icLayoutTripDetail.layoutTripTiming.layoutDestinationTime.tvTime.setText(studendetailData.getLeaveTime());

        mBinding.executePendingBindings();


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
                    Utils.dialdNumber(mContext, studendetailData.getSchoolContactNo());
            }
        });
    }

    private void setEscortData() {
        if(this.studendetailData.getEscortName()!=null && this.studendetailData.getEscortID()!=null){
            mBinding.icLayoutTripDetail.layoutEscort.getRoot().setVisibility(View.VISIBLE);
            mBinding.icLayoutTripDetail.layoutEscort.tvTitle.setText(getString(R.string.str_escort));
            mBinding.icLayoutTripDetail.layoutEscort.tvName.setText(this.studendetailData.getEscortName());
            mBinding.icLayoutTripDetail.layoutEscort.tvAddress.setText(this.studendetailData.getEscortContactNo());
            mBinding.icLayoutTripDetail.layoutEscort.layoutImgText.ivUserImage.setVisibility(View.VISIBLE);
            mBinding.icLayoutTripDetail.layoutEscort.layoutImgText.tvLetter.setVisibility(View.INVISIBLE);
            BaseBinder.setImageUrl(mBinding.icLayoutTripDetail.layoutEscort.layoutImgText.ivUserImage, this.studendetailData.getEscortimg());
            mBinding.icLayoutTripDetail.layoutEscort.ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBinding.icLayoutTripDetail.layoutEscort.ivPhone.getVisibility() == View.VISIBLE)
                        Utils.dialdNumber(mContext, studendetailData.getEscortContactNo());
                }
            });
        }else {
            mBinding.icLayoutTripDetail.layoutEscort.getRoot().setVisibility(View.GONE);
        }

    }

    private void setDriverData() {
        mBinding.icLayoutTripDetail.layoutDriver.tvTitle.setText(getString(R.string.str_driver));
        mBinding.icLayoutTripDetail.layoutDriver.tvName.setText(studendetailData.getDriverName());
        mBinding.icLayoutTripDetail.layoutDriver.tvAddress.setText(studendetailData.getDriverContactNo());
        mBinding.icLayoutTripDetail.layoutDriver.layoutImgText.ivUserImage.setVisibility(View.VISIBLE);
        mBinding.icLayoutTripDetail.layoutDriver.layoutImgText.tvLetter.setVisibility(View.INVISIBLE);
        BaseBinder.setImageUrl(mBinding.icLayoutTripDetail.layoutDriver.layoutImgText.ivUserImage, studendetailData.getDriverimg());
        mBinding.icLayoutTripDetail.layoutDriver.ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.icLayoutTripDetail.layoutDriver.ivPhone.getVisibility() == View.VISIBLE)
                    Utils.dialdNumber(mContext, studendetailData.getDriverContactNo());
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

}
