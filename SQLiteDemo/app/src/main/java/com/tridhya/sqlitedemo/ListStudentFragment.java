package com.tridhya.sqlitedemo;

import static com.tridhya.sqlitedemo.MainActivity.dbHandler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListStudentFragment extends Fragment {

    RecyclerView recyclerView;
    StudentDetailsAdapter studentDetailsAdapter;
    List<StudentDetailsData> list;

    public ListStudentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);

        list = dbHandler.fetchStudentDetails();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        studentDetailsAdapter = new StudentDetailsAdapter(list);
        recyclerView.setAdapter(studentDetailsAdapter);
    }
}