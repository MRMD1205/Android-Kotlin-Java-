package com.crashpot.base

import android.app.Activity
import android.view.View

interface BaseView {
    fun hideKeyboard(activity: Activity?)

    fun showKeyboard(view: View?)
}