package com.onestopcovid.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onestopcovid.BuildConfig
import com.onestopcovid.OneStopApplication
import com.onestopcovid.R
import java.io.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

class Utility {
    companion object {

        val VALID_EMAIL_ADDRESS_REGEX: Pattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

        /**
         * get color from resource
         */
        fun getColor(context: Context?, colorOrange: Int): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ContextCompat.getColor(context!!, colorOrange)
            } else context!!.resources.getColor(colorOrange)
        }

        @SuppressLint("ObsoleteSdkInt")
        fun getDrawable(context: Context?, id: Int): Drawable {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextCompat.getDrawable(context!!, id)!!
            } else {
                context!!.resources.getDrawable(id)
            }
        }

        /**
         * Show log of message
         */
        fun showLog(tag: String, message: String) {
            if (BuildConfig.DEBUG)
                Log.e(tag, message)
        }

        /**
         * Show toast of  message
         */
        fun showToast(mContext: Context?, message: String?) {
            val toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
            //            val view = toast.view
            //            MyDrawableCompat.setColorFilter(view.background, getColor(mContext, R.color.colorBlack))
            //            val text = view.findViewById<TextView>(R.id.message)
            //            text.setTextColor(getColor(mContext, R.color.colorWhite))
            toast.show()
        }

        /**
         * Check Internet is connected or not
         */
        fun isNetworkConnected(context: Context?): Boolean {
            val connectivityManager: ConnectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT < 23) {
                    val ni = connectivityManager.activeNetworkInfo
                    if (ni != null) {
                        return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI
                                || ni.type == ConnectivityManager.TYPE_MOBILE)
                    }
                } else {
                    val network: Network? = connectivityManager.activeNetwork
                    if (network != null) {
                        val networkCapabilities: NetworkCapabilities? =
                            connectivityManager.getNetworkCapabilities(network)

                        return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    }
                }
            }
            return false
        }

        /**
         * Share string Message To Other Apps
         */
        fun shareMessageToOtherApps(mContext: Context, message: String, extraMessage: String) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, extraMessage)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, message)
            mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }

        fun isEmailValid(string: String): Boolean {
            val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(string)
            return matcher.find()
        }

        fun getMyString(resId: Int): String? {
            return OneStopApplication.instance.getString(resId)
        }

        fun formatDecimal(doubleValue: Double?): String? {
            val numberFormat: NumberFormat = DecimalFormat("##.##")
            return "₹ " + numberFormat.format(doubleValue)
        }

        fun copyUriToFile(context: Context, uri: Uri): File? {
            var `in`: InputStream? = null
            var out: OutputStream? = null
            var outFile: File? = null
            try {
                if (context.contentResolver != null) {
                    `in` = context.contentResolver.openInputStream(uri)
                    var path: String = "temp_image_2_" + System.currentTimeMillis() + ".jpg"
                    outFile = createImageFile(context, path)
                    if (outFile != null && `in` != null) {
                        out = FileOutputStream(outFile)
                        val buf = ByteArray(1024)
                        var len: Int
                        while (`in`.read(buf).also { len = it } > 0) {
                            out.write(buf, 0, len)
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally { // Ensure that the InputStreams are closed even if there's an exception.
                try {
                    out?.close()
                    // If you want to close the "in" InputStream yourself then remove this
                    // from here but ensure that you close it yourself eventually.
                    `in`?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return outFile
        }

        @Throws(IOException::class)
        fun createImageFile(context: Context, imageFileName: String): File? {
            var storageDir = context.filesDir
            val dirCreated: Boolean
            if (storageDir == null) {
                val externalStorage =
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if (externalStorage == null) {
                    storageDir =
                        File(context.cacheDir, Environment.DIRECTORY_PICTURES)
                    dirCreated = storageDir.exists() || storageDir.mkdirs()
                } else {
                    dirCreated = true
                }
            } else {
                storageDir =
                    File(context.filesDir, Environment.DIRECTORY_PICTURES)
                dirCreated = storageDir.exists() || storageDir.mkdirs()
            }
            return if (dirCreated) {
                val imageFile = File(storageDir, imageFileName)
                var isDeleted = true
                if (imageFile.exists()) {
                    isDeleted = imageFile.delete()
                }
                if (isDeleted) {
                    val fileCreated = imageFile.createNewFile()
                    if (fileCreated) imageFile else null
                } else {
                    null
                }
            } else {
                null
            }
        }


        fun showSettingsDialog(context: Activity?) {
            val builder: MaterialAlertDialogBuilder =
                MaterialAlertDialogBuilder(context!!, R.style.AlertDialogTheme)
            builder.setTitle("Need Permissions")
            builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
            builder.setPositiveButton("GOTO SETTINGS") { dialog, p1 ->
                dialog!!.cancel()
                openSettings(context)
            }
            builder.setNegativeButton("Cancel") { dialog, p1 ->
                dialog!!.cancel()
            }
            builder.show()
        }

        // navigating user to app settings
        private fun openSettings(context: Activity?) {
            val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context?.packageName, null)
            intent.data = uri
            context?.startActivityForResult(intent, REQUEST_CODE)
        }

        fun isNull(str: String?): Boolean {
            return str == null || str.trim() == "" || str.trim().isEmpty()
        }
    }
}