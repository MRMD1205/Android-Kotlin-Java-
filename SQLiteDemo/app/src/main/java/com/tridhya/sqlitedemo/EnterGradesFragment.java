package com.tridhya.sqlitedemo;

import static com.tridhya.sqlitedemo.MainActivity.dbHandler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class EnterGradesFragment extends Fragment {
    private EditText studentName, program, course1, course2, course3, course4;
    Button btnAdd;

    public EnterGradesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_grades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studentName = view.findViewById(R.id.edit_student_name);
        program = view.findViewById(R.id.edit_program);
        course1 = view.findViewById(R.id.edit_course1);
        course2 = view.findViewById(R.id.edit_course2);
        course3 = view.findViewById(R.id.edit_course3);
        course4 = view.findViewById(R.id.edit_course4);
        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations()) {
                    // on below line we are calling a method to add grades to Grades table.
                    dbHandler.addGrades(studentName.getText().toString().trim(),
                            program.getText().toString().trim(),
                            course1.getText().toString().trim(),
                            course2.getText().toString().trim(),
                            course3.getText().toString().trim(),
                            course4.getText().toString().trim());

                    // after adding the data we are displaying a toast message.
                    Toast.makeText(getContext(), "Grades added successfully", Toast.LENGTH_SHORT).show();
                    studentName.setText("");
                    program.setText("");
                    course1.setText("");
                    course2.setText("");
                    course3.setText("");
                    course4.setText("");
                }
            }
        });
    }

    private boolean validations() {
        if (studentName.getText().toString().trim().isEmpty()) {
            Snackbar.make(getView(), "Please enter student name", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (program.getText().toString().trim().isEmpty()) {
            Snackbar.make(getView(), "Please enter program", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (course1.getText().toString().trim().isEmpty()) {
            Snackbar.make(getView(), "Please enter marks for course 1", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (Integer.parseInt(course1.getText().toString().trim()) < 0 || Integer.parseInt(course1.getText().toString().trim()) > 100) {
            Snackbar.make(getView(), "Please enter valid marks for course 1. Maximum marks can be 100", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (course2.getText().toString().trim().isEmpty()) {
            Snackbar.make(getView(), "Please enter marks for course 2", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (Integer.parseInt(course2.getText().toString().trim()) < 0 || Integer.parseInt(course2.getText().toString().trim()) > 100) {
            Snackbar.make(getView(), "Please enter valid marks for course 2. Maximum marks can be 100", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (course3.getText().toString().trim().isEmpty()) {
            Snackbar.make(getView(), "Please enter marks for course 3", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (Integer.parseInt(course3.getText().toString().trim()) < 0 || Integer.parseInt(course3.getText().toString().trim()) > 100) {
            Snackbar.make(getView(), "Please enter valid marks for course 3. Maximum marks can be 100", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (course4.getText().toString().trim().isEmpty()) {
            Snackbar.make(getView(), "Please enter marks for course 4", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (Integer.parseInt(course4.getText().toString().trim()) < 0 || Integer.parseInt(course4.getText().toString().trim()) > 100) {
            Snackbar.make(getView(), "Please enter valid marks for course 4. Maximum marks can be 100", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}