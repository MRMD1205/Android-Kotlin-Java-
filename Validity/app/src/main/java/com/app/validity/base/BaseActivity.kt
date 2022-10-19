package com.crashpot.base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.validity.R


abstract class BaseActivity : AppCompatActivity(), BaseView, View.OnClickListener {
    private lateinit var mContext: Context

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
     * To initialize views of activity
     */
    abstract fun initViews()

    /**
     * To set listeners of view or callback
     */
    abstract fun setListeners()

    /**
     * To get layout resource id
     */
    abstract fun getLayoutResId(): Int

    /**
     * Hide keyboard
     */
    override fun hideKeyboard(activity: Activity?) {
        val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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

    override fun onClick(view: View?) {
    }

    /**
     * navigate to fragment
     *
     * @param fragment       fragment name
     * @param addToBackstack is add to back stack or not
     * @param tag            fragment tag to identify fragment
     */
    open fun navigateToFragment(fragment: Fragment, addToBackstack: Boolean, tag: String?) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //        fragmentTransaction.setCustomAnimations(
        //            R.anim.enter_from_right, R.anim.exit_to_left,
        //            R.anim.enter_from_right, R.anim.exit_to_left
        //        )
        // TODO set fram container below
        fragmentTransaction.replace(R.id.frameContainerFragment, fragment, tag)
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
}