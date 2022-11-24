package com.example.feelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class ViewServiceDetails extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Services> list;
    ServiceAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service_details);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        searchView = (SearchView) findViewById(R.id.search_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Services>();

        reference = FirebaseDatabase.getInstance().getReference().child("Services");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Services p = dataSnapshot1.getValue(Services.class);
                    list.add(p);
                }
                adapter = new ServiceAdapter(ViewServiceDetails.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewServiceDetails.this, "Somthing Went Wrong...", Toast.LENGTH_SHORT).show();
            }
        });

        if(searchView != null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }

    }

    private void search(String str)
    {
        ArrayList<Services> myList = new ArrayList<>();
        for(Services object : list)
        {
            if(object.getServiceName().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        ServiceAdapter adapter = new ServiceAdapter(ViewServiceDetails.this, myList);
        recyclerView.setAdapter(adapter);
    }

}
