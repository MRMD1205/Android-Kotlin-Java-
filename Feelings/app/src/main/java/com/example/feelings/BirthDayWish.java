package com.example.feelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class BirthDayWish extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Users> list;
    BirthDateAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_day_wish);

        recyclerView = (RecyclerView) findViewById(R.id.birth_date_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Users>();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        final String currentDate = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        final String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        final Query query =   reference.orderByChild("birthDate");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users p = dataSnapshot1.getValue(Users.class);
                    final String birthDate = p.getBirthDate();

                    String[] splitdata = birthDate.split("/");

                        if (splitdata[0].equals(currentDate)) {
                            if(splitdata[1].equals(currentMonth)){
                                list.add(p);
                            }
                        }
                    }
                if(list.isEmpty())
                {
                    Toast.makeText(BirthDayWish.this,"No Customer", Toast.LENGTH_LONG).show();
                }
                else {
                    adapter = new BirthDateAdapter(BirthDayWish.this, list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BirthDayWish.this, "Somthing Went Wrong...", Toast.LENGTH_SHORT).show();
            }
        });

    }
}