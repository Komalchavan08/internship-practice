package com.example.StudentManagementAPI.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "student")

public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int studentId;

    private String studentName;

    private String email;

    private String department;

    private String city;

    private int age;

    private String course;

    public Student(){

    }

    public Student(int studentId, String studentName, String email,
                   String department, String city, int age, String course) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.department = department;
        this.city = city;
        this.age = age;
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStudentId(){
        return studentId;
    }

    public void setStudentId(int studentId)
    {
        this.studentId = studentId;
    }

    public String getStudentName(){
        return studentName;
    }

    public void setStudentName(String studentName){
        this.studentName = studentName;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public String getCourse(){
        return course;
    }

    public void setCourse(String course){
        this.course = course;
    }

}
