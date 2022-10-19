package com.courtesycarsredhill.ui.fragment;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.adapters.TripListAdapter;
import com.courtesycarsredhill.databinding.FragmentHistoryBinding;
import com.courtesycarsredhill.dialogs.FilterDialog;
import com.courtesycarsredhill.dialogs.SosDialog;
import com.courtesycarsredhill.interfaces.OnDismissedCall;
import com.courtesycarsredhill.interfaces.OnItemSelected;
import com.courtesycarsredhill.model.CheckListData;
import com.courtesycarsredhill.model.TripListData;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Logger;
import com.courtesycarsredhill.util.TimeStamp;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


public class HistoryFragment extends BaseFragment implements OnItemSelected<TripListData>, OnDismissedCall, TripListAdapter.ItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TripListAdapter mAdapter;
    private Boolean sosSuccess = false;

    FragmentHistoryBinding mBinding;
    int selectedMonth = TimeStamp.getCurrentMonth() + 1;
    int selectedYear = TimeStamp.getCurrentYear();
    ArrayList<TripListData> tripListDataList = new ArrayList<>();
    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public HistoryFragment() {
        // Required empty public constructor
    }


    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBarWithMenu(mBinding.toolbar.toolbar, AppConstants.HISTORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        inIt();
        return mBinding.getRoot();
    }


    private void inIt() {
        callApi();
        setHasOptionsMenu(true);

        mBinding.layoutHistory.pullToRefresh.setOnRefreshListener(() -> {
            mBinding.layoutHistory.pullToRefresh.setRefreshing(false);
            callApi();
        });

        mBinding.toolbar.ivSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOSDialog();
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

    private void callApi() {

        HashMap<String, String> params = new HashMap<>();
        params.put("UserID", String.valueOf(session.getUserDetail().getUserid()));
        /*if (((BaseActivity) mActivity).isParent()) {
            params.put("UserID", String.valueOf(2));
        } else {
            params.put("UserID", String.valueOf(8));
        }*/
        params.put("UserType", session.getUserDetail().getUsertype());
        params.put("IsTodayTrip", String.valueOf(false));
        params.put("Month", String.valueOf(selectedMonth));
        params.put("Year", String.valueOf(selectedYear));


        try {
            Retrofit.with(mContext)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setGetParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_GET_TRIP_LIST, session))
                    .setHeaderCallBackListener(new JSONCallback(mContext, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {

                                    Gson gson = new Gson();
                                    tripListDataList = gson.fromJson(jsonObject.optJSONArray("Data").toString(),
                                            new TypeToken<List<TripListData>>() {
                                            }.getType());

                                    Logger.e(tripListDataList.toString());
                                    if (tripListDataList != null && tripListDataList.size() > 0) {
                                        mBinding.layoutHistory.layoutEarning.getRoot().setVisibility(View.VISIBLE);
                                        mBinding.layoutHistory.recyclerView.setVisibility(View.VISIBLE);
                                        mBinding.layoutHistory.txtNoReview.setVisibility(View.GONE);
                                        setData();
                                    } else {
                                        mBinding.layoutHistory.layoutEarning.getRoot().setVisibility(View.GONE);
                                        mBinding.layoutHistory.recyclerView.setVisibility(View.GONE);
                                        mBinding.layoutHistory.txtNoReview.setVisibility(View.VISIBLE);
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

    private void setData() {
        mBinding.layoutHistory.layoutEarning.tvMonthalyEarnigValue.setText(getString(R.string.str_currency) + tripListDataList.get(0).getDriverMonthlyEarning());
        mBinding.layoutHistory.layoutEarning.tvTodayEarnigValue.setText(getString(R.string.str_currency) + tripListDataList.get(0).getDriverTodayEarning());
        if (((BaseActivity) mActivity).isParent()) {
            mBinding.layoutHistory.layoutEarning.getRoot().setVisibility(View.GONE);
        } else {
            mBinding.layoutHistory.layoutEarning.getRoot().setVisibility(View.VISIBLE);
        }
        setAdapter(tripListDataList);
        mBinding.layoutHistory.layoutEarning.tvSelectedMonth.setText(months[selectedMonth - 1].concat(" ").concat(String.valueOf(selectedYear)));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_calendar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.setListener(this);
        filterDialog.show(((NavigationMainActivity) getActivity()).getSupportFragmentManager(), FilterDialog.class.getName());
        return super.onOptionsItemSelected(item);
    }

    private void setAdapter(ArrayList<TripListData> tripListDataList) {
        mAdapter = new TripListAdapter(mContext, this, tripListDataList, true, this);
        mBinding.layoutHistory.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemSelected(TripListData tripData) {
        if (((BaseActivity) mActivity).isParent()) {
            ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, ParentTripDetailFragment.newInstance(tripData, true), false, true);

        } else
            ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, TripDetailFragment.newInstance(tripData, true), false, true);
    }

    @Override
    public void onDismissCalled(int month, int year) {
        selectedMonth = month;
        selectedYear = year;
        callApi();
    }

    @Override
    public void onEquipmentSelected(String action, ArrayList<CheckListData> values) {

    }

    @Override
    public void onFeedbackGiven(int val1, String val2) {

    }

    @Override
    public void onClick(View view, int position) {
        mBinding.layoutHistory.recyclerView.scrollTo(position, 0);
    }
}
