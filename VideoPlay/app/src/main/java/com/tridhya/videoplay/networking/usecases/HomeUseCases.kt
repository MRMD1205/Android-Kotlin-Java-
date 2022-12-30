package com.tridhya.videoplay.networking.usecases

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tridhya.videoplay.model.BaseErrorModel
import com.tridhya.videoplay.networking.repos.HomeRepo
import com.tridhya.videoplay.utils.Session

class HomeUseCases(
    mContext: Context,
    private var errorLiveData: MutableLiveData<BaseErrorModel>,
    private var responseUpdateData: MutableLiveData<String>? = null,
) {
    private val homeRepo = HomeRepo(mContext)
    private val session by lazy { Session(mContext) }

//    fun updateData(request: List<SyncDataModel>?) {
//        if (request == null) return
//        homeRepo.updateData(request)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : CallbackObserver<BaseModel<Any?>>() {
//                override fun onSuccess(response: BaseModel<Any?>) {
//                    responseUpdateData?.value = response.message
//                }
//
//                override fun onFailed(code: Int, message: String) {
//                    errorLiveData.value = BaseErrorModel(message, code)
//                }
//
//            })
//    }

}