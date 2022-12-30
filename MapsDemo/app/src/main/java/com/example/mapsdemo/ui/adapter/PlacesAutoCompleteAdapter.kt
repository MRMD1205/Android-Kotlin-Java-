package com.example.mapsdemo.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsdemo.databinding.ItemSearchPlaceBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class PlacesAutoCompleteAdapter(
    context: Context,
    private var clickListener: ClickListener? = null
) :
    RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder>(), Filterable {
    private var mResultList: ArrayList<PlaceAutocomplete>? = ArrayList()
    private val mContext: Context
    private val STYLE_BOLD: CharacterStyle
    private val STYLE_NORMAL: CharacterStyle
    private val placesClient: PlacesClient

    init {
        mContext = context
        STYLE_BOLD = StyleSpan(Typeface.BOLD)
        STYLE_NORMAL = StyleSpan(Typeface.NORMAL)
        placesClient = Places.createClient(context)
    }

    interface ClickListener {
        fun onPlaceSelected(place: Place)
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getPredictions(constraint)
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList
                        results.count = mResultList!!.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        }
    }

    private fun getPredictions(constraint: CharSequence): ArrayList<PlaceAutocomplete> {
        val resultList: ArrayList<PlaceAutocomplete> = ArrayList()

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()

        //https://gist.github.com/graydon/11198540
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                //.setCountry("BD")
                //.setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build()
        val autocompletePredictions: Task<FindAutocompletePredictionsResponse> =
            placesClient.findAutocompletePredictions(request)

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }
        return if (autocompletePredictions.isSuccessful()) {
            val findAutocompletePredictionsResponse: FindAutocompletePredictionsResponse =
                autocompletePredictions.getResult()
            if (findAutocompletePredictionsResponse != null) for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
                Log.i(TAG, prediction.placeId)
                resultList.add(
                    PlaceAutocomplete(
                        prediction.placeId,
                        prediction.getPrimaryText(STYLE_NORMAL).toString(),
                        prediction.getFullText(STYLE_BOLD).toString()
                    )
                )
            }
            resultList
        } else {
            resultList
        }
    }


    override fun onBindViewHolder(holder: PredictionHolder, position: Int) {
        holder.mBinding.tvAddress.text = mResultList!![position].address
        holder.mBinding.tvAddress.text = mResultList!![position].area
    }

    override fun getItemCount(): Int {
        return mResultList!!.size
    }

    inner class PredictionHolder internal constructor(val mBinding: ItemSearchPlaceBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        init {
            mBinding.root.setOnClickListener {
                val placeId = mResultList!![adapterPosition].placeId.toString()
                val placeFields: List<Place.Field> = listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS
                )
                val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                placesClient.fetchPlace(request).addOnSuccessListener { response ->
                    val place = response.place
                    clickListener!!.onPlaceSelected(place)
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Toast.makeText(mContext, exception.message + "", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    inner class PlaceAutocomplete internal constructor(
        var placeId: CharSequence,
        var area: CharSequence,
        var address: CharSequence
    ) {
        override fun toString(): String {
            return area.toString()
        }
    }

    companion object {
        private const val TAG = "PlacesAutoAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionHolder {
        val mBinding = ItemSearchPlaceBinding.inflate(LayoutInflater.from(parent.context))
        return PredictionHolder(mBinding)
    }
}