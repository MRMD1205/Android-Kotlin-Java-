package com.example.covidThreeStep.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.covidThreeStep.R
import com.onestopcovid.CovidThreeStopApplication
import com.onestopcovid.base.BaseActivity
import com.onestopcovid.fragment.HomeCovidTestFragment
import com.onestopcovid.fragment.ProfileFragment
import com.onestopcovid.util.HOME_COVID_TEST_FRAGMENT
import com.onestopcovid.util.PREF_IS_USER_LOGGED_IN
import com.onestopcovid.util.PROFILE_FRAGMENT
import com.onestopcovid.util.Utility
import kotlinx.android.synthetic.main.layout_appbar.*

class HomeActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun initViews() {
        navigateToFragment(HomeCovidTestFragment(), false, HOME_COVID_TEST_FRAGMENT, false)
    }

    fun setHeader(title: Int, profile: Boolean, recentTest: Boolean, vaccine: Boolean, newCovid: Boolean) {

        if (profile) {
            txtTitle.visibility = View.VISIBLE
            txtTitle.text = getString(title)
            ivBack.visibility = View.VISIBLE
            ivLogout.visibility = View.VISIBLE
            ivMenu.visibility = View.GONE
            ivProfile.visibility = View.GONE
        } else if (recentTest) {
            txtTitle.visibility = View.VISIBLE
            txtTitle.text = getString(title)
            ivBack.visibility = View.VISIBLE
            ivMenu.visibility = View.GONE
            ivProfile.visibility = View.GONE
        } else if (vaccine) {
            txtTitle.visibility = View.VISIBLE
            txtTitle.text = getString(title)
            ivBack.visibility = View.VISIBLE
            ivMenu.visibility = View.GONE
            ivProfile.visibility = View.GONE
        } else if (newCovid) {
            txtTitle.visibility = View.VISIBLE
            txtTitle.text = getString(title)
            ivBack.visibility = View.VISIBLE
            ivMenu.visibility = View.GONE
            ivProfile.visibility = View.GONE
        } else {
            txtTitle.visibility = View.GONE
            txtTitle.text = getString(title)
            ivMenu.visibility = View.VISIBLE
            ivProfile.visibility = View.VISIBLE
            ivBack.visibility = View.GONE
            ivLogout.visibility = View.GONE
        }

        ivProfile.setOnClickListener {
            navigateToFragment(ProfileFragment(), true, PROFILE_FRAGMENT, true)
        }

        ivBack.setOnClickListener {
            onBackPressed()
        }

        ivLogout.setOnClickListener {
            confirmationDialog(this)
        }

    }

    private fun confirmationDialog(context: Activity?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_exit))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { dialog, which -> navigateToLoginScreen() }
            .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
        val negativeButton: Button = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Utility.getColor(this, R.color.colorNegativeButton))
        val positiveButton: Button = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Utility.getColor(this, R.color.colorPositiveButton))
    }

    private fun navigateToLoginScreen() {
        CovidThreeStopApplication.instance.preferenceData?.setValueBoolean(PREF_IS_USER_LOGGED_IN, false)
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        setHeader(R.string.app_name, false, false, false, false)
    }

    override fun setListeners() {
    }

}