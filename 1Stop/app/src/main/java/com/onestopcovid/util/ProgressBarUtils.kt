package com.onestopcovid.util

import android.content.Context
import android.content.DialogInterface
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.onestopcovid.R

class ProgressBarUtils {

    companion object {
        private var progressDialog: AlertDialog? = null

        fun showProgress(context: Context) {
            showProgress(context, false, null)
        }

        private fun showProgress(
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


        private fun isProgressOpen(): Boolean {
            return progressDialog != null && progressDialog!!.isShowing
        }

        fun cancelProgress() {
            try {
                if (isProgressOpen()) {
                    progressDialog!!.dismiss()
                    progressDialog = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}