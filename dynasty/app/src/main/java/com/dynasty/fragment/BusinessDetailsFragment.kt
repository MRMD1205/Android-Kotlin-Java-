package com.dynasty.fragment

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dynasty.R
import com.dynasty.adapter.BusinessDetailImgViewAdapter
import com.dynasty.adapter.ServicesAdapter
import com.dynasty.base.BaseFragment
import com.dynasty.model.BusinessDetailModel
import com.dynasty.util.GpsTracker
import com.dynasty.util.PreferenceData
import com.dynasty.util.Utils
import com.dynasty.webservice.APIs
import com.dynasty.webservice.JSONCallback
import com.dynasty.webservice.Retrofit
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_bottom_tabs.view.*
import kotlinx.android.synthetic.main.fragment_business_details.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [BusinessDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BusinessDetailsFragment(businessId: String) : BaseFragment() {

    var businessId: String = ""
    var facebookLink: String = ""
    var twitterLink: String = ""
    var instagramLink: String = ""
    var email: String = ""
    var contactNumber: String = ""
    private var gpsTracker: GpsTracker? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private var businessList = ArrayList<String>()
    private var tagsList = ArrayList<BusinessDetailModel.Tag>()
    private var businessServices = ArrayList<BusinessDetailModel.Service>()

    init {
        this.businessId = businessId
    }

    override fun setContentView(): Int = R.layout.fragment_business_details

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        clBusinessIntroFix.visibility = View.GONE

        apiGetBusinessDetails(businessId)
        Utils.showNoInternet(requireContext())
        val categoryImg: ImageView = requireActivity().findViewById<View>(R.id.ivCategory) as ImageView
        categoryImg.setImageResource(R.drawable.ic_home)
        val businessImg: ImageView = requireActivity().findViewById<View>(R.id.ivBusiness) as ImageView
        businessImg.setImageResource(R.drawable.ic_business_selected)
        if(isAdded) {
            return
        }
    }

    private fun apiGetBusinessDetails(id: String) {
        hideKeyboard(requireActivity())
        businessList = ArrayList()
        businessServices = ArrayList()
        try {
            Retrofit.with(requireActivity())
                .setAPI(APIs.API_GET_BUSINESS_DETAILS)
                .setParameters(id)
                .setCallBackListener(object : JSONCallback(requireActivity(), showProgressBar()) {
                override fun onSuccess(statusCode: Int, jsonObject: JSONObject) {
                    try {
                        val modelType = object : TypeToken<BusinessDetailModel>() {}.type
                        if(jsonObject.opt("Data") != null) {
                            val model: BusinessDetailModel = Gson().fromJson(jsonObject.optJSONObject("Data").toString(), modelType)
                            if(!model.service.isNullOrEmpty()) {
                                cvServices.visibility = View.VISIBLE
                                businessServices = model.service as ArrayList<BusinessDetailModel.Service>
                            } else {
                                cvServices.visibility = View.GONE
                            }
                            setUIData(model)
                            setAdapter()
                            setServiceAdapter()

                            clBusinessIntroFix.visibility = View.VISIBLE
                            hideProgressBar()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailed(statusCode: Int, message: String) {
                    try {
                        hideProgressBar()
                        showShortToast(message)
                        clBusinessIntroFix.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: Exception) {
            try {
                e.printStackTrace()
                hideProgressBar()
                clBusinessIntroFix.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun setUIData(model: BusinessDetailModel) {
        /*if(!model.businessLogo.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(model.businessLogo)
                .error(R.drawable.small_icon_placeholder)
                .placeholder(R.drawable.small_icon_placeholder)
                .into(object : CustomTarget<Drawable?>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                        ivBusinessIcon.setBackground(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }*/

        if(!model.businessLogo.isNullOrEmpty()) {
            Glide.with(requireContext()).load(model.businessLogo).error(R.drawable.small_icon_placeholder).placeholder(R.drawable.small_icon_placeholder).into(ivBusinessIcon)
        } else {
            Glide.with(requireContext()).load(R.drawable.small_icon_placeholder).placeholder(R.drawable.small_icon_placeholder).into(ivBusinessIcon)
        }

        if(!model.backgroundImage.isNullOrEmpty()) {
            Glide.with(requireContext()).load(model.backgroundImage).error(R.drawable.transluecent_leaf_bg).placeholder(R.drawable.transluecent_leaf_bg).into(ivThemeBg1)
            Glide.with(requireContext()).load(model.backgroundImage).error(R.drawable.transluecent_leaf_bg).placeholder(R.drawable.transluecent_leaf_bg).into(ivThemeBg2)
            Glide.with(requireContext()).load(model.backgroundImage).error(R.drawable.transluecent_leaf_bg).placeholder(R.drawable.transluecent_leaf_bg).into(ivThemeBg3)
        } else {
            Glide.with(requireContext()).load(R.drawable.transluecent_leaf_bg).placeholder(R.drawable.transluecent_leaf_bg).into(ivThemeBg1)
            Glide.with(requireContext()).load(R.drawable.transluecent_leaf_bg).placeholder(R.drawable.transluecent_leaf_bg).into(ivThemeBg2)
            Glide.with(requireContext()).load(R.drawable.transluecent_leaf_bg).placeholder(R.drawable.transluecent_leaf_bg).into(ivThemeBg3)
        }

        if(!model.categoryName.isNullOrEmpty()) {
            tvCategoryName.text = model.categoryName
        } else {
            tvCategoryName.text = ""
        }

        if(!model.businessName.isNullOrEmpty()) {
            tvCompanyName.text = model.businessName
        } else {
            tvCompanyName.text = ""
        }

        if(!model.address.isNullOrEmpty()) {
            tvCompanyAddress.text = model.address
        } else {
            tvCompanyAddress.text = ""
        }

        if(!model.about.isNullOrEmpty()) {
            cvAboutUs.visibility = View.VISIBLE
            tvAboutUsDescription.text = model.about
        } else {
            cvAboutUs.visibility = View.GONE
        }

        if(!model.facebookLink.isNullOrEmpty()) {
            ivFbIcon.visibility = View.VISIBLE
            facebookLink = model.facebookLink!!
        } else {
            ivFbIcon.visibility = View.GONE
        }

        if(!model.twitterLink.isNullOrEmpty()) {
            ivTwitterIcon.visibility = View.VISIBLE
            twitterLink = model.twitterLink!!
        } else {
            ivTwitterIcon.visibility = View.GONE
        }

        if(!model.instagramLink.isNullOrEmpty()) {
            ivInstagramIcon.visibility = View.VISIBLE
            instagramLink = model.instagramLink!!
        } else {
            ivInstagramIcon.visibility = View.GONE
        }

        if(!model.latitude.isNullOrEmpty() && !model.longitude.isNullOrEmpty()) {
            ivLocationIcon.visibility = View.VISIBLE
            latitude = model.latitude!!.toDouble()
            longitude = model.longitude!!.toDouble()
        } else {
            ivLocationIcon.visibility = View.GONE
        }

        if(!model.email.isNullOrEmpty()) {
            ivEmailIcon.visibility = View.VISIBLE
            email = model.email!!
        } else {
            ivEmailIcon.visibility = View.GONE
        }

        if(!model.contactNumber.isNullOrEmpty()) {
            ivCallIcon.visibility = View.VISIBLE
            ivSmsIcon.visibility = View.VISIBLE
            contactNumber = model.contactNumber!![0].contactNo!!

        } else {
            ivCallIcon.visibility = View.GONE
            ivSmsIcon.visibility = View.GONE
        }


        if(!model.image.isNullOrEmpty()) {
            if(model.image?.size!! > 0) {
                for (i in model.image!!.indices) {
                    businessList.add(model.image!![i].imageURL!!)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        if(!model.tags.isNullOrEmpty()) {
            if(model.tags?.size!! > 0) {
                cvTags.visibility = View.VISIBLE
                try {
                    tagsList.addAll(model.tags!!)
                    setCustomTag(requireContext(), chipGroup, tagsList)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            cvTags.visibility = View.GONE
        }
    }

    private fun setAdapter() {
        val adapter = BusinessDetailImgViewAdapter(requireContext(), businessList)
        rvBusinessImgItem.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        rvBusinessImgItem.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvBusinessImgItem.adapter = adapter
    }

    private fun setServiceAdapter() {
        val adapter = ServicesAdapter(requireContext(), businessServices)
        rvServices.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        rvServices.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvServices.adapter = adapter
    }

    override fun setListeners() {

        ivFbIcon.setOnClickListener {
            val packageManager = context?.packageManager
            try {
                val versionCode: Int? = packageManager?.getPackageInfo("com.facebook.katana", 0)?.versionCode
                if(versionCode != null) {
                    if(versionCode >= 3002850) {
                        val uri = Uri.parse("fb://facewebmodal/f?href=$facebookLink")
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink)))
            }
        }

        ivTwitterIcon.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink)))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        ivInstagramIcon.setOnClickListener {
            val uri = Uri.parse(instagramLink)
            val i = Intent(Intent.ACTION_VIEW, uri)
            i.setPackage("com.instagram.android")
            try {
                startActivity(i)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink)))
            }
        }

        ivLocationIcon.setOnClickListener {
            gpsTracker = GpsTracker(requireContext())
            if(gpsTracker!!.canGetLocation()) {
                val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            } else {
                gpsTracker!!.showSettingsAlert()
            }
        }

        ivEmailIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$email"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Your Subject")
            intent.putExtra(Intent.EXTRA_TEXT, "Type Here")
            startActivity(intent)
        }

        ivCallIcon.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", contactNumber, null))

            if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 10)
                return@setOnClickListener
            } else {
                try {
                    startActivity(phoneIntent)
                } catch (ex: ActivityNotFoundException) {
                    showLongToast("Sorry Not able to make a call")
                }
            }
        }

        ivSmsIcon.setOnClickListener {
            try {
                val uri = Uri.parse("smsto:$contactNumber")
                val it = Intent(Intent.ACTION_SENDTO, uri)
                it.putExtra("sms_body", "Your Feedback / Query ")
                startActivity(it)
            } catch (ex: ActivityNotFoundException) {
                showLongToast("Sorry Not able to SMS")
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setCustomTag(context: Context, chipGroup: ChipGroup, tagList: List<BusinessDetailModel.Tag>) {
        chipGroup.removeAllViews()
        for (index in tagList.indices) {
            val tagName: String = tagList[index].tagName.toString()
            val chip = Chip(ContextThemeWrapper(context, R.style.ChipStyle))
            val paddingDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, context.getResources().getDisplayMetrics()).toInt()
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
            if(!tagName.equals("null", ignoreCase = true)) {
                chip.text = tagName
            }
            chip.textSize = 14f
            chip.isAllCaps = true
            chip.includeFontPadding = false
            chip.letterSpacing = 0.17F
            chip.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            //---------------------------------------------chip drawable--------------------------
            val chipDrawable = chip.chipDrawable as ChipDrawable
            chipDrawable.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray_F6F6F6))
            chipDrawable.chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray_F6F6F6))
            chipDrawable.chipStrokeWidth = 2f
            chip.setChipDrawable(chipDrawable)
            //-----------------------------------------------------------------------
            chip.isCloseIconEnabled = false
            chip.isCheckedIconVisible = false
            chip.isCheckable = false
            chip.isChipIconEnabled = false
            val finalIndex = index
            chipGroup.addView(chip)
        }
    }

}