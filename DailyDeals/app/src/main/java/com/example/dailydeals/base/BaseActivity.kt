package com.example.dailydeals.base

import android.R.id
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.dailydeals.R
import com.example.dailydeals.utility.WIFI_ENABLE_REQUEST
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity(), BaseView, View.OnClickListener {
    private lateinit var mContext: Context
    var progressDialog: AlertDialog? = null
    private val mInternetDialog: AlertDialog? = null
    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(getLayoutResId())
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        isConnected = false
        registerReceiver(mNetworkDetectReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
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
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private val mNetworkDetectReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkInternetConnection()
        }
    }

    private fun checkInternetConnection() {
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = manager.activeNetworkInfo

        if (ni != null && ni.state == NetworkInfo.State.CONNECTED) {
            isConnected = true
            showNoConnectionSnackBar("Connected", isConnected, 1500)
        } else {
            isConnected = false
            showNetworkAlertDialog(mContext)
            showNoConnectionSnackBar("No active Internet connection found.", isConnected, 3000)
        }
    }

    fun showNetworkAlertDialog(context: Context) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(context)
        builder.setTitle("NO INTERNET CONNECTION")
        builder.setMessage("Check Network Connectivity and Try again")
        builder.setCancelable(false)
        builder.setPositiveButton("Try again") { dialog, p1 ->
            registerReceiver(
                mNetworkDetectReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
            dialog!!.cancel()
        }
        builder.show()
    }

    private fun showNoConnectionSnackBar(message: String, connected: Boolean, duration: Int) {

        val snackbar = Snackbar.make(
            findViewById(id.content),
            message, duration
        )
        val sbView = snackbar.view
        val textView = sbView
            .findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        if (isConnected) {
            sbView.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        } else {
            sbView.setBackgroundColor(resources.getColor(R.color.colorGraph4))
            snackbar.setAction("Turn On") {
                val internetOptionsIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivityForResult(internetOptionsIntent, WIFI_ENABLE_REQUEST)
            }
            snackbar.setActionTextColor(resources.getColor(R.color.colorPrimary))
        }

        snackbar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == WIFI_ENABLE_REQUEST) {

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Show Keyboard
     */
    override fun showKeyboard(view: View?) {
        if (view!!.requestFocus()) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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
        showProgressBar(mContext, false, null)
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
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerFrame, fragment, tag)
        if (addToBackstack) fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    /**
     * @return get visible fragment ( if not null )
     */
    open fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this.supportFragmentManager
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible) return fragment
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
        if (Build.VERSION.SDK_INT >= 26) {
            fragmentTransaction.setReorderingAllowed(false)
        }
        fragmentTransaction.detach(fragment)
        fragmentTransaction.attach(fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        unregisterReceiver(mNetworkDetectReceiver);
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            mNetworkDetectReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        registerReceiver(
            mNetworkDetectReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }
}