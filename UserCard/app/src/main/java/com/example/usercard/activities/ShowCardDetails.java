package com.example.usercard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.TextView;

import com.example.usercard.R;
import com.example.usercard.databinding.ActivityShowCardDetailsBinding;

public class ShowCardDetails extends AppCompatActivity {

    private ActivityShowCardDetailsBinding activityShowCardDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card_details);

        activityShowCardDetailsBinding = DataBindingUtil.setContentView(ShowCardDetails.this, R.layout.activity_show_card_details);

        String oweValue = getIntent().getStringExtra("owe");
        String monthlyPaymentValue = getIntent().getStringExtra("monthlyPayments");
        String onPaymentValue = getIntent().getStringExtra("onPayments");
        String repairsValue = getIntent().getStringExtra("repairs");
        String sellerValue = getIntent().getStringExtra("seller");

        activityShowCardDetailsBinding.owe.setText(oweValue);
        activityShowCardDetailsBinding.monthlyPayments.setText(monthlyPaymentValue);
        activityShowCardDetailsBinding.onPayments.setText(onPaymentValue);
        activityShowCardDetailsBinding.repairs.setText(repairsValue);
        activityShowCardDetailsBinding.seller.setText(sellerValue);

    }
}