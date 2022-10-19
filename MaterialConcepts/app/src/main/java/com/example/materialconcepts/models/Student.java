package com.example.materialconcepts.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.materialconcepts.BR;

public class Student extends BaseObservable {

    private int id;
    private String name;
    private String dept;

    public Student(String name, String dept) {
        this.name = name;
        this.dept = dept;
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
        notifyPropertyChanged(BR.dept);
    }
}
