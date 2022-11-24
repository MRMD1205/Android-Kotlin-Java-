package com.example.feelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceProfile extends AppCompatActivity {

    EditText servicePrice;
    TextView mKey;
    Button updateBtn, deleteBtn;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_profile);

        servicePrice = (EditText) findViewById(R.id.update_delete_price);
        updateBtn = (Button) findViewById(R.id.update_btn);
        deleteBtn = (Button) findViewById(R.id.delete_btn);

        String key = getIntent().getExtras().get("key").toString();
        mKey = (TextView) findViewById(R.id.key);
        mKey.setText(key);
        reference = FirebaseDatabase.getInstance().getReference().child("Services").child(key);
        servicePrice.setText(getIntent().getStringExtra("servicePrice"));

    }

    public void btnUpdate_Click(View view) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("servicePrice").setValue(servicePrice.getText().toString());
                Toast.makeText(ServiceProfile.this, "Service Details Updated Successfully...!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ServiceProfile.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void btnDelete_Click(View view) {

        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(ServiceProfile.this, "Service Details Deleted Successfully...!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ServiceProfile.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(ServiceProfile.this, "Service Details Not Deleted...!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}