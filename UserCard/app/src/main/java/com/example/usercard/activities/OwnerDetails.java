package com.example.usercard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usercard.R;
import com.example.usercard.databinding.ActivityOwnerDetailsBinding;

public class OwnerDetails extends AppCompatActivity {

    private ActivityOwnerDetailsBinding activityOwnerDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_details);

        activityOwnerDetailsBinding = DataBindingUtil.setContentView(OwnerDetails.this, R.layout.activity_owner_details);

        activityOwnerDetailsBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    Intent intent = new Intent(OwnerDetails.this, CarouselCard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(OwnerDetails.this, "All fields are required...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityOwnerDetailsBinding.btnSkipTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OwnerDetails.this, "can`t skip...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validation() {
        if (activityOwnerDetailsBinding.ownerName.getText().length() == 0
                && activityOwnerDetailsBinding.ARV.getText().length() == 0
                && activityOwnerDetailsBinding.rent.getText().length() == 0) {
            return false;
        }
        return true;
    }
}