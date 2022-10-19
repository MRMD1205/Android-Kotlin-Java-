package com.courtesycarsredhill.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.courtesycarsredhill.ApplicationClass;
import com.courtesycarsredhill.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import static android.icu.util.ULocale.getName;
import static androidx.core.content.FileProvider.getUriForFile;
import static com.courtesycarsredhill.util.Common.isMediaDocument;
import static com.yalantis.ucrop.util.FileUtils.getDataColumn;
import static com.yalantis.ucrop.util.FileUtils.isDownloadsDocument;
import static com.yalantis.ucrop.util.FileUtils.isExternalStorageDocument;
import static org.apache.commons.codec.binary.Hex.encodeHex;

public class Utils {
    public static final String TEMP_IMAGE_FILE_NAME = "temp_image.jpg";
    public static final String TEMP_IMAGE_FILE_NAME_2 = "temp_image_2.jpg";

    public static void setLocale(Activity activity, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static String encode(String data) {
        Mac sha256_HMAC;
        String s = "";
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec("}{(*%$}&*^\\({#$%&@}^*$#{?}@^%#)(^%&".getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte digest[] = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            s = new String(encodeHex(digest));
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    Logger.d("Network", "NETWORK NAME: " + networkInfo.getTypeName());
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Logger.d("Network", "NETWORK NAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void ShareTheApp(Activity mActivity) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mActivity.getResources().getString(R.string.app_name) + "\n\n" + "https://play.google.com/store/apps/details?id=" + mActivity.getPackageName());
        sendIntent.setType("text/plain");
        mActivity.startActivity(Intent.createChooser(sendIntent, mActivity.getResources().getText(R.string.share_via)));
    }

    public static void ShareInfo(Activity mActivity, String info) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, info + "\n\nDownload now at:\n" + "https://play.google.com/store/apps/details?id=" + mActivity.getPackageName());
        sendIntent.setType("text/plain");
        mActivity.startActivity(Intent.createChooser(sendIntent, mActivity.getResources().getText(R.string.share_via)));
    }

    //Image Real Path from URI
    public static String getRealPathFromURI(Context mContext, Uri contentURI) {
        String result;
//        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String[] filePathColumn = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(filePathColumn[0]);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     */
    public static void showSettingsDialog(Activity mActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(mActivity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    public static void openSettings(Activity mActivity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        mActivity.startActivityForResult(intent, 101);
    }

  /*  public static void saveLanguageSelection(Context context, List<LanguageModel> ActivityList) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<LanguageModel>>() {
        }.getType();
        String strObject = gson.toJson(ActivityList, type);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("CategoryList", strObject).apply();
    }

    public static ArrayList<LanguageModel> getLanguageSelection(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        Type type = new TypeToken<List<LanguageModel>>() {
        }.getType();

        return gson.fromJson(prefs.getString("CategoryList", ""), type);
    }

    public static void setFooterFlags(RecyclerView rvFooterFlags) {

        ParseLocalJSONFile objJsonRead = new ParseLocalJSONFile();
        ArrayList<LanguageModel> languageList = objJsonRead.getLanguagesList(ApplicationClass.getAppContext());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ApplicationClass.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFooterFlags.setLayoutManager(layoutManager);
        rvFooterFlags.setAdapter(new FooterFlagAdapter(languageList));


    }*/

    // QR Code
   /* public static void generateQRCode(String qrpCode, AppCompatImageView iv) {
        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                generateQRCode(qrpCode, iv, true);
            }
        });
    }*/

    /*   public static void generateQRCode(String qrpCode, AppCompatImageView iv, String imagePath, AppCompatImageView ivUser) {
           generateQRCode(qrpCode, iv, true);
           BaseBinder.setImageUrl(ivUser, imagePath);
           ivUser.setVisibility(View.VISIBLE);
       }

       public static void generateQRCode(String qrpCode, AppCompatImageView iv, boolean isExcludeSpace) {
           if (qrpCode != null && !qrpCode.equals("null")) {
               Logger.e("QRCode: ", qrpCode);
               QRCodeWriter writer = new QRCodeWriter();
               Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
               hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
               hints.put(EncodeHintType.MARGIN, 0);
   //                EncryptionUtil obj = EncryptionUtil.getInstance();
   //                BitMatrix bitMatrix = isExcludeSpace ? writer.encode(obj.getEncodedValue(qrpCode), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight(), hints) : writer.encode(obj.getEncodedValue(qrpCode), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight());

               BitMatrix bitMatrix = null;
               try {
                   bitMatrix = isExcludeSpace ? writer.encode(CryptLib.getInstance().encryptPlainText(qrpCode), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight(), hints) : writer.encode(CryptLib.getInstance().encryptPlainText(qrpCode), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight());
   //                bitMatrix = isExcludeSpace ? writer.encode(obj.encryptPlainTextWithRandomIV(qrpCode, "(Black{Pearl}_Jack%Sparrow!)~2@03"), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight(), hints) : writer.encode(obj.encryptPlainTextWithRandomIV(qrpCode, "(Black{Pearl}_Jack%Sparrow!)~2@03"), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight());
               } catch (Exception e) {
                   e.printStackTrace();
               }

               int width = bitMatrix.getWidth();
               int height = bitMatrix.getHeight();
               int[] pixels = new int[width * height];
               for (int y = 0; y < height; y++) {
                   int offset = y * width;
                   for (int x = 0; x < width; x++) {
                       pixels[offset + x] = bitMatrix.get(x, y) ? ContextCompat.getColor(ApplicationClass.getAppContext(), R.color.blue) : WHITE;
                   }
               }

               Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
               bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

               iv.setImageBitmap(bitmap);
           }
       }
   */
    public static void setCurrentDate(AppCompatTextView tvDay, AppCompatTextView tvDate) {
        String[] strDate = TimeStamp.getCurrentDateElements();
        tvDay.setText(strDate[0]);
        tvDate.setText(strDate[1]);
    }

  /*  public static void setCurrentDateAndFlag(AppCompatTextView tvDay, AppCompatTextView tvDate, AppCompatImageView ivFlag, boolean isFlagged) {
        setCurrentDate(tvDay, tvDate);

        if (isFlagged) {
            ivFlag.setImageResource(R.drawable.ic_vector_flag_red);
        }
    }*/

    public static BitmapDescriptor BitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static String generateToken() {

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String currentDate = dateFormat.format(calendar.getTime());

        String tokenParams = "275229046203849302520".concat(currentDate);

        Logger.e("tokenParams: " + tokenParams);

        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(tokenParams.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            Logger.e("generateToken: " + hexString.toString());
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getFormattedNumber(String amount) {
        return getFormattedNumber(amount, true);
    }

    public static void dialdNumber(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static String getFormattedNumber(double amount) {
        return getFormattedNumber(amount, true);
    }

    public static String getFormattedNumber(String amount, boolean appendSymbol) {
        if (amount != null && amount.length() > 0) {
            try {
                double value = Double.parseDouble(amount);
                return getFormattedNumber(value, appendSymbol);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getFormattedNumber(double amount, boolean appendSymbol) {
        SessionManager session = new SessionManager(ApplicationClass.getAppContext());
        DecimalFormat formatter = new DecimalFormat("0.00");
        return appendSymbol ? session.getDataByKey(SessionManager.KEY_CURRENCY, "$").concat(formatter.format(amount)) : formatter.format(amount);
    }

    public static void openDirectionInGoogleMap(Context context, double latitude, double longitude) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", latitude, longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(mapIntent);
    }

    public static void openLinkInBrowser(Context mContext, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && url.contains("."))
            url = "http://" + url;
        if (url.length() > 0 && isValidUrl(url)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(browserIntent);
        } else {
            Toast.makeText(mContext, R.string.no_valid_url_available, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && url.contains("."))
            url = "http://" + url;
        Pattern p = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

  /*  public static String getWeekAndDayOfAd(float budget, float fee) {
        String weekAndDay = "";
        float weeks;
        if (budget > 0) {
            weeks = budget / fee;
            if (budget < fee) {
                weekAndDay = ApplicationClass.getAppContext().getResources().getString(R.string.lessthan_a_week);
            } else {
                int week = (int) weeks;
                int days = (int) (((weeks * 100) % 100) * 7 / 100);
                if (days == 1) {
                    weekAndDay = ApplicationClass.getAppContext().getString(week > 1 ? R.string.weeks_and_day : R.string.week_and_day, String.valueOf(week), String.valueOf(days));
                } else if (days > 1) {
                    weekAndDay = ApplicationClass.getAppContext().getString(week > 1 ? R.string.weeks_and_days : R.string.week_and_days, String.valueOf(week), String.valueOf(days));
                } else {
                    weekAndDay = ApplicationClass.getAppContext().getString(week > 1 ? R.string._weeks : R.string._week, String.valueOf(week));
                }
            }
        }
        return weekAndDay;
    }*/


    public static void setVectorForPreLollipop(TextView textView, int resourceId, Context activity, int position) {
        Drawable icon;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            icon = VectorDrawableCompat.create(activity.getResources(), resourceId, activity.getTheme());
        } else {
            icon = activity.getResources().getDrawable(resourceId, activity.getTheme());
        }
        switch (position) {
            case AppConstants.DRAWABLE_LEFT:
                textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null,
                        null);
                break;

            case AppConstants.DRAWABLE_RIGHT:
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon,
                        null);
                break;

            case AppConstants.DRAWABLE_TOP:
                textView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null,
                        null);
                break;

            case AppConstants.DRAWABLE_BOTTOM:
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                        icon);
                break;
        }
    }

    @Nullable
    public static File copyUriToFile(@NonNull Context context, @NonNull Uri uri) {

        InputStream in = null;
        OutputStream out = null;
        File outFile = null;
        try {
            if (context.getContentResolver() != null) {
                in = context.getContentResolver().openInputStream(uri);
                outFile = createImageFile(context, TEMP_IMAGE_FILE_NAME_2);
                if (outFile != null && in != null) {
                    out = new FileOutputStream(outFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if (out != null) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outFile;
    }

    /**
     * Creates empty file with name {@link #TEMP_IMAGE_FILE_NAME}
     * <p>
     * directory used:
     * internal data pictures
     * or external data pictures
     * or internal cache pictures
     * <p>
     * deletes file if already exist
     * creates new empty file
     *
     * @return created empty file or null if any operation fails
     * @throws IOException If an I/O error occurred
     */
    @Nullable
    public static File createImageFile(@NonNull Context context, @NonNull String imageFileName) throws IOException {
        File storageDir = context.getFilesDir();
        boolean dirCreated;
        if (storageDir == null) {
            File externalStorage = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (externalStorage == null) {
                storageDir = new File(context.getCacheDir(), Environment.DIRECTORY_PICTURES);
                dirCreated = storageDir.exists() || storageDir.mkdirs();
            } else {
                dirCreated = true;
            }
        } else {
            storageDir = new File(context.getFilesDir(), Environment.DIRECTORY_PICTURES);
            dirCreated = storageDir.exists() || storageDir.mkdirs();
        }

        if (dirCreated) {
            File imageFile = new File(storageDir, imageFileName);
            boolean isDeleted = true;
            if (imageFile.exists()) {
                isDeleted = imageFile.delete();
            }
            if (isDeleted) {
                boolean fileCreated = imageFile.createNewFile();
                if (fileCreated) return imageFile;
                else return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                /*final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);*/
                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    try {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));

                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception ignored) {
                    }
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }


    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        logDir(context.getCacheDir());
//        logDir(dir);

        return dir;
    }

    public static final String DOCUMENTS_DIR = "documents";

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            //Log.w(TAG, e);
            return null;
        }

        //logDir(directory);

        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new
                    FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }


    public static Uri getCacheImagePath(String fileName, Activity mActivity) {
        File path = new File(mActivity.getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(mActivity, mActivity.getPackageName() + ".provider", image);
    }
}