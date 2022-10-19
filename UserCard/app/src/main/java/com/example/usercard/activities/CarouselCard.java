package com.example.usercard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.usercard.R;
import com.example.usercard.adapter.CardAdapter;
import com.example.usercard.databinding.ActivityCarouselCardBinding;
import com.example.usercard.model.CardDetails;

import java.util.ArrayList;
import java.util.List;

public class CarouselCard extends AppCompatActivity {

    private CardAdapter cardAdapter;
    private List<CardDetails> cardDetailsList;
    private ActivityCarouselCardBinding activityCarouselCardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel_card);

        activityCarouselCardBinding = DataBindingUtil.setContentView(CarouselCard.this, R.layout.activity_carousel_card);

        cardDetailsList = new ArrayList<>();

        cardDetailsList.add(new CardDetails("How much do they owe?", "$0"));
        cardDetailsList.add(new CardDetails("Total Monthly Payments(PITI)", "$0"));
        cardDetailsList.add(new CardDetails("Behind on payments?", "$0"));
        cardDetailsList.add(new CardDetails("Repairs?", "$0"));
        cardDetailsList.add(new CardDetails("Cash to seller?", "$0"));

        cardAdapter = new CardAdapter(cardDetailsList, this, new CardAdapter.OnclickListener() {
            @Override
            public void onClick(int position) {
                if (activityCarouselCardBinding.viewPager.getCurrentItem() < cardDetailsList.size()) {
                    if (activityCarouselCardBinding.viewPager.getCurrentItem() == (cardDetailsList.size() - 1)) {
                        Intent intent = new Intent(CarouselCard.this, ShowCardDetails.class);
                        intent.putExtra("owe", cardDetailsList.get(activityCarouselCardBinding.viewPager.getCurrentItem() - 4).getValue());
                        intent.putExtra("monthlyPayments", cardDetailsList.get(activityCarouselCardBinding.viewPager.getCurrentItem() - 3).getValue());
                        intent.putExtra("onPayments", cardDetailsList.get(activityCarouselCardBinding.viewPager.getCurrentItem() - 2).getValue());
                        intent.putExtra("repairs", cardDetailsList.get(activityCarouselCardBinding.viewPager.getCurrentItem() - 1).getValue());
                        intent.putExtra("seller", cardDetailsList.get(activityCarouselCardBinding.viewPager.getCurrentItem()).getValue());
                        startActivity(intent);
                    }
                    activityCarouselCardBinding.viewPager.setCurrentItem(activityCarouselCardBinding.viewPager.getCurrentItem() + 1);
                }
            }
        });

        activityCarouselCardBinding.viewPager.setAdapter(cardAdapter);
        activityCarouselCardBinding.viewPager.setPadding(130, 0, 130, 0);
        activityCarouselCardBinding.viewPager.setClipToPadding(false);
        activityCarouselCardBinding.viewPager.setClipChildren(false);
        activityCarouselCardBinding.viewPager.setOffscreenPageLimit(3);
    }
}