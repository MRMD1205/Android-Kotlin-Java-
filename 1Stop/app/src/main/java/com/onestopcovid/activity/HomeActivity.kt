package com.onestopcovid.activity

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.onestopcovid.R
import com.onestopcovid.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun initViews() {
        toolBar.title = "Covid One Stop"
        setSupportActionBar(toolBar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()

        if (id == R.id.profile) {
            Toast.makeText(applicationContext, "Profile", Toast.LENGTH_SHORT).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setListeners() {
    }
}