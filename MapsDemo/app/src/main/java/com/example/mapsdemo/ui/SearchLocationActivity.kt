package com.example.mapsdemo.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.mapsdemo.R
import com.example.mapsdemo.databinding.ActivitySearchLocationBinding
import com.example.mapsdemo.localStorage.PlaceModel
import com.example.mapsdemo.ui.adapter.PlacesAutoCompleteAdapter
import com.example.mapsdemo.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place


class SearchLocationActivity : BaseActivity(), PlacesAutoCompleteAdapter.ClickListener {

    private lateinit var mAdapter: PlacesAutoCompleteAdapter
    private lateinit var mBinding: ActivitySearchLocationBinding
    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null
    private var markedPlace: PlaceModel? = null

    private val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.toString() != "") {
                mAdapter.filter.filter(s.toString())
                if (mBinding.rvPlaces.visibility == View.GONE) {
                    mBinding.rvPlaces.visibility = View.VISIBLE
                }
            } else {
                if (mBinding.rvPlaces.visibility == View.VISIBLE) {
                    mBinding.rvPlaces.visibility = View.GONE
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra(Constants.DATA)) {
            markedPlace = intent.getSerializableExtra(Constants.DATA) as PlaceModel
            val latLng = LatLng(
                markedPlace?.latitude!!,
                markedPlace?.longitude!!
            )
            googleMap?.addMarker(
                MarkerOptions().position(
                    latLng
                )
            )
            googleMap?.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(
                        latLng
                    ).zoom(12f).build()
                )
            )
            mBinding.etSearch.setText("")
            mBinding.llLocation.visibility = View.VISIBLE
            mBinding.tvLocation.text =
                "${markedPlace?.address}\n(${markedPlace?.latitude},${markedPlace?.longitude})"

            mBinding.btnSave.setOnClickListener {
                database.placesDao()
                    .add(markedPlace!!)
                finish()
            }

        }

        initDb()

        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync {
            this.googleMap = it
        }

        Places.initialize(this, Constants.MAPS_API_KEY);

        initView()
        mAdapter = PlacesAutoCompleteAdapter(this, this)
        mBinding.rvPlaces.adapter = mAdapter
        mAdapter.notifyDataSetChanged()


    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener {
            onBackPressed()
        }
        mBinding.etSearch.addTextChangedListener(filterTextWatcher)

    }

    override fun onPlaceSelected(place: Place) {
        googleMap?.addMarker(MarkerOptions().position(place.latLng))
        googleMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder().target(
                    place.latLng
                ).zoom(12f).build()
            )
        )
        mBinding.etSearch.setText("")
        mBinding.llLocation.visibility = View.VISIBLE
        mBinding.tvLocation.text =
            "${place.address}\n(${place.latLng.latitude},${place.latLng.longitude})"

        mBinding.btnSave.setOnClickListener {
            database.placesDao()
                .add(
                    if (markedPlace != null) {
                        markedPlace?.address = place.address
                        markedPlace?.id = place.id
                        markedPlace?.latitude = place.latLng.latitude
                        markedPlace?.longitude = place.latLng.longitude
                        markedPlace!!
                    } else {
                        PlaceModel(place.address, place.latLng.latitude, place.latLng.longitude)
                    }
                )
            finish()
        }

        hideKeyboard()
    }


}