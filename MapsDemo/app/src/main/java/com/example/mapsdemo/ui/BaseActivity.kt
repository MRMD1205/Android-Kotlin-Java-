package com.example.mapsdemo.ui

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mapsdemo.localStorage.PlaceDao
import com.example.mapsdemo.localStorage.PlacesDB

open class BaseActivity : AppCompatActivity() {

    lateinit var database: PlacesDB

    fun hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (this.currentFocus != null) {
            var view: View = this.currentFocus!!
            if (view == null) {
                view = View(this)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    fun initDb() {
        database = Room.databaseBuilder(
            applicationContext,
            PlacesDB::class.java, "places-database"
        ).allowMainThreadQueries().build()
    }
}