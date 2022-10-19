package com.dynasty.util

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.dynasty.R
import kotlinx.android.synthetic.main.dialog_image_picker.*

class ImagePickerDialog(context: Context, private val itemClick: onItemClick) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_picker)

        assert(camera != null)
        camera!!.setOnClickListener {
            itemClick.onCameraClicked()
            dismiss()
        }

        assert(gallery != null)
        gallery!!.setOnClickListener {
            itemClick.onGalleryClicked()
            dismiss()
        }
    }
}

interface onItemClick {
    fun onCameraClicked()
    fun onGalleryClicked()
}