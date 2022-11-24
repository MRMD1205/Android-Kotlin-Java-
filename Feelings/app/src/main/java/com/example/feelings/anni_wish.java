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

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class anni_wish extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Users> list;
    AnniDateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anni_wish);

        recyclerView = (RecyclerView) findViewById(R.id.birth_date_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Users>();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        final String currentDate = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        final String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        final Query query =   reference.orderByChild("anniversaryDate");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users p = dataSnapshot1.getValue(Users.class);
                    final String anniversaryDate = p.getAnniversaryDate();

                    String[] splitdata = anniversaryDate.split("/");

                    if (splitdata[0].equals(currentDate)) {
                        if(splitdata[1].equals(currentMonth)){
                            list.add(p);
                        }
                    }
                }
                if(list.isEmpty())
                {
                    Toast.makeText(anni_wish.this,"No Customer", Toast.LENGTH_LONG).show();
                }
                else {
                    adapter = new AnniDateAdapter(anni_wish.this, list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(anni_wish.this, "Somthing Went Wrong...", Toast.LENGTH_SHORT).show();
            }
        });

    }
}