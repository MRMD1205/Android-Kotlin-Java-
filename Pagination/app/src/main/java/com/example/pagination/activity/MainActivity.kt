package com.example.pagination.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagination.R
import com.example.pagination.adapter.ProfileAdapter
import com.example.pagination.model.Profile
import com.example.pagination.network.ApiInterface
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var profileList: ArrayList<Profile>
    private lateinit var profileAdapter: ProfileAdapter
    private var page = 1
    private var pageLimit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nestedScrollView = findViewById(R.id.nestedScrollView)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)
        profileList = ArrayList()

        profileAdapter = ProfileAdapter(profileList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = profileAdapter

        getData(page, pageLimit)

        nestedScrollView.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                    page++
                    progressBar.visibility = View.VISIBLE
                    getData(page, pageLimit)
                }
            }
        })
    }

    private fun getData(page: Int, pageLimit: Int) {

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)

        val apiCall: Call<String> = apiInterface.apiCall(page, pageLimit)

        apiCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    progressBar.visibility = View.GONE
                    val jsonArray = JSONArray(response.body())
                    parseResult(jsonArray)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }
        })
    }

    private fun parseResult(jsonArray: JSONArray) {

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val profileData =
                Profile(jsonObject.getString("download_url"), jsonObject.getString("author"))
            profileList.add(profileData)

            profileAdapter = ProfileAdapter(profileList, this)
            recyclerView.adapter = profileAdapter
        }
    }
}