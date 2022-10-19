package com.app.validity.activity

import android.content.Intent
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.app.validity.R
import com.app.validity.ValidityApplication
import com.app.validity.fragment.*
import com.app.validity.util.*
import com.crashpot.base.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_appbar.*


class DashboardActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun getLayoutResId(): Int = R.layout.activity_dashboard

    override fun initViews() {
        setNavigationItem()

        if (supportFragmentManager.backStackEntryCount == 0) {
            navigateToFragment(DashboardFragment(), false, DASHBOARD_FRAGMENT)
        }
    }

    private fun setNavigationItem() {
        ivMenu.visibility = View.VISIBLE
        nav_view.setNavigationItemSelectedListener(this)
        setSelection()
        nav_view.itemIconTintList = null //show original color of icon in Navigation drawer
        //        setNavigationHeader() //must be after calling api of get aggregator profile

        if (!TextUtils.isEmpty(ValidityApplication.instance.preferenceData!!.getValueFromKey(PREF_EMAIL))) {
            val headerView: View = nav_view.getHeaderView(0)
            val txtEmail: TextView = headerView.findViewById<View>(R.id.txtEmail) as TextView
            txtEmail.text = ValidityApplication.instance.preferenceData!!.getValueFromKey(PREF_EMAIL)
        }
    }

    fun setSelection() {
        nav_view.menu.getItem(0).isChecked = true
    }

    override fun setListeners() {
        ivMenu.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        when (view!!.id) {
            R.id.ivMenu -> {
                drawer.openDrawer(GravityCompat.START)
            }
        }
    }

    fun setHeader(title: Int, showDrawer: Boolean) {
        txtTitle.visibility = View.VISIBLE
        txtTitle.text = getString(title)

        if (showDrawer) {
            ivMenu.visibility = View.VISIBLE
            ivBack.visibility = View.GONE
        } else {
            ivMenu.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
        }

        ivMenu.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        nav_view.menu.clear()
        nav_view.inflateMenu(R.menu.menu_navigation_drawer)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        hideKeyboard(this)
        drawer.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_dashboard -> {
                navigateToFragment(DashboardFragment(), false, DASHBOARD_FRAGMENT)
            }
            R.id.nav_reminder -> {
                navigateToFragment(RemindersFragment(), true, REMINDERS_FRAGMENT)
            }
            R.id.nav_logout -> {
                logoutDialog()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment: Fragment? =
            supportFragmentManager.findFragmentByTag(ADD_VEHICAL_FRAGMENT)
        if (fragment != null && fragment is AddVehicalFragment) {
            fragment.onActivityResult(requestCode, resultCode, data)
        } else if (fragment != null && fragment is AddPersonalDocumentFragment) {
            fragment.onActivityResult(requestCode, resultCode, data)
        } else if (fragment != null && fragment is AddIndustrialFragment) {
            fragment.onActivityResult(requestCode, resultCode, data)
        } else if (fragment != null && fragment is AddHomeAppliancesFragment) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun logoutDialog() {
        val builder = MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme)
        builder.setMessage(getString(R.string.want_to_logout))
        builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
            dialog.dismiss()
            logOut()
        }
        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    fun logOut() {
        ValidityApplication.instance.preferenceData!!.setValueBoolean(PREF_IS_LOGIN, false)
        ValidityApplication.instance.preferenceData!!.setValue(PREF_DEVICE_TOKEN, "")

        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
        finish()
    }
}
