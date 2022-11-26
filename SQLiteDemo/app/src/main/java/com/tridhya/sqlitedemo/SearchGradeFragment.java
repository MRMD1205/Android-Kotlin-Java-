package com.tridhya.sqlitedemo;

import static com.tridhya.sqlitedemo.MainActivity.dbHandler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchGradeFragment extends Fragment {
    RecyclerView recyclerView;
    SearchView searchView;
    StudentDetailsAdapter studentDetailsAdapter;
    StudentDetailsData studentDetailsData;
    List<StudentDetailsData> studentDetailsDataList = new ArrayList<>();

    public SearchGradeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_grade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    studentDetailsData=null;
                    studentDetailsDataList.clear();
                    List<StudentDetailsData> list = dbHandler.fetchStudentDetails();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStudentId().equals(query.trim())) {
                            studentDetailsData = list.get(i);
                            break;
                        }
                    }
                    if (studentDetailsData != null) {
                        studentDetailsDataList.add(studentDetailsData);
                        setAdapter(studentDetailsDataList);
                    } else {
                        studentDetailsDataList.clear();
                        recyclerView.setAdapter(null);
                    }
                } else {
                    studentDetailsDataList.clear();
                    recyclerView.setAdapter(null);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setAdapter(List<StudentDetailsData> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        studentDetailsAdapter = new StudentDetailsAdapter(list);
        recyclerView.setAdapter(studentDetailsAdapter);
    }
}