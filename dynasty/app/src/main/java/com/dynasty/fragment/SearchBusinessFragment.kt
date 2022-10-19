package com.dynasty.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dynasty.R
import com.dynasty.activity.BusinessFilterActivity
import com.dynasty.adapter.SearchBusinessAdapter
import com.dynasty.base.BaseFragment
import com.dynasty.interfaces.OnLoadMoreListener
import com.dynasty.model.BusinessListModel
import com.dynasty.util.GpsTracker
import com.dynasty.util.SEARCH_PAGE_SIZE
import com.dynasty.util.Utils
import com.dynasty.webservice.APIs
import com.dynasty.webservice.JSONCallback
import com.dynasty.webservice.Retrofit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_search_business.*
import kotlinx.android.synthetic.main.fragment_search_business.view.*
import kotlinx.android.synthetic.main.search_business_layout.view.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [SearchBusinessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchBusinessFragment : BaseFragment() {

    private lateinit var mAdapter: SearchBusinessAdapter
    private var businessList: ArrayList<BusinessListModel.Data> = ArrayList()
    var businessNameList: ArrayList<String> = ArrayList()
    private var root: View? = null   // create a global variable which will hold your layout

    private var layoutManager: LinearLayoutManager? = null
    private var loading = false
    var visibleItemCount: Int = 0
    var pastVisibleItems: Int = 0
    var limit: Int = 0
    var pageIndex = 1
    private var gpsTracker: GpsTracker? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private var selectedTagList = ArrayList<Int>()
    private var selectedCategoryList = ArrayList<Int>()
    private var radius: Int = 0
    var sb = java.lang.StringBuilder()

    companion object {
        var totalItemCount = 0
    }

    override fun setContentView(): Int {
        TODO("Not yet implemented")
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        businessList = ArrayList()
        gpsTracker = GpsTracker(requireActivity())
        if (gpsTracker!!.canGetLocation()) {
            latitude = gpsTracker!!.latitude
            longitude = gpsTracker!!.longitude
        } else {
            gpsTracker!!.showSettingsAlert()
        }
        apiBusinessList(
            "",
            false,
            "0",
            "0",
            listToString(selectedTagList)!!,
            listToString(selectedCategoryList)!!
        )
        Utils.showNoInternet(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_search_business, container, false)
            pageIndex = 1
        }
        val categoryImg: ImageView =
            requireActivity().findViewById<View>(R.id.ivCategory) as ImageView
        categoryImg.setImageResource(R.drawable.ic_home)
        val businessImg: ImageView =
            requireActivity().findViewById<View>(R.id.ivBusiness) as ImageView
        businessImg.setImageResource(R.drawable.ic_business_selected)
        if (isAdded) {
            return root
        }
        return root
    }

    override fun setListeners() {

        root!!.rvSearchBusiness.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = layoutManager!!.childCount
                    limit = layoutManager!!.itemCount
                    pastVisibleItems = layoutManager!!.findFirstVisibleItemPosition()
                    if (!loading) {
                        if (businessList.size < totalItemCount) {
                            if (visibleItemCount + pastVisibleItems >= limit) {
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

        layout_search_business.imgFilter.setOnClickListener {
            val intent = Intent(requireContext(), BusinessFilterActivity::class.java)
            intent.putIntegerArrayListExtra("catData", selectedCategoryList)
            intent.putIntegerArrayListExtra("tagData", selectedTagList)
            intent.putExtra("radius", radius)
            startActivityForResult(intent, 100)
        }

        layout_search_business.etSearchBusiness.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().isEmpty()) {
                    pageIndex = 1
                    var searchedString = layout_search_business.etSearchBusiness.text.toString()
                    if (radius == 0) {
                        apiBusinessList(
                            searchedString,
                            true,
                            "0",
                            "0",
                            listToString(selectedTagList)!!,
                            listToString(selectedCategoryList)!!
                        )
                    } else {
                        apiBusinessList(
                            searchedString,
                            false,
                            latitude.toString(),
                            longitude.toString(),
                            listToString(selectedTagList)!!,
                            listToString(selectedCategoryList)!!
                        )
                    }

                }
            }
        })
    }


    private fun listToString(numbers: List<Int>): String? {
        val buffer = StringBuilder()
        var isFirstTime = true
        for (nextNumber in numbers) {
            if (!isFirstTime) {
                buffer.append(",")
            }
            isFirstTime = false

            //-- Rest of the code here:
            buffer.append(nextNumber)
        }
        return buffer.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            if (data.hasExtra("tagData"))
                selectedTagList = data.getIntegerArrayListExtra("tagData")!!
            if (data.hasExtra("catData"))
                selectedCategoryList = data.getIntegerArrayListExtra("catData")!!
            if (data.hasExtra("radius"))
                radius = data.getIntExtra("radius", 0)

            pageIndex = 1
            if (radius == 0) {
                apiBusinessList(
                    "",
                    true,
                    "0",
                    "0",
                    listToString(selectedTagList)!!,
                    listToString(selectedCategoryList)!!
                )
            } else {
                apiBusinessList(
                    layout_search_business.etSearchBusiness.text.toString(),
                    true,
                    latitude.toString(),
                    longitude.toString(),
                    listToString(selectedTagList)!!,
                    listToString(selectedCategoryList)!!
                )
            }
        }
    }


    private fun apiBusinessList(
        searchText: String,
        isProgressNeeded: Boolean,
        currentLat: String,
        currentLong: String,
        taglist: String,
        categoryList: String
    ) {
        if (!isProgressNeeded) {
            showProgressBar()
        }
        val param = HashMap<String, String>()
        param["SearchedString"] = searchText
        param["SearchedTags"] = taglist
        param["SearchedCategory"] = categoryList
        param["LocationRadius"] = radius.toString()
        param["CurrentLat"] = currentLat
        param["CurrentLong"] = currentLong
        param["PageNumber"] = pageIndex.toString()
        param["PageSize"] = SEARCH_PAGE_SIZE.toString()
        try {
            Retrofit.with(requireActivity()).setAPI(APIs.API_SEARCH_BUSINESS_LIST)
                .setParameters(param).setCallBackListener(object : JSONCallback(requireActivity()) {
                    override fun onSuccess(statusCode: Int, jsonObject: JSONObject) {
                        try {
                            val modelType =
                                object : TypeToken<ArrayList<BusinessListModel.Data>>() {}.type
                            hideProgressBar()
                            if (jsonObject.optJSONArray("Data") != null) {
                                if (!loading) {
                                    businessList.clear()
                                }
                                if (!loading) {
                                    businessList = Gson().fromJson(
                                        jsonObject.optJSONArray("Data").toString(),
                                        modelType
                                    )
                                } else {
                                    businessList.addAll(
                                        Gson().fromJson(
                                            jsonObject.optJSONArray("Data").toString(), modelType
                                        )
                                    )
                                }
                                if (businessList.size > 0) {
                                    totalItemCount =
                                        if (!businessList[0].totalRecords.isNullOrEmpty()) {
                                            businessList[0].totalRecords?.toInt()!!
                                        } else {
                                            SEARCH_PAGE_SIZE
                                        }
                                    tvNoBusinessSearchFound.visibility = View.GONE
                                    rvSearchBusiness.visibility = View.VISIBLE
                                    if (businessList.isNotEmpty()) {
                                        setBusinessAdapter(businessList)
                                    }
                                    if (loading) {
                                        loading = false
                                    }
                                } else {
                                    rvSearchBusiness.visibility = View.GONE
                                    tvNoBusinessSearchFound.visibility = View.VISIBLE
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
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
        } catch (e: Exception) {
            try {
                e.printStackTrace()
                hideProgressBar()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setBusinessAdapter(filterList: ArrayList<BusinessListModel.Data>) {
        if (!::mAdapter.isInitialized) {
            mAdapter = SearchBusinessAdapter(requireActivity(), businessList)
            layoutManager = LinearLayoutManager(requireActivity())
            root!!.rvSearchBusiness.layoutManager = layoutManager
            root!!.rvSearchBusiness.adapter = mAdapter
        } else {
            mAdapter.updateData(filterList)
        }
    }

    var onLoadMoreListener: OnLoadMoreListener = object : OnLoadMoreListener {
        override fun onLoadMore(page: Int) {
            if (radius == 0) {
                apiBusinessList(
                    layout_search_business.etSearchBusiness.text.toString(),
                    true,
                    "0",
                    "0",
                    listToString(selectedTagList)!!,
                    listToString(selectedCategoryList)!!
                )
            } else {
                apiBusinessList(
                    layout_search_business.etSearchBusiness.text.toString(),
                    true,
                    latitude.toString(),
                    longitude.toString(),
                    listToString(selectedTagList)!!,
                    listToString(selectedCategoryList)!!
                )
            }
        }
    }
}