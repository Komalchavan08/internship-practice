package model;

public class Student {

    // Instance Variables
    private int studentId;
    private String studentName;
    private int age;
    private String course;

    // Default Constructor
    public Student() {

    }

    // Parameterized Constructor
    public Student(int studentId, String studentName, int age, String course) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.age = age;
        this.course = course;
    }

    // Getter for Student ID
    public int getStudentId() {
        return studentId;
    }

    // Setter for Student ID
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    // Getter for Student Name
    public String getStudentName() {
        return studentName;
    }

    // Setter for Student Name
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    // Getter for Age
    public int getAge() {
        return age;
    }

    // Setter for Age
    public void setAge(int age) {
        this.age = age;
    }

    // Getter for Course
    public String getCourse() {
        return course;
    }

    // Setter for Course
    public void setCourse(String course) {
        this.course = course;
    }
}
