package com.dynasty.base

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dynasty.R
import com.dynasty.util.Utils
import kotlinx.android.synthetic.main.full_screen_dialog_no_internet_connection.*


abstract class BaseActivity : AppCompatActivity(), BaseView, View.OnClickListener {
    private lateinit var mContext: Context
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(getLayoutResId())
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initViews()
        setListeners()
        setCommonListeners()
    }

    open fun setCommonListeners() {
    }

    /**
     * To get layout resource id
     */
    abstract fun getLayoutResId(): Int

    /**
     * To initialize views of activity
     */
    abstract fun initViews()

    /**
     * To set listeners of view or callback
     */
    abstract fun setListeners()

    /**
     * Hide keyboard
     */
    override fun hideKeyboard(activity: Activity?) {
        val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if(view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Show Keyboard
     */
    override fun showKeyboard(view: View?) {
        if(view!!.requestFocus()) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onClick(view: View?) {
    }


    /**
     * navigate to fragment
     *
     * @param fragment       fragment name
     * @param addToBackstack is add to back stack or not
     * @param tag            fragment tag to identify fragment
     */
    open fun navigateToFragment(
        fragment: Fragment,
        addToBackstack: Boolean,
        tag: String?
    ) {
        navigateToFragment(fragment, addToBackstack, tag, false)
    }

    /**
     * navigate to fragment
     *
     * @param fragment       fragment name
     * @param addToBackstack is add to back stack or not
     * @param tag            fragment tag to identify fragment
     * @param showAnimation  if open fragment with animation or not
     */
    open fun navigateToFragment(
        fragment: Fragment,
        addToBackstack: Boolean,
        tag: String?,
        showAnimation: Boolean
    ) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if(showAnimation) {
            fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_right, R.anim.exit_to_left
            )
        }
        // TODO set fram container below
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag)
        if(addToBackstack) fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()

    }


    /**
     * @return get visible fragment ( if not null )
     */
    open fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this.supportFragmentManager
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if(fragment != null && fragment.isVisible) return fragment
        }
        return null
    }

    /**
     * clear all fragment from BackStack
     */
    open fun clearBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    /**
     * pop Immediate fragment
     */
    open fun popScreen() {
        supportFragmentManager.popBackStackImmediate()
    }

    /**
     * refresh Fragment
     */
    open fun refreshFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if(Build.VERSION.SDK_INT >= 26) {
            fragmentTransaction.setReorderingAllowed(false)
        }
        fragmentTransaction.detach(fragment)
        fragmentTransaction.attach(fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun hideProgressBar() {
        try {
            if(isProgressOpen()) {
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
        if(!isProgressOpen()) {
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
        if(!isProgressOpen()) {
            progressDialog = AlertDialog.Builder(context, R.style.AppTheme_ProgressDialog)
                .setCancelable(cancelable)
                .setView(ProgressBar(context))
                .setOnDismissListener(dismissListener)
                .create()
        }
        progressDialog!!.show()
    }

}