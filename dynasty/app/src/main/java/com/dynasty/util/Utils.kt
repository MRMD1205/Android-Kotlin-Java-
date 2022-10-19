package com.dynasty.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dynasty.R
import com.dynasty.base.BaseActivity
import kotlinx.android.synthetic.main.full_screen_dialog_no_internet_connection.*
//import de.hdodenhof.circleimageview.CircleImageView
import java.io.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class Utils {
    companion object {

        /**
         * get color from resource
         */
        fun getColor(context: Context?, colorOrange: Int): Int {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ContextCompat.getColor(context!!, colorOrange)
            } else context!!.resources.getColor(colorOrange)
        }

        @SuppressLint("ObsoleteSdkInt")
        fun getDrawable(context: Context?, id: Int): Drawable {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
         * Check Internet is connected or not
         */
        fun isConnectingToInternet(context: Context?): Boolean {
            val connectivityManager: ConnectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if(connectivityManager != null) {
                if(Build.VERSION.SDK_INT < 23) {
                    val ni = connectivityManager.activeNetworkInfo
                    if(ni != null) {
                        return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI
                                || ni.type == ConnectivityManager.TYPE_MOBILE)
                    }
                } else {
                    val network: Network? = connectivityManager.activeNetwork
                    if(network != null) {
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

        /**
         * Hide keyboard
         */
        fun hideKeyboard(activity: Activity?) {
            val imm =
                activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if(view == null) view = View(activity)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * Show Keyboard
         */
        fun showKeyboard(context: Context?, view: View?) {
            if(view!!.requestFocus()) {
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        /**
         * Show If Internet Lost
         */
        fun showNoInternet(context: Context?) {
            if(!isConnectingToInternet(context)) {
                val dialog = Dialog(context!!, R.style.Theme_AppCompat_Light)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.full_screen_dialog_no_internet_connection)
                dialog.btTryAgain.setOnClickListener {
                    if(isConnectingToInternet(context)) {
                        dialog.dismiss()
                    } else {
                        dialog.show()
                    }
                }
                dialog.show()
            }
        }


        /**
         * Change string coin format ex: 2,400,658,987
         */
        fun changeCoinsFormat(coins: String): String {
            if(!TextUtils.isEmpty(coins)) {
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
            val originalFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
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
        /*
                fun setCircularImage(context: Context, view: CircleImageView, url: String) {
                    try {
                        Glide.with(context).load(url).apply(
                            RequestOptions.circleCropTransform()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                        ).into(view)
                    } catch (e: Exception) {
                        showLog("Error:", e.printStackTrace().toString())
                    }
                }
        */

        /**
         * Set Image in AppCompat Image View
         */
        fun setSimpleImage(context: Context, view: AppCompatImageView, url: String) {
            try {
                Glide.with(context).load(url).apply(
                    RequestOptions.centerInsideTransform()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                ).into(view)
            } catch (e: Exception) {
                showLog("Error:", e.printStackTrace().toString())
            }
        }

        fun copyUriToFile(context: Context, uri: Uri): File? {
            var `in`: InputStream? = null
            var out: OutputStream? = null
            var outFile: File? = null
            try {
                if(context.contentResolver != null) {
                    `in` = context.contentResolver.openInputStream(uri)
                    var path: String = "temp_image_2_" + System.currentTimeMillis() + ".jpg"
                    outFile = createImageFile(context, path)
                    if(outFile != null && `in` != null) {
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
            if(storageDir == null) {
                val externalStorage =
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if(externalStorage == null) {
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
            return if(dirCreated) {
                val imageFile = File(storageDir, imageFileName)
                var isDeleted = true
                if(imageFile.exists()) {
                    isDeleted = imageFile.delete()
                }
                if(isDeleted) {
                    val fileCreated = imageFile.createNewFile()
                    if(fileCreated) imageFile else null
                } else {
                    null
                }
            } else {
                null
            }
        }

        fun showSettingsDialog(context: Activity?) {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(context, R.style.AlertDialogTheme)
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

        private fun openSettings(context: Activity?) {
            // navigating user to app settings
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context?.packageName, null)
            intent.data = uri
            context?.startActivityForResult(intent, REQUEST_CODE)
        }

        //Image Real Path from URI
        fun getRealPathFromURI(mContext: Context, contentURI: Uri): String? {
            val result: String?
            //        String[] filePathColumn = {MediaStore.Images.Media.DATA};
            val filePathColumn = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor =
                mContext.contentResolver.query(contentURI, filePathColumn, null, null, null)
            if(cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(filePathColumn[0])
                result = cursor.getString(idx)
                cursor.close()
            }
            return result
        }

        fun setCurrentDate(tvDay: AppCompatTextView, tvDate: AppCompatTextView) {
            val strDate = TimeStamp.currentDateElements
            tvDay.setText(strDate[0])
            tvDate.setText(strDate[1])
        }

        fun setVectorForPreLollipop(
            textView: TextView,
            resourceId: Int,
            activity: Context,
            position: Int
        ) {
            val icon: Drawable?
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                icon = VectorDrawableCompat.create(activity.resources, resourceId, activity.theme)
            } else {
                icon = activity.resources.getDrawable(resourceId, activity.theme)
            }
            when (position) {
                DRAWABLE_LEFT -> textView.setCompoundDrawablesWithIntrinsicBounds(
                    icon,
                    null,
                    null,
                    null
                )

                DRAWABLE_RIGHT -> textView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    icon,
                    null
                )

                DRAWABLE_TOP -> textView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    icon,
                    null,
                    null
                )

                DRAWABLE_BOTTOM -> textView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    icon
                )
            }
        }

        fun getActionBarHeight(mActivity: Activity): Int {
            if(mActivity.actionBar != null) {
                var actionBarHeight = (mActivity as BaseActivity).supportActionBar!!.height
                if(actionBarHeight != 0)
                    return actionBarHeight
                val tv = TypedValue()
                if(mActivity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data,
                        mActivity.getResources().displayMetrics
                    )
                return actionBarHeight
            }
            return 0
        }

        fun getFormattedAmount(textView: AppCompatTextView, amount: String?) {
            val decimalFormat = DecimalFormat("#")

            if(amount != null && !amount.isEmpty())

                textView.text = decimalFormat.format(java.lang.Double.parseDouble(amount))
            else {
                textView.text = amount
            }
        }

        fun getFormattedAmountString(amount: String?): String? {
            val decimalFormat = DecimalFormat("#")

            return if(amount != null && !amount.isEmpty())
                decimalFormat.format(java.lang.Double.parseDouble(amount))
            else {
                amount
            }
        }

        fun getBitmap(photo: Bitmap): Bitmap {
            val bitmap: Bitmap
            if(photo.width > photo.height) {
                val matrix = Matrix()
                matrix.postRotate(90f)
                bitmap = Bitmap.createBitmap(photo, 0, 0, photo.width, photo.height, matrix, true)
            } else {
                bitmap = photo
            }
            return bitmap
        }

        fun bitmapToString(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
    }
}