package com.example.dailydeals.activity

import android.content.Intent
import android.preference.PreferenceManager
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.dailydeals.R
import com.example.dailydeals.base.BaseActivity
import com.example.dailydeals.fragment.*
import com.google.android.material.navigation.NavigationView

class HomeActivity : BaseActivity() {

    private lateinit var btnLogout: Button

    lateinit var navigation: NavigationView
    lateinit var drawer: DrawerLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var toggle: ActionBarDrawerToggle

    override fun getLayoutResId(): Int {
        return R.layout.activity_home
    }

    override fun initViews() {
        navigation = findViewById(R.id.navigation)
        drawer = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.open, R.string.close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, HomeFragment()).commit()
        toolbar.title = "Home"

        navigation.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment()).commit()
                    toolbar.title = "Home"
                }
                R.id.navProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment()).commit()
                    toolbar.title = "Profile"
                }
                R.id.navAboutUs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutUsFragment()).commit()
                    toolbar.title = "About us"
                }
                R.id.navContactUs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ContactUsFragment()).commit()
                    toolbar.title = "Contact us"
                }
                R.id.navOrder -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, OrderFragment()).commit()
                    toolbar.title = "Order"
                }
                R.id.navWishlist -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, WishlistFragment()).commit()
                    toolbar.title = "Wishlist"
                }
                R.id.navTnC -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, TermsCondiFragment()).commit()
                    toolbar.title = "Terms & Conditions"
                }
                R.id.navPrivacy -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, PrivacyPolicyFragment()).commit()
                    toolbar.title = "Privacy Policy"
                }
                R.id.navLogout -> {

                    val sp = PreferenceManager.getDefaultSharedPreferences(this@HomeActivity)
                    val editor = sp.edit()
                    editor.putBoolean("booleanIsChecked", false)
                    editor.apply()

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        })
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun setListeners() {
    }
}