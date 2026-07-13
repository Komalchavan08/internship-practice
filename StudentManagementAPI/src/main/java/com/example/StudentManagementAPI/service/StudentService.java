package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.entity.Student;
import com.example.StudentManagementAPI.exception.EmailAlreadyExistsException;
import com.example.StudentManagementAPI.exception.StudentNotFoundException;
import com.example.StudentManagementAPI.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    // Add Student
    public Student addStudent(Student student) {
        return repository.save(student);
    }

    // Get All Students
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // Get Student By ID
    public Student getStudentById(int id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with ID : " + id));
    }

    // Update Student
    public Student updateStudent(int id, Student student) {

        Student updateStudent = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with ID : " + id));

        updateStudent.setStudentName(student.getStudentName());
        updateStudent.setAge(student.getAge());
        updateStudent.setCourse(student.getCourse());
        updateStudent.setDepartment(student.getDepartment());
        updateStudent.setCity(student.getCity());
        updateStudent.setEmail(student.getEmail());
        updateStudent.setPassword(student.getPassword());

        return repository.save(updateStudent);
    }

    // Delete Student
    public String deleteStudent(int id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with ID : " + id));

        repository.delete(student);

        return "Student Deleted Successfully";
    }

    // Search By Name
    public List<Student> searchByName(String studentName) {
        return repository.findByStudentName(studentName);
    }

    // Search By Email
    public List<Student> searchByEmail(String email) {
        return repository.findByEmail(email);
    }

    // Search By Department
    public List<Student> searchByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    // Search By City
    public List<Student> searchByCity(String city) {
        return repository.findByCity(city);
    }

    // Pagination
    public Page<Student> getStudentsWithPagination(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findAll(pageable);
    }

    // Sorting Ascending
    public List<Student> getStudentsAscending(String field) {

        return repository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    // Sorting Descending
    public List<Student> getStudentDescending(String field) {

        return repository.findAll(Sort.by(Sort.Direction.DESC, field));
    }

    // Filter
    public List<Student> filterByDepartmentAndCity(String department, String city) {

        return repository.findByDepartmentAndCity(department, city);
    }

    // Signup
    public String signUp(Student student) {

        if (repository.existsByEmail(student.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        repository.save(student);

        return "Student Registered Successfully";
    }

    // Login
    public String login(String email, String password) {

        Optional<Student> student = repository.findStudentByEmail(email);

        if (student.isPresent()) {

            if (student.get().getPassword().equals(password)) {
                return "Login Successful";
            } else {
                return "Incorrect Password";
            }

        }

        return "Email Not Found";
    }

    // Logout
    public String logout() {
        return "Logout Successful";
    }

}