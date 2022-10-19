package com.courtesycarsredhill.ui.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.adapters.TripListAdapter;
import com.courtesycarsredhill.databinding.FragmentParentHomeBinding;
import com.courtesycarsredhill.interfaces.OnItemSelected;
import com.courtesycarsredhill.model.TripListData;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.ui.base.BaseActivity;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Logger;
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
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements OnItemSelected<TripListData>, TripListAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentParentHomeBinding mBinding;
    TripListAdapter mAdapter;
    ArrayList<TripListData> tripListDataList = new ArrayList<>();
    private Boolean sosSuccess = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.toolbar.ivSosButton.setVisibility(View.VISIBLE);
        setupToolBarWithMenu(mBinding.toolbar.toolbar, AppConstants.HOME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_home, container, false);
        inIt();
        onClick();
        return mBinding.getRoot();
    }

    private void onClick() {
        mBinding.toolbar.ivSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOSDialog();
            }
        });
    }

    private void inIt() {
        callApi();
        mBinding.layoutRecyclerView.pullToRefresh.setOnRefreshListener(() -> {
            mBinding.layoutRecyclerView.pullToRefresh.setRefreshing(false);
            callApi();
        });
    }

    private void setAdapter(ArrayList<TripListData> tripListDataList) {
        mAdapter = new TripListAdapter(mContext, this, tripListDataList, false, this);
        mBinding.layoutRecyclerView.recyclerView.setAdapter(mAdapter);
    }

    private void callApi() {

        HashMap<String, String> params = new HashMap<>();
        params.put("UserID", String.valueOf(session.getUserDetail().getUserid()));
      /*  if (((BaseActivity) mActivity).isParent())
            params.put("UserID", String.valueOf(2));
        else params.put("UserID", String.valueOf(8));*/

        params.put("UserType", session.getUserDetail().getUsertype());
        params.put("IsTodayTrip", String.valueOf(true));

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
                                        mBinding.layoutRecyclerView.recyclerView.setVisibility(View.VISIBLE);
                                        mBinding.layoutRecyclerView.txtNoReview.setVisibility(View.GONE);
                                        setAdapter(tripListDataList);
                                    } else {
                                        mBinding.layoutRecyclerView.recyclerView.setVisibility(View.GONE);
                                        mBinding.layoutRecyclerView.txtNoReview.setVisibility(View.VISIBLE);
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

    @Override
    public void onItemSelected(TripListData tripData) {
        if (((BaseActivity) mActivity).isParent()) {
            ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, ParentTripDetailFragment.newInstance(tripData, false), false, true);

        } else {
            ((NavigationMainActivity) getActivity()).replaceFragment(AppConstants.NAVIGATION_KEY, TripDetailFragment.newInstance(tripData, false), false, true);

        }

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

    @Override
    public void onClick(View view, int position) {

    }
}
