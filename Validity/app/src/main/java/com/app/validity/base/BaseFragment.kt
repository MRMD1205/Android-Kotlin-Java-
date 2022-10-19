package com.app.validity.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.validity.R
import com.crashpot.base.BaseView
import kotlinx.android.synthetic.main.layout_dashboard_container.*

abstract class BaseFragment : Fragment(), BaseView, View.OnClickListener {

    private var mContext: Context? = null
    private var mActivity: AppCompatActivity? = null
    private var mView: View? = null
    private var isViewInitiated = false

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

    fun getDotedText(string: String?): String {
        return if (string == null) " - " else "${getString(R.string.dot)} $string"
    }

    /**
     * Show Keyboard
     */
    override fun showKeyboard(view: View?) {
        if (view!!.requestFocus()) {
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
            requireActivity().txtListIsEmpty.visibility = View.GONE
            isViewInitiated = true
        }
    }

    fun isListEmpty(boolean: Boolean) {
        if (boolean) {
            requireActivity().txtListIsEmpty.visibility = View.VISIBLE
        } else {
            requireActivity().txtListIsEmpty.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onClick(view: View?) {
    }
}