package com.example.feelings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class billing extends AppCompatActivity {

    private TextView setDate,setPrice, setPhno, setQtyTotal, setGrandTotal, setPayableAmount, setDiscountPrice;
    private EditText mQty, mDiscountRate;
    private Button createBill;
    AutoCompleteTextView setName, setService;
    DatabaseReference reference;
    DatabaseReference serviceReference;
    ArrayList<String> customers;
    ArrayList<String> services;
    ArrayAdapter<String> customerAdapter;
    ArrayAdapter<String> serviceAdapter;
    Bitmap bitmap, scaledbmp;
    int pageWidth = 1200;
    DateFormat dateFormat;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        setName = (AutoCompleteTextView) findViewById(R.id.set_name);
        setService = (AutoCompleteTextView) findViewById(R.id.bill_service);
        setDate = (TextView) findViewById(R.id.set_date);
        setPrice= (TextView) findViewById(R.id.bill_service_price);
        setPhno = (TextView) findViewById(R.id.set_phno);
        mQty = (EditText) findViewById(R.id.bill_qty);
        setQtyTotal = (TextView) findViewById(R.id.qty_total);
        mDiscountRate = (EditText) findViewById(R.id.discount_rate);
        setGrandTotal = (TextView) findViewById(R.id.totalAmount);
        setDiscountPrice = (TextView) findViewById(R.id.discount_price);
        setPayableAmount = (TextView) findViewById(R.id.PayableAmount);
        createBill = (Button) findViewById(R.id.create_bill);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bitmap, 400, 150, false);
        
        final String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        setDate.setText(currentDate);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customers = new ArrayList<String>();
                  for (DataSnapshot snapshot : dataSnapshot.getChildren())
                     {
                         Users users = snapshot.getValue(Users.class);
                         String name= users.getFullName();
                         customers.add(name);
                    }
                      customerAdapter = new ArrayAdapter<String>(billing.this, android.R.layout.simple_list_item_1, customers);
                      setName.setAdapter(customerAdapter);


                      setName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                          @Override
                          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                              String name = setName.getText().toString();
                              Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("fullName").equalTo(name);
                              query.addValueEventListener(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                      {
                                          Users users = snapshot.getValue(Users.class);
                                          String phno = users.getPhoneNumber();
                                          setPhno.setText(phno);
                                      }

                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                  }
                              });
                          }
                      });
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

       
        serviceReference = FirebaseDatabase.getInstance().getReference().child("Services");

        serviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Services service = snapshot.getValue(Services.class);
                    String name= service.getServiceName();
                    services.add(name);
                }
                serviceAdapter = new ArrayAdapter<String>(billing.this, android.R.layout.simple_list_item_1, services);
                setService.setAdapter(serviceAdapter);

                setService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String service = setService.getText().toString();
                        Query query = FirebaseDatabase.getInstance().getReference("Services").orderByChild("serviceName").equalTo(service);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                {
                                    Services service = snapshot.getValue(Services.class);
                                    String price = service.getServicePrice();
                                    setPrice.setText(price);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String sQty = mQty.getText().toString();
                String sPrice = setPrice.getText().toString();

                if(!sQty.isEmpty())
                {
                    try
                    {
                        int qty = Integer.parseInt(sQty);
                        int price = Integer.parseInt(sPrice);
                        int total = (price * qty);
                        setQtyTotal.setText(String.valueOf(total));
                        setGrandTotal.setText(String.valueOf(total));

                    }catch (NumberFormatException e)
                    {
                        Toast.makeText(billing.this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        mDiscountRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String dRate = mDiscountRate.getText().toString();
                String sTotal = setQtyTotal.getText().toString();

                if(!dRate.isEmpty())
                {
                    try
                    {
                        int discountRate = Integer.parseInt(dRate);
                        int total = Integer.parseInt(sTotal);
                        double rate = (discountRate/100.0);
                        double discount = (total*rate);
                        double grandTotal = (total-discount);
                        setDiscountPrice.setText(String.valueOf(discount));
                        setPayableAmount.setText(String.valueOf(grandTotal));
                    }catch (NumberFormatException e)
                    {
                        Toast.makeText(billing.this, "Invalid Amount", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();
    }

    private void createPDF(){
        createBill.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                date = new Date();

                if (setName.getText().toString().length() == 0 ||
                        setPhno.getText().toString().length() == 0 ||
                        setService.getText().toString().length() == 0 ||
                        setPrice.getText().toString().length() == 0 ||
                        mQty.getText().toString().length() == 0 ||
                        setQtyTotal.getText().toString().length() == 0 ||
                        setGrandTotal.getText().toString().length() == 0 ||
                        setDiscountPrice.getText().toString().length() == 0 ||
                        mDiscountRate.getText().toString().length() == 0 ||
                        setPayableAmount.getText().toString().length() == 0) {
                    Toast.makeText(billing.this, "All fields are mendatory...", Toast.LENGTH_SHORT).show();
                } else {
                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();
                    Paint titlePaint = new Paint();

                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = myPage1.getCanvas();

                    canvas.drawBitmap(scaledbmp, 750, 125, myPaint);

                    titlePaint.setTextAlign(Paint.Align.LEFT);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(50);
                    canvas.drawText("Feelings Beauty Care", 50, 150, titlePaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Jamnagar", 50, 195, myPaint);
                    canvas.drawText("Phone no.: 9924269898", 50, 235, myPaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(70);
                    titlePaint.setColor(Color.rgb(139,0,139));
                    canvas.drawText("Invoice", pageWidth / 2, 400, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.LEFT);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setColor(Color.BLACK);
                    titlePaint.setTextSize(35);
                    canvas.drawText("Bill To :", 50, 540, titlePaint);
                    canvas.drawText(String.valueOf(setName.getText()), 50, 590, titlePaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Contact No: " + setPhno.getText(), 50, 640, myPaint);

                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Date: " + setDate.getText(), pageWidth - 50, 540, myPaint);

                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    canvas.drawText("Time: " + dateFormat.format(date), pageWidth - 50, 590, myPaint);


                    myPaint.setColor(Color.rgb(139,0,139));

                    myPaint.setColor(Color.rgb(139,0,139));
                    canvas.drawRect(50, 780, pageWidth - 50, 860, myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setStyle(Paint.Style.FILL);
                    myPaint.setColor(Color.WHITE);
                    canvas.drawText("Sr No.", 65, 830, myPaint);
                    canvas.drawText("Service", 200, 830, myPaint);
                    canvas.drawText("Price", 700, 830, myPaint);
                    canvas.drawText("Qty", 900, 830, myPaint);
                    canvas.drawText("Total", 1050, 830, myPaint);

                    canvas.drawLine(180, 790, 180, 840, myPaint);
                    canvas.drawLine(680, 790, 680, 840, myPaint);
                    canvas.drawLine(880, 790, 880, 840, myPaint);
                    canvas.drawLine(1030, 790, 1030, 840, myPaint);

                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("1.", 65, 950, myPaint);
                    canvas.drawText(setService.getText().toString(), 200, 950, myPaint);
                    canvas.drawText(setPrice.getText().toString(), 700, 950, myPaint);
                    canvas.drawText(mQty.getText().toString(), 900, 950, myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(setQtyTotal.getText().toString(), pageWidth - 70, 950, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);

                    canvas.drawLine(680, 1200, pageWidth - 20, 1200, myPaint);

                    canvas.drawText("Sub total", 700, 1250, myPaint);
                    canvas.drawText(":", 900, 1250, myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(setGrandTotal.getText().toString(), pageWidth - 40, 1250, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);

                    canvas.drawText("Discount Rate", 700, 1300, myPaint);
                    canvas.drawText(":", 900, 1300, myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(mDiscountRate.getText().toString(), pageWidth - 90, 1300, myPaint);
                    canvas.drawText("%", pageWidth - 40, 1300, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);

                    canvas.drawText("Discount Price", 700, 1350, myPaint);
                    canvas.drawText(":", 900, 1350, myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(setDiscountPrice.getText().toString(), pageWidth - 40, 1350, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);

                    myPaint.setColor(Color.rgb(139, 0, 139));
                    canvas.drawRect(680, 1400, pageWidth - 20, 1500, myPaint);

                    myPaint.setColor(Color.WHITE);
                    myPaint.setTextSize(50f);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Total", 700, 1465, myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(setPayableAmount.getText().toString(), pageWidth - 40, 1465, myPaint);

                    myPdfDocument.finishPage(myPage1);

                    String billerName = setName.getText().toString();
                    String filePath= Environment.getExternalStorageDirectory().getPath() +  "/"+billerName+".pdf";
                    File file = new File(filePath);

                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                        String number = setPhno.getText().toString();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone= 91" + number + "&text=" + "";
                        sendIntent.setData(Uri.parse(url));
                        startActivity(sendIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myPdfDocument.close();

                }
            }
        });

    }
}