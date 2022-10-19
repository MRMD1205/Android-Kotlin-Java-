package com.dynasty.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dynasty.R
import com.dynasty.adapter.BusinessAdapter
import com.dynasty.base.BaseActivity
import com.dynasty.base.BaseFragment
import com.dynasty.interfaces.OnLoadMoreListener
import com.dynasty.model.BusinessListModel
import com.dynasty.util.CATEGORY_FRAGMENT
import com.dynasty.util.PAGE_SIZE
import com.dynasty.util.SEARCH_CATEGORY_FRAGMENT
import com.dynasty.util.Utils
import com.dynasty.webservice.APIs
import com.dynasty.webservice.JSONCallback
import com.dynasty.webservice.Retrofit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_business.*
import kotlinx.android.synthetic.main.fragment_business.view.*
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [BusinessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BusinessFragment(categoryId: String) : BaseFragment() {

    private lateinit var mAdapter: BusinessAdapter
    private var businessList: ArrayList<BusinessListModel.Data> = ArrayList()

    //    private var finalBusinessList: ArrayList<BusinessListModel.Data> = ArrayList()
    var categoryId: String = ""
    private lateinit var searchBusinessFragment: SearchBusinessFragment

    private var root: View? = null   // create a global variable which will hold your layout

    companion object {
        var totalItemCount = 0
    }

    init {
        this.categoryId = categoryId
    }
    private var layoutManager: LinearLayoutManager? = null
    private var loading = false
    var visibleItemCount: Int = 0
    var pastVisibleItems: Int = 0
    var limit: Int = 0
    var pageIndex = 1
    var offset = 0
    var fromList: Boolean = false

    override fun setContentView(): Int {
        TODO("Not yet implemented")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(root == null) {
            root = inflater.inflate(R.layout.fragment_business, container, false)
            // setBusinessAdapter(finalBusinessList)
            pageIndex = 1
        }
        val categoryImg: ImageView = requireActivity().findViewById<View>(R.id.ivCategory) as ImageView
        categoryImg.setImageResource(R.drawable.ic_home)
        val businessImg: ImageView = requireActivity().findViewById<View>(R.id.ivBusiness) as ImageView
        businessImg.setImageResource(R.drawable.ic_business_selected)
        if(isAdded) {
            return root
        }

        return root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        businessList = ArrayList()
        searchBusinessFragment = SearchBusinessFragment()
        if(categoryId.isNotEmpty()) {
            apiGetBusinessList(categoryId, false)
        }
        Utils.showNoInternet(requireContext())

    }

    private fun apiGetBusinessList(id: String, isRefreshed: Boolean) {
        if(!isRefreshed) {
            showProgressBar()
        }
        val param = HashMap<String, String>()
        param["PageNumber"] = pageIndex.toString()
        param["PageSize"] = PAGE_SIZE.toString()
        tvNoBusinessFound.visibility = View.GONE
        hideKeyboard(requireActivity())
        Log.e("Business ID = ", id)
        try {
            Retrofit.with(requireActivity()).setAPI(APIs.API_GET_BUSINESS_LIST+id)
                .setGetParameters(param).setCallBackListener(object : JSONCallback(requireActivity()) {
                    override fun onSuccess(statusCode: Int, jsonObject: JSONObject) {
                        try {
                            val modelType = object : TypeToken<ArrayList<BusinessListModel.Data>>() {}.type
                            hideProgressBar()
                            swipeToRefreshList.isRefreshing = false
                            if(jsonObject.optJSONArray("Data") != null) {
                                if(!loading) {
                                    //  finalBusinessList.clear()
                                    businessList.clear()
                                }
                                if(!loading) {
                                    businessList = Gson().fromJson(jsonObject.optJSONArray("Data").toString(), modelType)
                                } else {
                                    businessList.addAll(Gson().fromJson(jsonObject.optJSONArray("Data").toString(), modelType))
                                }
                                if(businessList.size > 0) {
                                    //  finalBusinessList.addAll(businessList)
                                    totalItemCount = if(!businessList[0].totalRecords.isNullOrEmpty()) {
                                        businessList[0].totalRecords?.toInt()!!
                                    } else {
                                        PAGE_SIZE
                                    }
                                    tvNoBusinessFound.visibility = View.GONE
                                    rvBusinessList.visibility = View.VISIBLE
                                    /*if(finalBusinessList.isNotEmpty()) {
                                        setBusinessAdapter(finalBusinessList)
                                    }*/
                                    if(businessList.isNotEmpty()) {
                                        setBusinessAdapter(businessList)
                                    }
                                    if(loading) {
                                        loading = false
                                    }
                                } else {
                                    tvNoBusinessFound.visibility = View.VISIBLE
                                    rvBusinessList.visibility = View.GONE
                                    swipeToRefreshList.isRefreshing = false
                                }
                            } else {
                                pageIndex = 1
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                    override fun onFailed(statusCode: Int, message: String) {
                        try {
                            hideProgressBar()
                            showShortToast(message)
                            swipeToRefreshList.isRefreshing = false
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
        } catch (e: Exception) {
            try {
                e.printStackTrace()
                swipeToRefreshList.isRefreshing = false
                hideProgressBar()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setBusinessAdapter(filterList: ArrayList<BusinessListModel.Data>) {
        if(!::mAdapter.isInitialized) {
            mAdapter = BusinessAdapter(requireActivity(), businessList, false)
            layoutManager = LinearLayoutManager(requireActivity())
            root!!.rvBusinessList.layoutManager = layoutManager
            root!!.rvBusinessList.adapter = mAdapter
        } else {
            mAdapter.updateData(filterList)
        }
    }

    override fun setListeners() {

        ivSearchBusiness.setOnClickListener {
            (context as BaseActivity).navigateToFragment(searchBusinessFragment, true, SEARCH_CATEGORY_FRAGMENT)
        }

        root!!.rvBusinessList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0) {
                    visibleItemCount = layoutManager!!.childCount
                    limit = layoutManager!!.itemCount
                    pastVisibleItems = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                    if(!loading) {
                        if(businessList.size < totalItemCount) {
                            if(visibleItemCount + pastVisibleItems >= limit) {
                                loading = true
                                pageIndex++
                                onLoadMoreListener.onLoadMore(pageIndex)
                                mAdapter.showLoading(true)
                            } else {
                                mAdapter.showLoading(true)
                            }
                        } else {
                            mAdapter.showLoading(false)
                        }
                    }
                }
            }
        })

        swipeToRefreshList.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                pageIndex = 1
                apiGetBusinessList(categoryId, true)
                swipeToRefreshList.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
            }
        )
    }

    var onLoadMoreListener: OnLoadMoreListener = object : OnLoadMoreListener {
        override fun onLoadMore(page: Int) {
            apiGetBusinessList(categoryId, true)
        }
    }
}
