package com.dynasty.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dynasty.R
import kotlinx.android.synthetic.main.dialog_bottom_sheet_forgot_password.*

class ForgotPasswordBottomSheetDialog() : BottomSheetDialogFragment() {


    override fun getTheme(): Int {
        return R.style.BottomSheetThemeBecomeAdvisor
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            val view = view
            view?.post {
                val parent = view.parent as View
                val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
                bottomSheetBehavior!!.isFitToContents = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.peekHeight = view.measuredHeight
                (bottomSheet.parent as View).setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom_sheet_forgot_password, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgCancel.setOnClickListener {
            dismiss()
        }

        btnSubmit.setOnClickListener {
            dismiss()
        }
    }
}