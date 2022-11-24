package com.example.feelings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.core.Context;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button EditCustomerDetails, CustomerDetails, Message, BillingDetails, EditServiceDetails, ServiceDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(NetworkInformation.isConnected(MainActivity.this))
        {
            EditCustomerDetails = (Button) findViewById(R.id.edit_customer_details);
            CustomerDetails = (Button) findViewById(R.id.customer_details);
            Message = (Button) findViewById(R.id.Message_details);
            BillingDetails = (Button) findViewById(R.id.billing_details);
            EditServiceDetails = (Button) findViewById(R.id.add_service_details);
            ServiceDetails = (Button) findViewById(R.id.service_details);


            EditCustomerDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AddCustomer.class);
                    startActivity(intent);
                }
            });

            CustomerDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ViewDetails.class);
                    startActivity(intent);
                }
            });

            Message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MessageType.class);
                    startActivity(intent);
                }
            });

            BillingDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, billing.class);
                    startActivity(intent);
                }
            });

            EditServiceDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AddService.class);
                    startActivity(intent);
                }
            });

            ServiceDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ViewServiceDetails.class);
                    startActivity(intent);
                }
            });

        }else{
            Intent intent = new Intent(MainActivity.this, InternetAccess.class);
            startActivity(intent);
        }
    }
}
