package com.example.dailydeals.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dailydeals.R

abstract class BaseFragment : Fragment(), BaseView, View.OnClickListener {

    private var mContext: Context? = null
    private var mActivity: AppCompatActivity? = null
    private var mView: View? = null
    private var isViewInitiated = false
    private var progressDialog: AlertDialog? = null
    var title: TextView? = null

    /**
     * To set fragment layout
     *
     * @return Layout id
     */
    abstract fun setContentView(): Int

    /**
     * To init view
     *
     * @param rootView           Fragment root view
     * @param savedInstanceState SavedInstance
     */
    abstract fun initView(rootView: View?, savedInstanceState: Bundle?)

    /**
     * To set listeners of views or any other
     */
    abstract fun setListeners()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun hideKeyboard(activity: Activity?) {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Show Keyboard
     */
    override fun showKeyboard(view: View?) {
        if (requireView().requestFocus()) {
            val imm =
                mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = getContext()
        mActivity = getContext() as AppCompatActivity?
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (mView != null) {
            mView
        } else inflater.inflate(setContentView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        if (!isViewInitiated) {
            initView(view, savedInstanceState)
            setListeners()
            isViewInitiated = true
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    fun hideProgressBar() {
        try {
            if (isProgressOpen()) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isProgressOpen(): Boolean {
        return progressDialog != null && progressDialog!!.isShowing
    }

    fun showProgressBar(): AlertDialog {
        showProgressBar(mContext!!, false, null)
        if (!isProgressOpen()) {
            progressDialog = AlertDialog.Builder(mContext, R.style.AppTheme_ProgressDialog)
                .setCancelable(false)
                .setView(ProgressBar(mContext))
                .create()
        }
        return progressDialog!!
    }

    private fun showProgressBar(
        context: Context,
        cancelable: Boolean,
        dismissListener: DialogInterface.OnDismissListener?
    ) {
        if (!isProgressOpen()) {
            progressDialog = AlertDialog.Builder(context, R.style.AppTheme_ProgressDialog)
                .setCancelable(cancelable)
                .setView(ProgressBar(context))
                .setOnDismissListener(dismissListener)
                .create()
        }
        progressDialog!!.show()
    }

    fun showShortToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onClick(view: View?) {
    }
}