package com.example.mapsdemo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapsdemo.R
import com.example.mapsdemo.databinding.ActivityMainBinding
import com.example.mapsdemo.localStorage.PlaceModel
import com.example.mapsdemo.ui.adapter.PlacesListAdapter
import com.example.mapsdemo.utils.Constants
import com.example.mapsdemo.utils.SortPlaces
import com.google.android.gms.maps.model.LatLng
import java.util.*

class MainActivity : BaseActivity(), PlacesListAdapter.OnOptionSelected {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: PlacesListAdapter
    private var sorting = "NA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initDb()

        mBinding.btnAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchLocationActivity::class.java))
        }


        mBinding.btnGetDirections.setOnClickListener {
            val intent = Intent(this@MainActivity, DirectionsActivity::class.java)
            startActivity(intent)
        }

        mBinding.ivSort.setOnClickListener {
            when (sorting) {
                "NA", "D" -> {
                    sorting = "A"
                    mBinding.ivSort.setImageResource(R.drawable.ic_arrow_up)
                }
                "A" -> {
                    sorting = "D"
                    mBinding.ivSort.setImageResource(R.drawable.ic_arrow_down)
                }
                else -> {
                    mBinding.ivSort.setImageResource(R.drawable.ic_sort)
                }
            }
            loadLocations()
        }

        mBinding.rvLocations.layoutManager = LinearLayoutManager(this)
        mAdapter = PlacesListAdapter(this)
        mBinding.rvLocations.adapter = mAdapter

        loadLocations()

    }

    override fun onEdit(place: PlaceModel) {
        val intent = Intent(this@MainActivity, SearchLocationActivity::class.java)
        intent.putExtra(Constants.DATA, place)
        startActivity(intent)
    }

    override fun onDelete(place: PlaceModel) {
        database.placesDao().remove(place)
        loadLocations()
    }

    override fun onResume() {
        super.onResume()
        loadLocations()
    }

    private fun loadLocations() {
        var list: List<PlaceModel> = emptyList()
        mAdapter = PlacesListAdapter(this)
        when (sorting) {
            "NA" -> {
                list = database.placesDao().getAll()
            }
            "A" -> {
                list = database.placesDao().getAll()
                Collections.sort(
                    list,
                    SortPlaces(LatLng(list.first().latitude, list.first().longitude))
                )
            }
            "D" -> {
                list = database.placesDao().getAll()
                Collections.sort(
                    list,
                    SortPlaces(LatLng(list.first().latitude, list.first().longitude))
                )
                list = list.reversed()
            }
        }
        mAdapter.setList(arrayListOf<PlaceModel>().apply { addAll(list) })
        mBinding.rvLocations.adapter = mAdapter
        mBinding.btnGetDirections.visibility = if (list.size > 1) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}