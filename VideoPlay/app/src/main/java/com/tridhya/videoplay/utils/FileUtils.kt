package com.tridhya.videoplay.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class FileUtils(private val mContext: Context) {
    fun convertMultiPart(fileUri: Uri, name: String): MultipartBody.Part? {
        val filePath = FilePathUtil.getPath(mContext, fileUri)
        if (filePath != null && filePath.isNotEmpty()) {
            val file = File(filePath)
            if (file.exists()) {
                // creates RequestBody instance from file
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                // MultipartBody.Part is used to send also the actual filename
                return MultipartBody.Part.createFormData(name, file.name, requestFile)
            }
            return null
        }
        return null
    }

    fun getFileSize(uri: Uri): Long? {
        val mimeType = mContext.contentResolver.getType(uri)
        var fileSize: Long? = null
        if (mimeType == null && mContext != null) {
            val path = getPath(uri)
            fileSize = if (path == null) {
                return null
            } else {
                val file = File(path)
                file.length()
            }
        } else {
            val returnCursor = mContext.contentResolver.query(uri, null, null, null, null)
            if (returnCursor != null) {
                val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                returnCursor.moveToFirst()
                fileSize = returnCursor.getLong(sizeIndex)
                returnCursor.close()
            }
        }
        return fileSize
    }


    @SuppressLint("NewApi")
    fun getPath(uri: Uri): String? {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null

        if (DocumentsContract.isDocumentUri(mContext, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                try {
                    val id = DocumentsContract.getDocumentId(uri)
                    uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                } catch (e: NumberFormatException) {
                    return null
                }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                when (type) {
                    "image" -> {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = mContext.contentResolver.query(
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun downloadPDF(url: String, fileName: String) {
        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
        request.setDescription("Download File")
        request.setTitle(fileName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            fileName
        )

        val manager: DownloadManager? =
            mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        manager?.enqueue(request)
    }

    fun GetFileExtension(uri: Uri?): String? {
        val contentResolver: ContentResolver = mContext.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }
}