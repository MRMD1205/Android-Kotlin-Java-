package com.tridhya.sqlitedemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentDetailsAdapter extends RecyclerView.Adapter<StudentDetailsAdapter.StudentDetailsViewHolder> {
    private List<StudentDetailsData> list;

    // data is passed into the constructor
    StudentDetailsAdapter(List<StudentDetailsData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StudentDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_details, parent, false);
        return new StudentDetailsViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDetailsViewHolder holder, int position) {
        StudentDetailsData data = list.get(position);
        holder.studentId.setText("Student ID : " + data.getStudentId());
        holder.studentName.setText("Student Name : " + data.getStudentName());
        holder.program.setText("Program : " + data.getProgram());
        holder.course1.setText("Course 1 : " + data.getCourse1());
        holder.course2.setText("Course 2 : " + data.getCourse2());
        holder.course3.setText("Course 3 : " + data.getCourse3());
        holder.course4.setText("Course 4 : " + data.getCourse4());

        int totalMarks = Integer.parseInt(data.getCourse1())
                + Integer.parseInt(data.getCourse2())
                + Integer.parseInt(data.getCourse3())
                + Integer.parseInt(data.getCourse4());
        holder.total.setText("Total Marks : " + totalMarks);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView studentId;
        TextView studentName;
        TextView program;
        TextView course1;
        TextView course2;
        TextView course3;
        TextView course4;
        TextView total;

        public StudentDetailsViewHolder(View mView) {
            super(mView);
            studentId = mView.findViewById(R.id.text_student_id);
            studentName = mView.findViewById(R.id.text_student_name);
            program = mView.findViewById(R.id.text_program);
            course1 = mView.findViewById(R.id.text_course1);
            course2 = mView.findViewById(R.id.text_course2);
            course3 = mView.findViewById(R.id.text_course3);
            course4 = mView.findViewById(R.id.text_course4);
            total = mView.findViewById(R.id.text_total);
        }
    }
}
