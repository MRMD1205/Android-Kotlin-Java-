package com.courtesycarsredhill.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.FragmentUploadDocumentBinding;
import com.courtesycarsredhill.model.DocumentTypeData;
import com.courtesycarsredhill.ui.ImagePickerActivity;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.ui.base.BaseFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Utils;
import com.courtesycarsredhill.webservice.APIs;
import com.courtesycarsredhill.webservice.JSONCallback;
import com.courtesycarsredhill.webservice.ResponseUtils;
import com.courtesycarsredhill.webservice.Retrofit;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.courtesycarsredhill.util.AppConstants.IMAGE_TYPE;
import static com.courtesycarsredhill.util.AppConstants.PDF_TYPE;

public class UploadDocumentFragment extends BaseFragment {

    FragmentUploadDocumentBinding mBinding;
    String mDocumentUri = null;
    String mImageUri = null;
    boolean isForImage = false;
    final Calendar myCalendar = Calendar.getInstance();
    public static final int REQUEST_IMAGE = 100;
    public static final int PICK_PDF_REQUEST = 1;
    private Boolean sosSuccess = false;
    private Boolean isReminder = false;
    private Boolean isRetake = false;
    ArrayList<DocumentTypeData> documentTypeList = new ArrayList<DocumentTypeData>();
    private File imgFile, pdfFile;
    private Uri mediaPath = null;
    ArrayList<String> docTypeName = new ArrayList<>();
    ArrayList<Integer> docTypeId = new ArrayList<>();
    int documentTypeId = 0;
    String reminderDays = "0";
    DocumentTypeData typeData = new DocumentTypeData();

    public static UploadDocumentFragment newInstance(String mDocumentUri, boolean isForImage) {
        UploadDocumentFragment fragment = new UploadDocumentFragment();
        Bundle args = new Bundle();
        args.putString("mDocumentUri", mDocumentUri);
        args.putBoolean("isForImage", isForImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("mDocumentUri")) {
                mDocumentUri = getArguments().getString("mDocumentUri");
            }
            if (getArguments().containsKey("isForImage")) {
                isForImage = getArguments().getBoolean("isForImage");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_upload_document, container, false);
        setClick();
        if (!isRetake) {
            loadPreview(mDocumentUri);
        } else {
            if (mImageUri != null) {
                loadPreview(mImageUri);
            }
        }
        callDocumentTypeApi();
        if (!mDocumentUri.isEmpty()) {
            if (!isRetake) {
                mediaPath = Uri.parse(mDocumentUri);
            } else {
                if (mImageUri != null) {
                    mediaPath = Uri.parse(mImageUri);
                }
            }
            if (isForImage) {
                imgFile = new File(Objects.requireNonNull(Utils.getPath(getActivity(), mediaPath)));
            } else {
                if (mediaPath.toString().startsWith("content://com.google.android.apps.docs.storage")) {
                    showLongToast("Please select PDF from local storage");
                } else {
                    pdfFile = new File(Objects.requireNonNull(Utils.getPath(getActivity(), mediaPath)));
                }
            }
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isForImage) {
            mBinding.btEditImage.setVisibility(View.VISIBLE);
            mBinding.ivPdfIcon.setVisibility(View.GONE);
            mBinding.tvPdfName.setVisibility(View.GONE);
            setupToolBarWithBackArrow(mBinding.toolbar.toolbar, getString(R.string.new_upload_Image));
        } else {
            mBinding.ivPdfIcon.setVisibility(View.VISIBLE);
            mBinding.tvPdfName.setVisibility(View.VISIBLE);
            if (pdfFile != null) {
                mBinding.tvPdfName.setText(pdfFile.getName());
            }
            setupToolBarWithBackArrow(mBinding.toolbar.toolbar, getString(R.string.new_upload_pdf));
        }

    }

    private void loadPreview(String url) {
        Glide.with(this).load(url).into(mBinding.ivDocumentUploaded);
        mBinding.ivDocumentUploaded.setColorFilter(ContextCompat.getColor(mActivity, android.R.color.transparent));
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(mActivity, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseFromGallery() {
                launchGalleryIntent();
            }

        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(mActivity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(mActivity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public boolean Validate() {

        String text = mBinding.etDocumentTitleValue.toString().trim();
        if (text.length() == 0) {
            showLongToast(getString(R.string.title_is_empty_validation));
            return false;
        }

        if (!(mBinding.tvExpiryDateValue.getText().length() > 0)) {
            showLongToast(getString(R.string.expiry_date_is_empty_validation));
            return false;
        }
        return true;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mBinding.tvExpiryDateValue.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getParcelableExtra("path");
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri);

                    // loading profile image from local cache
                    assert uri != null;
                    if (isRetake) {
                        mediaPath = uri;
                    } else {
                        mediaPath = Objects.requireNonNull(data).getData();
                    }
                    imgFile = new File(Objects.requireNonNull(Utils.getPath(getActivity(), mediaPath)));

                    mImageUri = uri.toString();
                    loadPreview(mImageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            Uri filePath = data.getData();
            mediaPath = Objects.requireNonNull(data).getData();
            pdfFile = new File(Objects.requireNonNull(Utils.getPath(getActivity(), mediaPath)));
//            mDocumentUri = filePath.toString();
        }

    }

    private void callDocumentTypeApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("", "");
        try {
            Retrofit.with(mContext)
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setGetParameters(params)
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_GET_DOCUMENT_TYPE_LIST, session))
                    .setHeaderCallBackListener(new JSONCallback(mContext, showProgressBar()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    Gson gson = new Gson();
                                    documentTypeList = gson.fromJson(jsonObject.optJSONArray("Data").toString(), new TypeToken<ArrayList<DocumentTypeData>>() {
                                    }.getType());

                                    if (documentTypeList != null && documentTypeList.size() > 0) {
                                        for (int i = 0; i < documentTypeList.size(); i++) {
                                            docTypeName.add(documentTypeList.get(i).getName());
                                        }
                                    }
                                    ArrayAdapter<String> mAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_dropdown_item, docTypeName);
                                    mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    mBinding.etDocumentTitleValue.setAdapter(mAdapter);

                                    documentTypeId = documentTypeList.get(Objects.requireNonNull(mBinding.etDocumentTitleValue).getSelectedItemPosition()+1).getDocumentTypeId();
                                    isReminder = documentTypeList.get(Objects.requireNonNull(mBinding.etDocumentTitleValue).getSelectedItemPosition()+1).getIsReminder();
                                    reminderDays = documentTypeList.get(Objects.requireNonNull(mBinding.etDocumentTitleValue).getSelectedItemPosition()+1).getReminderDays();

                                    Log.e("TAG", "onSuccess: " + documentTypeList.get(Objects.requireNonNull(mBinding.etDocumentTitleValue).getSelectedItemPosition()).getDocumentTypeId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showShortToast(jsonObject.optString("Message"));
                            }
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            hideProgressBar();
                            showShortToast(message);
                        }
                    });
        } catch (Exception e) {
            hideProgressBar();

        }
    }

    public void callDocumentSaveApi(String isReminder, String ReminderDays, int DocumentTypeId, String type) {
        String expiryDate = mBinding.tvExpiryDateValue.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        params.put("UserType", "DRIVER");
        params.put("UserId", String.valueOf(session.getUserDetail().getUserid()));
        params.put("ExpiryDate", expiryDate);
        params.put("isReminder", isReminder);
        params.put("ReminderDays", ReminderDays);
        params.put("DocumentTypeId", String.valueOf(DocumentTypeId));

        RequestBody reqFile;
        List<MultipartBody.Part> photos = new ArrayList<>();
        if (type.equalsIgnoreCase("image")) {
            reqFile = RequestBody.create(MediaType.parse("image/*"), imgFile);
            photos.add(MultipartBody.Part.createFormData("image[0]", imgFile.getName(), reqFile));
        } else {
            reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdfFile);
            photos.add(MultipartBody.Part.createFormData("image[0]", pdfFile.getName(), reqFile));
        }
        if (imgFile != null || pdfFile != null) {
            try {
                Retrofit.with(mContext)
                        .setHeader(session.getUserDetail().getAuthenticationkey())
                        .setParametersNew(params, photos)
                        .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_SAVE_DOCUMENT, session))
                        .setHeaderCallBackListener(new JSONCallback(getActivity(), showProgressBar()) {
                            @Override
                            protected void onSuccess(int statusCode, JSONObject jsonObject) {
                                hideProgressBar();
                                try {
                                    ((NavigationMainActivity) getActivity()).replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new DocumentSuccessfullyUploadedFragment(), false, true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected void onFailed(int statusCode, String message) {
                                hideProgressBar();
                                showShortToast(message);
                            }
                        });
            } catch (Exception e) {
                hideProgressBar();

            }
        } else {
            try {
                Retrofit.with(mContext)
                        .setHeader(session.getUserDetail().getAuthenticationkey())
                        .setGetParameters(params)
                        .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_SAVE_DOCUMENT, session))
                        .setHeaderCallBackListener(new JSONCallback(mContext, showProgressBar()) {
                            @Override
                            protected void onSuccess(int statusCode, JSONObject jsonObject) {
                                hideProgressBar();
                                try {
                                    ((NavigationMainActivity) getActivity()).replaceFragmentAndPop(AppConstants.NAVIGATION_KEY, new DocumentSuccessfullyUploadedFragment(), false, true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected void onFailed(int statusCode, String message) {
                                hideProgressBar();
                                showShortToast(message);
                            }
                        });
            } catch (Exception e) {
                hideProgressBar();

            }
        }
    }

    private void callSOSApi() {

        try {
            Retrofit.with(getContext())
                    .setHeader(session.getUserDetail().getAuthenticationkey())
                    .setParameters(new JSONObject())
                    .setAPI(ResponseUtils.getRequestAPIURL(APIs.API_SOS_REMINDER + "?DriverID=" + session.getUserDetail().getUserid(), session))
                    .setHeaderCallBackListener(new JSONCallback(getContext()) {
                        @Override
                        protected void onSuccess(int statusCode, JSONObject jsonObject) {
                            hideProgressBar();
                            if (jsonObject.optString("Success").equalsIgnoreCase("true")) {
                                try {
                                    sosSuccess = true;
                                    Log.e("TAG", "onActive Swipe Button: " + getString(R.string.notification_sent));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showShortToast(jsonObject.optString(getString(R.string.message)));
                            }
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            hideProgressBar();
                            showShortToast(message);
                        }
                    });
        } catch (Exception e) {
            hideProgressBar();

        }
    }

    private void openSOSDialog() {
        Dialog dialog = new Dialog(mActivity, R.style.DialogWithAnimation);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_sos_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        SwipeButton swipeBtn = dialog.findViewById(R.id.swipe_btn);

        swipeBtn.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                callSOSApi();
                swipeBtn.setEnabled(false);
            }
        });

        swipeBtn.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                TextView tvSOSTitle = dialog.findViewById(R.id.tvSosTitle);
                if (sosSuccess) {
                    swipeBtn.setHasActivationState(true);
                    tvSOSTitle.setText(getString(R.string.notification_sent));
                } else {
                    swipeBtn.setHasActivationState(false);
                    tvSOSTitle.setText(getString(R.string.notification_sent));
                }
            }
        });

        dialog.show();
    }

    private void setClick() {

        mBinding.toolbar.ivSosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOSDialog();
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        mBinding.viewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, date, myCalendar
                        .get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        mBinding.btUploadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validate()) {
                    // API call for Uploading Documents
                    if (isForImage) {
                        callDocumentSaveApi(isReminder.toString(), reminderDays, documentTypeId, IMAGE_TYPE);
                    } else {
                        if (mediaPath.toString().startsWith("content://com.google.android.apps.docs.storage")) {
                            showLongToast("Please select PDF from local storage");
                        } else {
                            callDocumentSaveApi(isReminder.toString(), reminderDays, documentTypeId, PDF_TYPE);
                        }
                    }
                }
            }
        });

        mBinding.btCancelNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });

        mBinding.btEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Feature to Adjust the Clicked Image
                isRetake = true;
                showImagePickerOptions();
            }
        });
    }

}