package com.tridhya.videoplay.ui.viewModel.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tridhya.videoplay.ui.base.BaseViewModel

class HomeViewModel(context: Context) : BaseViewModel(context) {
    val responseUpdateData: MutableLiveData<String> = MutableLiveData()
//    private val homeUseCase =
//        HomeUseCases(context, errorLiveData, responseUpdateData = responseUpdateData)
//
//    fun updateData(request: List<SyncDataModel>) = homeUseCase.updateData(request)

}