package com.example.feelings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnniMessage extends AppCompatActivity {

    TextView bdayNumber,bdayName;
    Button bdayWishBtn;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anni_message);

        bdayNumber = (TextView) findViewById(R.id.bday_no);
        bdayName = (TextView) findViewById(R.id.bday_name);
        bdayWishBtn = (Button) findViewById(R.id.wish_btn);

        String key = getIntent().getExtras().get("key").toString();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        bdayNumber.setText(getIntent().getStringExtra("wphoneNumber"));
        bdayName.setText(getIntent().getStringExtra("fullName"));

        bdayWishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = bdayNumber.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                String path = "Warm wishes to you on your marriage milestone. \nHappy Anniversary! \n\nFrom : \nUrmi Mengar \nFeelings Beauty Care \nJamnagar";
                String url = "https://api.whatsapp.com/send?phone= 91" + number + "&text=" + path;
                sendIntent.setData(Uri.parse(url));
                startActivity(sendIntent);
            }
        });
    }
}