package com.courtesycarsredhill.ui.base;

import android.content.res.Resources;
import android.net.Uri;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.courtesycarsredhill.R;
import com.courtesycarsredhill.util.TimeStamp;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.courtesycarsredhill.util.TimeStamp.FullDateTimeFormat;
import static com.courtesycarsredhill.util.TimeStamp.TimeFormate;
import static com.courtesycarsredhill.webservice.APIs.BASE_IMAGE_PATH;

/**
 * Created by viraj.patel on 05-Sep-18
 */
public class BaseBinder {

    @BindingAdapter({"app:setLanguageIcon"})
    public static void setFlagIcon(AppCompatImageView iv, String image) {

        if (image != null && image.length() > 0 && !image.equals("null")) {
            Resources resources = iv.getContext().getResources();
            final int resourceId = resources.getIdentifier(image, "drawable",
                    iv.getContext().getPackageName());
            iv.setImageResource(resourceId);
        }/* else {
            iv.setImageResource(R.drawable.ic_vector_flag_soomaaliya);
        }*/
    }

    @BindingAdapter({"app:setSimpleImage"})
    public static void setSimpleImage(AppCompatImageView iv, String image) {

        if (image != null && image.length() > 0 && !image.equals("null"))
            Glide.with(iv.getContext())
                    .load(BASE_IMAGE_PATH + image)
                    .apply(new RequestOptions()
                            .placeholder(R.color.transparent)
                            .error(R.color.grey)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .centerCrop())
                    .transition(withCrossFade(300))
                    .into(iv);
       /* else
            iv.setImageResource(R.drawable.ic_vector_onkout);*/
    }
/*
    @BindingAdapter({"app:setBackgroundTripList"})
    public static void setBackgroundTripList(ConstraintLayout constraintLayout){


    }*/

    @BindingAdapter({"app:setImageUri", "app:setImageUrl"})
    public static void setImage(AppCompatImageView iv, Uri imageUri, String Url) {

        if (Url == null || Url.length() == 0) {
            setImageUri(iv, imageUri);
        } else {
            setImageUrl(iv, Url);
        }
    }

    @BindingAdapter({"app:setImageUri"})
    public static void setImageUri(AppCompatImageView iv, Uri imageUri) {

        if (imageUri != null)
            Glide.with(iv.getContext())
                    .load(imageUri.getPath())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.shape_circle_grey)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(iv);
        else
            iv.setImageResource(R.drawable.shape_circle_grey);
    }

    @BindingAdapter({"app:setImageUrl"})
    public static void setImageUrl(AppCompatImageView iv, String imageUrl) {

        if (imageUrl != null && imageUrl.length() > 0 && !imageUrl.equals("null"))
            Glide.with(iv.getContext())
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.shape_circle_grey)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(iv);
        else
            iv.setImageResource(R.drawable.shape_circle_grey);
    }


    @BindingAdapter({"app:setDate"})
    public static void setDate(TextView tv, String date) {
        if (date != null) {
            //2018-07-06T00:00:00"
            // String date="Mar 10, 2016 6:30:00 PM" "2018-07-06T00:00:00",;
            String formatDate = TimeStamp.customDateFormat(TimeStamp.formatToSeconds(date, "yyyy-MM-dd"), "dd MMM");
            tv.setText(formatDate);
        }
    }

    @BindingAdapter({"app:setTime"})
    public static void setTime(TextView tv, String time) {

        //2018-07-06T00:00:00"
        //  2019-03-28T18:21:54.93
        // String date="Mar 10, 2016 6:30:00 PM" "2018-07-06T00:00:00",;
        if (time != null && time.length() > 0) {
            String formatDate = TimeStamp.changeFormat(FullDateTimeFormat, TimeFormate,time);
            tv.setText(formatDate);
        } else {
            tv.setText("-");
        }


    }
/*
    @BindingAdapter({"app:setBackground"})
    public static void setBackground(ConstraintLayout constraintLayout, String tripStatus) {
        if (tripStatus!=null) {
            if(tripStatus.equals(AppConstants.TRIP_READY_TO_GO))
                constraintLayout.setBackground(constraintLayout.getContext().);
            //2018-07-06T00:00:00"
            // String date="Mar 10, 2016 6:30:00 PM" "2018-07-06T00:00:00",;
            String formatDate=  TimeStamp.customDateFormat(TimeStamp.formatToSeconds(date,"yyyy-MM-dd"),"dd MMM");
            tv.setText(formatDate);
        }
    }*/

    @BindingAdapter({"app:setImageUrl", "app:isSetOnkoutLogo"})
    public static void setImageUrl(AppCompatImageView iv, String imageUrl, boolean isSetOnkoutLogo) {

        if (imageUrl != null && imageUrl.length() > 0 && !imageUrl.equals("null"))
            Glide.with(iv.getContext())
                    .load(BASE_IMAGE_PATH + imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.shape_circle_grey)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(iv);
      /*  else
          iv.setImageResource(isSetOnkoutLogo ? R.drawable.ic_vector_onkout : R.drawable.shape_circle_grey);*/
    }
/*
    @BindingAdapter({"app:setFlaggedImage"})
    public static void setFlaggedImage(AppCompatImageView iv, boolean isFlagged) {
     //   iv.setImageResource(isFlagged ? R.drawable.ic_vector_flag_red : R.drawable.ic_vector_flag_gray);
    }*/
}
