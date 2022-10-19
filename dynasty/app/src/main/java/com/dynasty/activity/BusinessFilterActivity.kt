package com.dynasty.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dynasty.R
import com.dynasty.adapter.CategoryFilterListAdapter
import com.dynasty.adapter.TagFilterListAdapter
import com.dynasty.base.BaseActivity
import com.dynasty.model.CategoryListModel
import com.dynasty.model.TagListModel
import com.dynasty.util.GpsTracker
import com.dynasty.util.Utils
import com.dynasty.webservice.APIs
import com.dynasty.webservice.JSONCallback
import com.dynasty.webservice.Retrofit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_business_filter.*
import kotlinx.android.synthetic.main.toolbar_with_back.view.*
import org.json.JSONObject


class BusinessFilterActivity : BaseActivity() {

    private lateinit var mAdapter: CategoryFilterListAdapter
    private var categoryList = ArrayList<CategoryListModel>()
    private var mLayoutManager: LinearLayoutManager? = null
    private lateinit var nAdapter: TagFilterListAdapter
    private var tagList = ArrayList<TagListModel>()
    private var nLayoutManager: LinearLayoutManager? = null
    private val currentSelectedCategoryItems: ArrayList<CategoryListModel> = ArrayList()
    private val currentSelectedTagItems: ArrayList<TagListModel> = ArrayList()
    private var categoryListIsOpen: Boolean = false
    private var tagListIsOpen: Boolean = false
    private var seekbarPosition: Int = 0
    private var seekbarValue: Int = 2
    private var selectedTagList = ArrayList<Int>()
    private var selectedCategoryList = ArrayList<Int>()
    private var radius: Int = 2

    override fun getLayoutResId(): Int = R.layout.activity_business_filter

    override fun initViews() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            checkbox.isChecked = false
            seekbar.isEnabled = false
            seekbarValue = 0
            seekbar.setProgressTintList(ColorStateList.valueOf(resources.getColor(R.color.colorOrangeTransparent)));
            requestPermission()
        } else {
            checkbox.isChecked = true
            seekbar.isEnabled = true
            seekbarValue = 2
            seekbar.setProgressTintList(ColorStateList.valueOf(resources.getColor(R.color.colorAccent)));
        }
        if(intent.hasExtra("catData") && intent.getIntegerArrayListExtra("catData") != null)
            selectedCategoryList = intent.getIntegerArrayListExtra("catData")!!
        if(intent.hasExtra("tagData") && intent.getIntegerArrayListExtra("tagData") != null)
            selectedTagList = intent.getIntegerArrayListExtra("tagData")!!
        if(intent.hasExtra("radius") && intent.getIntExtra("radius", 0) != 0)
            radius = intent.getIntExtra("radius", 0)
        apiGetCategoryList()
        apiGetTagList()
        Utils.showNoInternet(this)
    }

    private fun requestPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    checkbox.isChecked = true
                    seekbar.isEnabled = true
                    seekbarValue = 2
                    seekbar.setProgressTintList(ColorStateList.valueOf(resources.getColor(R.color.colorAccent)));
                }

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    checkbox.isChecked = false
                    seekbar.isEnabled = false
                    seekbarValue = 0
                    seekbar.setProgressTintList(ColorStateList.valueOf(resources.getColor(R.color.colorOrangeTransparent)));
                    // check for permanent denial of permission
                    if(response.isPermanentlyDenied) {
                        showSettingsAlert()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(this)

        // Setting Dialog Title
        alertDialog.setTitle("Need Permission")

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    private fun apiGetCategoryList() {
        categoryList = ArrayList()
        categoryListIsOpen = true
        val param = HashMap<String, String>()
        param[""] = ""
        hideKeyboard(this)
        try {
            Retrofit.with(this)
                .setAPI(APIs.API_GET_CATEGORY_LIST)
                .setGetParameters(param)
                .setCallBackListener(object : JSONCallback(this, showProgressBar()) {
                    override fun onSuccess(statusCode: Int, jsonObject: JSONObject) {
                        try {
                            val modelType = object : TypeToken<ArrayList<CategoryListModel>>() {}.type
                            hideProgressBar()
                            if(jsonObject.optJSONArray("Data") != null) {
                                categoryList = Gson().fromJson(jsonObject.optJSONArray("Data").toString(), modelType)
                                setCategoryAdapter()
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailed(statusCode: Int, message: String) {
                        hideProgressBar()
                        showShortToast(message)
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
            hideProgressBar()
        }
    }

    private fun openCategoryList() {
        recyclerViewCategories.visibility = View.GONE
        categoryListIsOpen = true
    }

    private fun closeCategoryList() {
        recyclerViewCategories.visibility = View.VISIBLE
        categoryListIsOpen = false
    }

    private fun setCategoryAdapter() {
        mAdapter = CategoryFilterListAdapter(this, categoryList, object : CategoryFilterListAdapter.OnItemCheckListener {
            override fun onItemCheck(item: CategoryListModel) {
                currentSelectedCategoryItems.add(item)
            }

            override fun onItemUncheck(item: CategoryListModel) {
                currentSelectedCategoryItems.remove(item)
            }
        }, selectedCategoryList)
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewCategories.layoutManager = mLayoutManager
        recyclerViewCategories.adapter = mAdapter
    }

    private fun apiGetTagList() {
        tagList = ArrayList()
        val param = HashMap<String, String>()
        param[""] = ""
        hideKeyboard(this)
        try {
            Retrofit.with(this)
                .setAPI(APIs.API_GET_TAG_LIST)
                .setGetParameters(param)
                .setCallBackListener(object : JSONCallback(this, showProgressBar()) {
                    override fun onSuccess(statusCode: Int, jsonObject: JSONObject) {
                        try {
                            val modelType = object : TypeToken<ArrayList<TagListModel>>() {}.type
                            hideProgressBar()
                            if(jsonObject.optJSONArray("Data") != null) {
                                tagList = Gson().fromJson(jsonObject.optJSONArray("Data").toString(), modelType)
                                setTagAdapter()
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailed(statusCode: Int, message: String) {
                        hideProgressBar()
                        showShortToast(message)
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
            hideProgressBar()
        }
    }

    private fun openTagList() {
        recyclerViewTags.visibility = View.GONE
        tagListIsOpen = true
    }

    private fun closeTagList() {
        recyclerViewTags.visibility = View.VISIBLE
        tagListIsOpen = false
    }

    private fun setTagAdapter() {
        nAdapter = TagFilterListAdapter(this, tagList, object : TagFilterListAdapter.OnItemCheckListener {
            override fun onItemCheck(item: TagListModel) {
                currentSelectedTagItems.add(item)
            }

            override fun onItemUncheck(item: TagListModel) {
                currentSelectedTagItems.remove(item)
            }
        }, selectedTagList)
        nLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTags.layoutManager = nLayoutManager
        recyclerViewTags.adapter = nAdapter
    }


    override fun setListeners() {


        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            requestPermission()
        }

        layout_search_business_filter.ivBack.setOnClickListener {
            onBackPressed()
        }

        layout_search_business_filter.tvClearAll.setOnClickListener {
            selectedCategoryList = ArrayList()
            selectedTagList = ArrayList()
            seekbar.progress = 1
            //            seekbarValue=0
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                seekbarValue = 0
            } else {
                seekbarValue = 2
            }
            apiGetCategoryList()
            apiGetTagList()
        }

        ivDropdownCategories.setOnClickListener {
            if(!categoryListIsOpen) {
                openCategoryList()
            } else {
                closeCategoryList()
            }
        }

        ivDropdownTags.setOnClickListener {
            if(!tagListIsOpen) {
                openTagList()
            } else {
                closeTagList()
            }
        }

        seekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekbarPosition = progress
                if(seekbarPosition == 0) {
                    seekbarValue = 1
                } else if(seekbarPosition == 2) {
                    seekbarValue = 5
                } else {
                    seekbarValue = 2
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        if(radius == 2)
            seekbar.progress = 1
        else if(radius == 5)
            seekbar.progress = 2
        else
            seekbar.progress = 0
        btnApply.setOnClickListener {
            selectedCategoryList = ArrayList<Int>()
            selectedTagList = ArrayList<Int>()
            for (d in nAdapter.getData()) {
                if(d.isSelected!!) {
                    selectedTagList.add(d.tagId!!)
                    Log.e("setListenerstag: ", "name----" + d.tagName + "id---" + d.tagId)
                }
            }
            for (d in mAdapter.getData()) {
                if(d.isSelected!!) {
                    selectedCategoryList.add(d.id!!.toInt())
                    Log.e("setListenerscat: ", "name----" + d.name + "id---" + d.id)
                }
            }
            Log.e("TAG", "setListeners: " + seekbarValue)
            Log.e("TAG", "setListeners: " + currentSelectedCategoryItems)
            Log.e("TAG", "setListeners: " + currentSelectedTagItems.size)
            val intentResult = Intent()
            intentResult.putIntegerArrayListExtra("catData", selectedCategoryList)
            intentResult.putIntegerArrayListExtra("tagData", selectedTagList)
            intentResult.putExtra("radius", seekbarValue)
            setResult(Activity.RESULT_OK, intentResult)
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}