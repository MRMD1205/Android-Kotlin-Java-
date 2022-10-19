package com.example.materialconcepts.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.materialconcepts.dialog.BottomSheetDialog;
import com.example.materialconcepts.R;
import com.example.materialconcepts.models.Student;
import com.example.materialconcepts.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {

    private ActivityMainBinding activityMainBinding;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Student student = new Student("Mayank", "CE");
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setStudent(student);

        activityMainBinding.floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet");
            }
        });
    }

    @Override
    public void onButtonClick(int i) {
        activityMainBinding.imgAppBar.setImageResource(i);
        Toast.makeText(this, "Image Changed...", Toast.LENGTH_SHORT).show();
        bottomSheetDialog.dismiss();
    }
}