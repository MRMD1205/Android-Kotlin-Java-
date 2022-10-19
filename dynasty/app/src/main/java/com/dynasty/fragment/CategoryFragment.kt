package com.dynasty.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dynasty.R
import com.dynasty.adapter.CategoryAdapter
import com.dynasty.base.BaseActivity
import com.dynasty.base.BaseFragment
import com.dynasty.interfaces.OnLoadMoreListener
import com.dynasty.model.CategoryDataModel
import com.dynasty.model.CategoryListModel
import com.dynasty.util.SEARCH_CATEGORY_FRAGMENT
import com.dynasty.util.Utils
import com.dynasty.webservice.APIs.API_GET_CATEGORY_LIST
import com.dynasty.webservice.JSONCallback
import com.dynasty.webservice.Retrofit
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.common_toolbar_layout.view.*
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_category.view.*
import org.json.JSONObject
import java.io.Serializable


class CategoryFragment : BaseFragment(), Serializable {

    private lateinit var mAdapter: CategoryAdapter
    private var categoryList = ArrayList<CategoryListModel>()
    private lateinit var cd: CategoryDataModel
    private lateinit var searchCategoryFragment: SearchCategoryFragment


    private var root: View? = null   // create a global variable which will hold your layout

    companion object {
        var totalItemCount = 0
        const val pageSize: Int = 10
    }

    private var layoutManager: LinearLayoutManager? = null
    private var loading = false
    var visibleItemCount: Int = 0
    var pastVisibleItems: Int = 0
    var limit: Int = 0
    var pageIndex = 0
    var offset = 0

    override fun setContentView(): Int {
        TODO("Not yet implemented")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(root == null) {
            root = inflater.inflate(R.layout.fragment_category, container, false)
            setCategoryAdapter()
            pageIndex = 0
        }
        val categoryImg: ImageView = requireActivity().findViewById<View>(R.id.ivCategory) as ImageView
        categoryImg.setImageResource(R.drawable.ic_home_selected)
        val businessImg: ImageView = requireActivity().findViewById<View>(R.id.ivBusiness) as ImageView
        businessImg.setImageResource(R.drawable.ic_business)
        if(isAdded) {
            return root
        }
        return root
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        searchCategoryFragment = SearchCategoryFragment()
        apiGetCategoryList(false)
        Utils.showNoInternet(requireContext())
    }


    private fun apiGetCategoryList(isRefreshed: Boolean) {
        categoryList = ArrayList()
        if(!isRefreshed) {
            showProgressBar()
        } else {
            hideProgressBar()
        }
        val param = HashMap<String, String>()
        param[""] = ""
        hideKeyboard(requireActivity())
        try {
            Retrofit.with(requireActivity()).setAPI(API_GET_CATEGORY_LIST)
                .setGetParameters(param).setCallBackListener(object : JSONCallback(requireActivity()) {
                    override fun onSuccess(statusCode: Int, jsonObject: JSONObject) {
                        try {
                            swipeToRefreshCategoryList.isRefreshing = false
                            val modelType = object : TypeToken<ArrayList<CategoryListModel>>() {}.type
                            hideProgressBar()
                            if(jsonObject.optJSONArray("Data") != null) {
                                categoryList = Gson().fromJson(jsonObject.optJSONArray("Data").toString(), modelType)
                                setCategoryAdapter()

                            } else {
                                pageIndex = 0
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailed(statusCode: Int, message: String) {
                        try {
                            hideProgressBar()
                            swipeToRefreshCategoryList.isRefreshing = false
                            showShortToast(message)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
        } catch (e: Exception) {
            try {
                e.printStackTrace()
                swipeToRefreshCategoryList.isRefreshing = false
                hideProgressBar()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setCategoryAdapter() {
        mAdapter = CategoryAdapter(requireActivity(), categoryList, false)
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        root!!.rvCategoryList.layoutManager = layoutManager
        root!!.rvCategoryList.adapter = mAdapter
        root!!.rvCategoryList.scheduleLayoutAnimation()

    }

    override fun setListeners() {

        layout_toolbar_home.ivSearch.setOnClickListener {
            (context as BaseActivity).navigateToFragment(searchCategoryFragment, true, SEARCH_CATEGORY_FRAGMENT)
        }

        swipeToRefreshCategoryList.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                apiGetCategoryList(true)
                swipeToRefreshCategoryList.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
            }
        )

        root!!.rvCategoryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0) {
                    visibleItemCount = layoutManager!!.childCount
                    limit = layoutManager!!.itemCount
                    pastVisibleItems = layoutManager!!.findFirstVisibleItemPosition()
                    if(!loading) {
                        if(categoryList.size < totalItemCount) {
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
    }

    var onLoadMoreListener: OnLoadMoreListener = object : OnLoadMoreListener {
        override fun onLoadMore(page: Int) {

        }
    }
}