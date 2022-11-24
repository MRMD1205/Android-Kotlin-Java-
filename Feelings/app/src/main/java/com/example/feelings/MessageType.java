package com.example.feelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageType extends AppCompatActivity {

    private Button birthdayWishBtn, anniversaryWishButton;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_type);

        birthdayWishBtn = (Button) findViewById(R.id.birthday_wish_btn);
        anniversaryWishButton = (Button) findViewById(R.id.anniversary_wish_btn);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        birthdayWishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageType.this, BirthDayWish.class);
                startActivity(intent);
            }
        });

        anniversaryWishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageType.this, anni_wish.class);
                startActivity(intent);
            }
        });

    }
}