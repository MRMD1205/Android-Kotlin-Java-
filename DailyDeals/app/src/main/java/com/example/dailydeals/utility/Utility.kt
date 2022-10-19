package com.example.dailydeals.utility

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
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dailydeals.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.*
import java.math.BigDecimal
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class Utility {
    companion object {

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
         * Show log of message
         */
        fun showLog(tag: String, message: String) {
            Log.e(tag, message)
        }

        /**
         * Show log of message
         */
        fun showLog(message: String) {
            Log.e("@@@", message)
        }

        /**
         * Check Internet is connected or not
         */
        fun isNetworkConnected(context: Context?): Boolean {
            val connectivityManager: ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT < 23) {
                    val ni = connectivityManager.activeNetworkInfo
                    if (ni != null) {
                        return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                    }
                } else {
                    val network: Network? = connectivityManager.activeNetwork
                    if (network != null) {
                        val networkCapabilities: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(network)

                        return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
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

        /**
         * Hide keyboard
         */
        fun hideKeyboard(activity: Activity?) {
            val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) view = View(activity)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * Show Keyboard
         */
        fun showKeyboard(context: Context?, view: View?) {
            if (view!!.requestFocus()) {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        /**
         * Change string coin format ex: 2,400,658,987
         */
        fun changeCoinsFormat(coins: String): String {
            if (!TextUtils.isEmpty(coins)) {
                val formatter: NumberFormat = DecimalFormat("#,###")
                val myNumber: Double? = coins.toDouble()
                return formatter.format(myNumber)
            }
            return "0"
        }

        /**
         * change String Date Format for chat
         */
        fun changeStringDateFormat(dateString: String, newFormat: String): String? {
            val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("UTC")
            val targetFormat: DateFormat = SimpleDateFormat(newFormat, Locale.getDefault())
            targetFormat.timeZone = TimeZone.getDefault()
            var date: Date? = null;
            try {
                date = originalFormat.parse(dateString);
            } catch (e: java.lang.Exception) {
                e.printStackTrace();
            }
            var formattedDate = targetFormat.format(date)
            return formattedDate
        }

        /**
         * Set Image in circle Image view
         */
        fun setCircularImage(context: Context, view: CircleImageView, url: String) {
            try {
                Glide.with(context).load(url).apply(
                    RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)).into(view)
            } catch (e: Exception) {
                showLog("Error:", e.printStackTrace().toString())
            }
        }

        /**
         * Set Image in AppCompat Image View
         */
        fun setSimpleImage(context: Context, view: AppCompatImageView, url: String) {
            try {
                Glide.with(context).load(url).apply(
                    RequestOptions.centerInsideTransform().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)).into(view)
            } catch (e: Exception) {
                showLog("Error:", e.printStackTrace().toString())
            }
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
                val externalStorage = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if (externalStorage == null) {
                    storageDir = File(context.cacheDir, Environment.DIRECTORY_PICTURES)
                    dirCreated = storageDir.exists() || storageDir.mkdirs()
                } else {
                    dirCreated = true
                }
            } else {
                storageDir = File(context.filesDir, Environment.DIRECTORY_PICTURES)
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

        fun passwordShowOrHide(edtPassword: EditText, imageView: ImageView, eyeOpen: Int, eyeClose: Int) {
            if (edtPassword.transformationMethod is HideReturnsTransformationMethod) {
                edtPassword.transformationMethod = PasswordTransformationMethod()
                edtPassword.setSelection(edtPassword.text.length)
                imageView.setImageResource(eyeClose)
            } else {
                edtPassword.transformationMethod = HideReturnsTransformationMethod()
                edtPassword.setSelection(edtPassword.text.length)
                imageView.setImageResource(eyeOpen)
            }
        }

        /**
         * Format Double Values
         */
        fun Double?.formatAsUSD(): String {
            return when {
                this == null || this == 0.00 -> "0.00"
                this % 1 == 0.0 -> DecimalFormat("#,##0.00;-#,##0.00").format(this)
                else -> DecimalFormat("#,##0.00;-#,##0.00").format(this)
            }
        }

        fun getPmtMonthlyNote(loanAmt: Double, r: Double, term: Double): Double {
            var pmt: BigDecimal = BigDecimal(0)
            try {
                pmt = BigDecimal(loanAmt) * BigDecimal(r) * BigDecimal(Math.pow(r + 1, term) / (Math.pow(r + 1, term) - 1))
            } catch (e: Exception) {
                pmt = BigDecimal(0)
            }
            if (pmt.toDouble().isNaN() || pmt.toDouble().isInfinite()) {
                pmt = BigDecimal(0)
            }
            return pmt.toDouble()
        }

        fun getDoubleValue(value: String): Double {
            var newValue = value.replace(" ", "")
                .replace("$", "")
                .replace(",", "")
                .replace("%", "")
            return newValue.toDouble()
        }

        /**
         * Custom Tooltip
         */
        /*fun SetToolTip(mContext: Context?, view: View, message: String) {
            var tooltip: Tooltip? = null
            val gravity = Tooltip.Gravity.BOTTOM
            val style = R.style.ToolTipAltStyle
            val arrow = true
            val text = message


            fun getClosePolicy(): ClosePolicy {
                val builder = ClosePolicy.Builder()
                builder.inside(true)
                builder.outside(true)
                builder.consume(true)
                return builder.build()
            }

            tooltip = mContext?.let {
                Tooltip.Builder(it)
                    .anchor(view, 0, 0, false)
                    .arrow(arrow)
                    .closePolicy(getClosePolicy())
                    .text(text.toString()).styleId(style).create()
            }
            tooltip?.doOnHidden { tooltip = null }?.doOnFailure { }?.doOnShown {}?.show(view, gravity, true)
        }*/
    }
}