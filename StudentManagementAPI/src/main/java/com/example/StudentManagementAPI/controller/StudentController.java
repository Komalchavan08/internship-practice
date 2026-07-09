package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.entity.Student;
import com.example.StudentManagementAPI.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/students")

public class StudentController {

    @Autowired
    private StudentService service;

    @PostMapping
    public Student addStudent(@RequestBody Student student){
        return service.addStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // Get Student By ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        return service.getStudentById(id);
    }

    // Update Student
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable int id,
                                 @RequestBody Student student) {

        return service.updateStudent(id, student);
    }

    // Delete Student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {

        return service.deleteStudent(id);
    }

    @GetMapping("/search/name/{studentName}")
    public List<Student> searchByName(@PathVariable String studentName) {

        return service.searchByName(studentName);

    }

    @GetMapping("/search/email/{email}")
    public List<Student> searchByEmail(@PathVariable String email) {
        return service.searchByEmail(email);
    }

    @GetMapping("/search/department/{department}")
    public List<Student> searchByDepartment(@PathVariable String department){
        return service.searchByDepartment(department);
    }

    @GetMapping("/search/city/{city}")
    public List<Student> searchByCity(@PathVariable String city){
        return service.searchByCity(city);
    }

    @GetMapping("/pagination")
    public Page<Student> getStudentsWithPagination(
            @RequestParam int page,
            @RequestParam int size) {

        return service.getStudentsWithPagination(page, size);

    }

    @GetMapping("/sort/asc/{field}")
    public List<Student> sortAscending(@PathVariable String field) {

        return service.getStudentsAscending(field);

    }

    @GetMapping("/sort/desc/{field}")
    public List<Student> sortDescending(@PathVariable String field) {

        return service.getStudentDescending(field);
    }

    @GetMapping("/filter")
    public List<Student> filterStudents(
            @RequestParam String department,
            @RequestParam String city) {

        return service.filterByDepartmentAndCity(department, city);

    }

    @PostMapping("/signup")
    public String signUp(@RequestBody Student student) {

        return service.signUp(student);
    }

    @PostMapping("/login")
    public String login(@RequestBody Student student){

        return service.login(student.getEmail(), student.getPassword());

    }


    @PostMapping("/logout")
    public  String logout(){
        return service.logout();
    }

}

