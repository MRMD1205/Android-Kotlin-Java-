package com.example.feelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_LONG;

public class AddService extends AppCompatActivity {

    private EditText editServiceName, editServicePrice;
    private Button SubmitDetailsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        editServiceName = (EditText) findViewById(R.id.edit_service_name);
        editServicePrice = (EditText) findViewById(R.id.edit_price);
        SubmitDetailsBtn = (Button) findViewById(R.id.submit_btn);

        SubmitDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
//              storeNewUsersData();
                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                    final DatabaseReference reference = rootNode.getReference("Services");

                    final String serviceName = editServiceName.getEditableText().toString().trim();
                    final String servicePrice = editServicePrice.getEditableText().toString().trim();

                    final Services addServices = new Services(serviceName, servicePrice);
                    reference.orderByChild("serviceName").equalTo(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {

                                Toast.makeText(AddService.this, "Service already registed", LENGTH_LONG).show();
                            } else {
                                reference.child(serviceName).setValue(addServices);
                                Toast.makeText(AddService.this, "Service Registered", LENGTH_LONG).show();

                                Intent intent = new Intent(AddService.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }

    public boolean checkValidation() {
        String serviceName = editServiceName.getEditableText().toString().trim();
        String servicePrice = editServicePrice.getEditableText().toString().trim();

        if (serviceName.length() <= 0) {
            Toast.makeText(AddService.this, "Please Enter Service Name", LENGTH_LONG).show();
            return false;
        }
        else if (servicePrice.length() <= 0) {
            Toast.makeText(AddService.this, "Please Enter Price", LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
}
