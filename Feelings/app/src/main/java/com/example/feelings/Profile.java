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

public class Profile extends AppCompatActivity {

    EditText fullName, wphoneNumber, birthDate, anniversaryDate, address;
    TextView mKey;
    Button updateBtn, deleteBtn;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = (EditText) findViewById(R.id.update_delete_full_name);
        wphoneNumber = (EditText) findViewById(R.id.update_delete_wp_no);
        birthDate = (EditText) findViewById(R.id.update_delete_birth_date);
        anniversaryDate = (EditText) findViewById(R.id.update_delete_aniversary_date);
        address = (EditText) findViewById(R.id.update_delete_address);

        updateBtn = (Button) findViewById(R.id.update_btn);
        deleteBtn = (Button) findViewById(R.id.delete_btn);

        String key = getIntent().getExtras().get("key").toString();
        mKey = (TextView) findViewById(R.id.key);
        mKey.setText(key);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        fullName.setText(getIntent().getStringExtra("fullName"));
        wphoneNumber.setText(getIntent().getStringExtra("wphoneNumber"));
        birthDate.setText(getIntent().getStringExtra("birthDate"));
        anniversaryDate.setText(getIntent().getStringExtra("anniversaryDate"));
        address.setText(getIntent().getStringExtra("address"));
    }

    public void btnUpdate_Click(View view) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("fullName").setValue(fullName.getText().toString());
                dataSnapshot.getRef().child("wphoneNumber").setValue(wphoneNumber.getText().toString());
                dataSnapshot.getRef().child("birthDate").setValue(birthDate.getText().toString());
                dataSnapshot.getRef().child("anniversaryDate").setValue(anniversaryDate.getText().toString());
                dataSnapshot.getRef().child("address").setValue(address.getText().toString());

                Toast.makeText(Profile.this, "User Details Updated Successfully...!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Profile.this, MainActivity.class);
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
                    Toast.makeText(Profile.this, "User Details Deleted Successfully...!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Profile.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Profile.this, "User Details Not Deleted...!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
