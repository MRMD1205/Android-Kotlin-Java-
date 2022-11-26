package com.tridhya.sqlitedemo;

import static com.tridhya.sqlitedemo.MainActivity.dbHandler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EnterImprovementsFragment extends Fragment {

    private Spinner spinner;
    private EditText studentId, marks;
    private Button btnAdd;
    private StudentDetailsData data;
    private String courseName;
    private String enteredMarks;
    private ArrayList<String> improvementList;

    public EnterImprovementsFragment() {
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
        return inflater.inflate(R.layout.fragment_enter_improvements, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get the spinner from the xml.
        spinner = view.findViewById(R.id.spinner);
        studentId = view.findViewById(R.id.edit_student_id);
        marks = view.findViewById(R.id.edit_marks);
        btnAdd = view.findViewById(R.id.btnAdd);
//create a list of items for the spinner.
        String[] items = new String[]{"Course 1", "Course 2", "Course 3", "Course 4"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);


        //below will get improvement details from Improvement table
//        improvementList = dbHandler.fetchImprovements();
//        Snackbar.make(getView(), "Improvement List size :" + improvementList.size(), Snackbar.LENGTH_LONG).show();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations()) {
                    List<StudentDetailsData> list = dbHandler.fetchStudentDetails();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStudentId().equals(studentId.getText().toString().trim())) {
                            data = list.get(i);
                            break;
                        }
                    }

                    if (data != null) {
                        if (spinner.getSelectedItem().toString().equalsIgnoreCase("Course 1")) {
                            if (Integer.parseInt(data.getCourse1()) + Integer.parseInt(marks.getText().toString()) > 100) {
                                Snackbar.make(getView(), "Course marks should not exceeds 100.\nCurrent " + spinner.getSelectedItem().toString() + " are " + data.getCourse1(), Snackbar.LENGTH_LONG).show();
                            } else {
                                updateData(Integer.parseInt(data.getCourse1()) + Integer.parseInt(marks.getText().toString()));
                            }
                        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Course 2")) {
                            if (Integer.parseInt(data.getCourse2()) + Integer.parseInt(marks.getText().toString()) > 100) {
                                Snackbar.make(getView(), "Course marks should not exceeds 100.\nCurrent " + spinner.getSelectedItem().toString() + " are " + data.getCourse2(), Snackbar.LENGTH_LONG).show();
                            } else {
                                updateData(Integer.parseInt(data.getCourse2()) + Integer.parseInt(marks.getText().toString()));
                            }
                        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Course 3")) {
                            if (Integer.parseInt(data.getCourse3()) + Integer.parseInt(marks.getText().toString()) > 100) {
                                Snackbar.make(getView(), "Course marks should not exceeds 100.\nCurrent " + spinner.getSelectedItem().toString() + " are " + data.getCourse3(), Snackbar.LENGTH_LONG).show();
                            } else {
                                updateData(Integer.parseInt(data.getCourse3()) + Integer.parseInt(marks.getText().toString()));
                            }
                        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Course 4")) {
                            if (Integer.parseInt(data.getCourse4()) + Integer.parseInt(marks.getText().toString()) > 100) {
                                Snackbar.make(getView(), "Course marks should not exceeds 100.\nCurrent " + spinner.getSelectedItem().toString() + " are " + data.getCourse4(), Snackbar.LENGTH_LONG).show();
                            } else {
                                updateData(Integer.parseInt(data.getCourse4()) + Integer.parseInt(marks.getText().toString()));
                            }
                        }
                    } else {
                        Snackbar.make(getView(), "No records found with entered student Id", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void updateData(int value) {
        dbHandler.updateImprovements(studentId.getText().toString().trim(), spinner.getSelectedItem().toString(), String.valueOf(value));
        dbHandler.addImprovements(studentId.getText().toString().trim(), spinner.getSelectedItem().toString(), String.valueOf(value));
        Snackbar.make(getView(), "Data entered successfully", Snackbar.LENGTH_LONG).show();
    }

    private boolean validations() {
        if (studentId.getText().toString().trim().isEmpty()) {
            Snackbar.make(getView(), "Please enter student Id", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (marks.getText().toString().trim().isEmpty()) {
            Snackbar.make(getView(), "Please enter marks", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (Integer.parseInt(marks.getText().toString().trim()) < 0 || Integer.parseInt(marks.getText().toString().trim()) > 100) {
            Snackbar.make(getView(), "Please enter valid marks. Maximum marks can be 100", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}