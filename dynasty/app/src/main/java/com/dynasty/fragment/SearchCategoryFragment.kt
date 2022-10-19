package com.dynasty.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.dynasty.R
import com.dynasty.adapter.SearchCategoryAdapter
import com.dynasty.base.BaseFragment
import com.dynasty.model.CategoryListModel
import com.dynasty.util.Utils
import com.dynasty.webservice.APIs
import com.dynasty.webservice.JSONCallback
import com.dynasty.webservice.Retrofit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_search_category.*
import kotlinx.android.synthetic.main.search_category_layout.view.*
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [SearchCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchCategoryFragment : BaseFragment() {

    private lateinit var mAdapter: SearchCategoryAdapter
    private var categoryList = ArrayList<CategoryListModel>()
    var categoryNameList: ArrayList<String> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_search_category

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        apiGetCategoryList()
        Utils.showNoInternet(requireContext())
        val categoryImg: ImageView = requireActivity().findViewById<View>(R.id.ivCategory) as ImageView
        categoryImg.setImageResource(R.drawable.ic_home_selected)
        val businessImg: ImageView = requireActivity().findViewById<View>(R.id.ivBusiness) as ImageView
        businessImg.setImageResource(R.drawable.ic_business)
    }

    private fun apiGetCategoryList() {
        categoryList = ArrayList()
        val param = HashMap<String, String>()
        param[""] = ""
        hideKeyboard(requireActivity())
        try {
            Retrofit.with(requireActivity()).setAPI(APIs.API_GET_CATEGORY_LIST).setGetParameters(param).setCallBackListener(object : JSONCallback(requireActivity(), showProgressBar()) {
                override fun onSuccess(statusCode: Int, jsonObject: JSONObject) {
                    try {
                        val modelType = object : TypeToken<ArrayList<CategoryListModel>>() {}.type
                        hideProgressBar()
                        if(jsonObject.optJSONArray("Data") != null) {
                            categoryList = Gson().fromJson(jsonObject.optJSONArray("Data").toString(), modelType)
                            setCategoryAdapter()
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

    private fun setCategoryAdapter() {
        mAdapter = SearchCategoryAdapter(requireActivity(), categoryList)
        rvSearchCategory.adapter = mAdapter
    }

    override fun setListeners() {

        layout_search_home.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mAdapter.filterList(categoryList)
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mAdapter.filter?.filter(layout_search_home.etSearch.text.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

}