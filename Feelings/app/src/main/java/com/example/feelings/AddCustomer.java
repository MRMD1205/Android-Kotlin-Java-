package com.example.feelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_LONG;

public class AddCustomer extends AppCompatActivity {

    private EditText EditFullName, EditPhoneNumber, EditWPhoneNumber, EditAnniversaryDate, EditBirthDate, EditAddress;
    private Button SubmitDetailsBtn;
    private DatePickerDialog.OnDateSetListener mDateSetListner;
    String fullName, phoneNumber, WphoneNumber, birthDate, anniversaryDate, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        EditAnniversaryDate = (EditText) findViewById(R.id.edit_aniversary_date);
        EditBirthDate = (EditText) findViewById(R.id.edit_birth_date);
        EditFullName = (EditText) findViewById(R.id.edit_full_name);
        EditPhoneNumber = (EditText) findViewById(R.id.edit_ph_no);
        EditWPhoneNumber = (EditText) findViewById(R.id.edit_wp_no);
        EditAddress = (EditText) findViewById(R.id.edit_address);
        SubmitDetailsBtn = (Button) findViewById(R.id.submit_btn);


        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);


        EditBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(
                        AddCustomer.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;

                        if(month < 10 && day <10){
                            String date = "0" + day + "/" + "0" + month + "/" + year;
                            EditBirthDate.setText(date);
                        }

                        else if(month < 10){
                            String date = day + "/" + "0" + month + "/" + year;
                            EditBirthDate.setText(date);
                        }
                        else if(day < 10){
                            String date = "0" + day + "/" + month + "/" + year;
                            EditBirthDate.setText(date);
                        }
                        else {
                            String date = day + "/" + month + "/" + year;
                            EditBirthDate.setText(date);
                        }

                    }
                }, year, month, day);
                dialog.show();
            }
        });

        EditAnniversaryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(
                        AddCustomer.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;

                        if(month < 10 && day <10){
                            String date = "0" + day + "/" + "0" + month + "/" + year;
                            EditAnniversaryDate.setText(date);
                        }
                        else if(month < 10){
                            String date = day + "/" + "0" + month + "/" + year;
                            EditAnniversaryDate.setText(date);
                        }
                        else if(day < 10){
                            String date = "0" + day + "/" + month + "/" + year;
                            EditAnniversaryDate.setText(date);
                        }
                        else {
                            String date = day + "/" + month + "/" + year;
                            EditAnniversaryDate.setText(date);
                        }
                    }
                }, year, month, day);
                dialog.show();
            }
        });


        SubmitDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkValidation()) {
//              storeNewUsersData();
                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                    final DatabaseReference reference = rootNode.getReference("Users");

                    final String fullName = EditFullName.getEditableText().toString().trim();
                    final String phoneNumber = EditPhoneNumber.getEditableText().toString().trim();
                    String WphoneNumber = EditWPhoneNumber.getEditableText().toString().trim();
                    String birthDate = EditBirthDate.getEditableText().toString().trim();
                    String anniversaryDate = EditAnniversaryDate.getEditableText().toString();
                    String address = EditAddress.getEditableText().toString().trim();

                    final AddUserClass addUser = new AddUserClass(fullName, phoneNumber, WphoneNumber, birthDate, anniversaryDate, address);
                    reference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {

                                Toast.makeText(AddCustomer.this, "Mobile Number already registed", LENGTH_LONG).show();
                            } else {

                                reference.child(phoneNumber).setValue(addUser);
                                Toast.makeText(AddCustomer.this, "User created", LENGTH_LONG).show();

                                Intent intent = new Intent(AddCustomer.this, MainActivity.class);
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
        final String fullName = EditFullName.getEditableText().toString().trim();
        String phoneNumber = EditPhoneNumber.getEditableText().toString().trim();
        String WphoneNumber = EditWPhoneNumber.getEditableText().toString().trim();
        String birthDate = EditBirthDate.getEditableText().toString().trim();
        String anniversaryDate = EditAnniversaryDate.getEditableText().toString();
        String address = EditAddress.getEditableText().toString().trim();

        if (fullName.length() <= 0) {
            Toast.makeText(AddCustomer.this, "Please Enter Name", LENGTH_LONG).show();
            return false;
        }

        else if (phoneNumber.length() != 10) {
            Toast.makeText(AddCustomer.this, "Please Enter Valid Phone Number", LENGTH_LONG).show();
            return false;
        }

        else if (WphoneNumber.length() != 10) {
            Toast.makeText(AddCustomer.this, "Please Enter Valid Whats app Number", LENGTH_LONG).show();
            return false;
        }

        else if (birthDate.length() <= 0) {
            Toast.makeText(AddCustomer.this, "Please Enter Birthdate", LENGTH_LONG).show();
            return false;
        }

        else if (address.length() <= 0) {
            Toast.makeText(AddCustomer.this, "Please Enter address", LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
}

