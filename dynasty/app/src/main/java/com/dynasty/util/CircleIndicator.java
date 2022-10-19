package com.dynasty.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.dynasty.R;


public class CircleIndicator extends LinearLayout {

  private final static int DEFAULT_INDICATOR_WIDTH = 5;
  private ViewPager mViewpager;
  private int mIndicatorMargin = -1;
  private int mIndicatorWidth = -1;
  private int mIndicatorHeight = -1;
  private final int mIndicatorBackgroundResId = R.drawable.white_circle;
  private final int mIndicatorUnselectedBackgroundResId = R.drawable.light_white_circle;
  private boolean isStaticCount = false;
  private int mLastPosition = -1;

  public CircleIndicator(Context context) {
    this(context, null);
  }

  public CircleIndicator(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
    init(context, attrs);
  }

  public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(Context context, @Nullable AttributeSet attrs) {
    if (attrs != null) {

      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
      try {
        mIndicatorWidth =
                typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_width, -1);
        mIndicatorHeight =
                typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_height, -1);
        mIndicatorMargin =
                typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, -1);

               /* mIndicatorBackgroundResId =
                    typedArray.getResourceId(R.styleable.CircleIndicator_ci_drawable,
                        R.drawable.white_radius);
                mIndicatorUnselectedBackgroundResId =
                    typedArray.getResourceId(R.styleable.CircleIndicator_ci_drawable_unselected,
                        mIndicatorBackgroundResId);*/
      } finally {
        typedArray.recycle();
      }
    }

    checkIndicatorConfig(context);
  }


  private void checkIndicatorConfig(Context context) {
    mIndicatorWidth = (mIndicatorWidth < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
    mIndicatorHeight =
            (mIndicatorHeight < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
    mIndicatorMargin =
            (mIndicatorMargin < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;

      /*  mIndicatorBackgroundResId = (mIndicatorBackgroundResId == 0) ? R.drawable.white_radius
            : mIndicatorBackgroundResId;
        mIndicatorUnselectedBackgroundResId =
            (mIndicatorUnselectedBackgroundResId == 0) ? mIndicatorBackgroundResId
                : mIndicatorUnselectedBackgroundResId;*/
  }

  public void setViewPager(ViewPager viewPager, boolean isStaticCount) {
    mViewpager = viewPager;
    if (mViewpager != null && mViewpager.getAdapter() != null) {
      mLastPosition = -1;
      this.isStaticCount = isStaticCount;
      createIndicators();
      mViewpager.removeOnPageChangeListener(mInternalPageChangeListener);
      mViewpager.addOnPageChangeListener(mInternalPageChangeListener);
      mInternalPageChangeListener.onPageSelected(mViewpager.getCurrentItem());
    }
  }

  private final ViewPager.OnPageChangeListener mInternalPageChangeListener = new ViewPager.OnPageChangeListener() {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

      if (mViewpager.getAdapter() == null || mViewpager.getAdapter().getCount() <= 0) {
        return;
      }


      View currentIndicator;
      if (mLastPosition >= 0 && (currentIndicator = getChildAt(mLastPosition)) != null) {
        currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
      }

      View selectedIndicator = getChildAt(position);
      if (selectedIndicator != null) {
        selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId);
      }
      mLastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
  };

  public DataSetObserver getDataSetObserver() {
    return mInternalDataSetObserver;
  }

  private DataSetObserver mInternalDataSetObserver = new DataSetObserver() {
    @Override
    public void onChanged() {
      super.onChanged();
      if (mViewpager == null) {
        return;
      }

      int newCount = mViewpager.getAdapter().getCount();
      int currentCount = getChildCount();

      if (newCount == currentCount) {  // No change
        return;
      } else if (mLastPosition < newCount) {
        mLastPosition = mViewpager.getCurrentItem();
      } else {
        mLastPosition = -1;
      }

      createIndicators();
    }
  };

  /**
   * @deprecated Use {@link ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)}
   */
  @Deprecated
  public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
    if (mViewpager == null) {
      throw new NullPointerException("can not find Viewpager , setViewPager first");
    }
    mViewpager.removeOnPageChangeListener(onPageChangeListener);
    mViewpager.addOnPageChangeListener(onPageChangeListener);
  }

  private void createIndicators() {
    removeAllViews();
    int count;
    if (!isStaticCount)
      count = mViewpager.getAdapter().getCount();
    else
      count = 9; // jaimit

    if (count <= 0) {
      return;
    }
    int currentItem = mViewpager.getCurrentItem();
    int orientation = getOrientation();

    for (int i = 0; i < count; i++) {
      if (currentItem == i) {
        addIndicator(orientation, mIndicatorBackgroundResId);
      } else {
        addIndicator(orientation, mIndicatorUnselectedBackgroundResId);
      }
    }
  }

  private void addIndicator(int orientation, @DrawableRes int backgroundDrawableId) {

    View Indicator = new View(getContext());
    Indicator.setBackgroundResource(backgroundDrawableId);
    addView(Indicator, mIndicatorWidth, mIndicatorHeight);
    LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();

    if (orientation == HORIZONTAL) {
      lp.leftMargin = mIndicatorMargin;
      lp.rightMargin = mIndicatorMargin;
    } else {
      lp.topMargin = mIndicatorMargin;
      lp.bottomMargin = mIndicatorMargin;
    }

    Indicator.setLayoutParams(lp);
  }

  public int dip2px(float dpValue) {
    final float scale = getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
