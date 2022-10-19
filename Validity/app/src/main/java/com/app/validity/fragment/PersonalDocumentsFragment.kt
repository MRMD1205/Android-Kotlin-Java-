package com.app.validity.fragment

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.PersonalDocumentAdapter
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetPersonalDocumentListResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.ADD_PERSONAL_DOCUMENT_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.VIEW_PERSONAL_DOCUMENT_FRAGMENT
import com.app.validity.base.BaseFragment
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_personal_documents.*
import org.json.JSONObject

class PersonalDocumentsFragment : BaseFragment(), ItemClickCallback<GetPersonalDocumentListResponse.Datum> {

    override fun setContentView(): Int = R.layout.fragment_personal_documents

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        fabAddPersonalDocuments.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddPersonalDocumentFragment(null), true, ADD_PERSONAL_DOCUMENT_FRAGMENT)
        }
    }

    override fun onResume() {
        super.onResume()
        getPersonalDocumentList()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_personal_documents, false)
    }

    override fun setListeners() {
    }

    override fun onItemClick(view: View, selectedItem: GetPersonalDocumentListResponse.Datum, position: Int) {
        when (view.id) {

            R.id.llView -> (requireActivity() as DashboardActivity).navigateToFragment(
                ViewPersonalDocumentFragment(selectedItem), true, VIEW_PERSONAL_DOCUMENT_FRAGMENT)

            R.id.llEdit -> (requireActivity() as DashboardActivity).navigateToFragment(
                AddPersonalDocumentFragment(selectedItem), true, ADD_PERSONAL_DOCUMENT_FRAGMENT)

            R.id.llDelete -> {
                deletePersonalDocumentsDialog(selectedItem)
            }

            else -> {
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: GetPersonalDocumentListResponse.Datum, position: Int) {
        //deletePersonalDocumentsDialog(selectedItem)
    }

    private fun setAdapter(listResponse: GetPersonalDocumentListResponse) {
        val itemList: ArrayList<GetPersonalDocumentListResponse.Datum> =
            listResponse.list as ArrayList<GetPersonalDocumentListResponse.Datum>
        rvPersonalDocuments.adapter = PersonalDocumentAdapter(itemList, this)
    }

    private fun deletePersonalDocumentsDialog(selectedItem: GetPersonalDocumentListResponse.Datum) {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
        builder.setMessage(getString(R.string.want_delete_this_personal_document))
        builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
            dialog.dismiss()
            deletePersonalDocument(selectedItem.id.toString())
        }
        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    //API call
    private fun getPersonalDocumentList() {
        ApiCallMethods(requireActivity()).getPersonalDocumentList(object : OnApiCallCompleted<GetPersonalDocumentListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetPersonalDocumentListResponse = obj as GetPersonalDocumentListResponse
                isListEmpty(response.list.size == 0)
                setAdapter(response)
            }

            override fun apiFailure(errorMessage: String) {
                Utility.showToast(requireActivity(), errorMessage)
            }

            override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
                val errorMessage: String = errorObject.getString(MESSAGE)
                Utility.showToast(requireActivity(), errorMessage)
            }
        })
    }

    private fun deletePersonalDocument(propertyId: String) {
        ApiCallMethods(requireActivity()).deletePersonalDocument(
            propertyId,
            object : OnApiCallCompleted<BaseResponse> {
                override fun apiSuccess(obj: Any?) {
                    val response: BaseResponse = obj as BaseResponse
                    if (response.isSuccess) {
                        getPersonalDocumentList()
                    }
                }

                override fun apiFailure(errorMessage: String) {
                    Utility.showToast(requireActivity(), errorMessage)
                }

                override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
                    val errorMessage: String = errorObject.getString(MESSAGE)
                    Utility.showToast(requireActivity(), errorMessage)
                }
            })
    }
}
