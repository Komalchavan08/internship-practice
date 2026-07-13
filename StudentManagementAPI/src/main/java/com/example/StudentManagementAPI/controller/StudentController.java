package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.entity.Student;
import com.example.StudentManagementAPI.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/students")

public class StudentController {

    @Autowired
    private StudentService service;

    @PostMapping
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student){

        Student savedStudent = service.addStudent(student);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedStudent);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(){

        return ResponseEntity.ok(service.getAllStudents());

    }

    // Get Student By ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id){

        return ResponseEntity.ok(service.getStudentById(id));

    }

    // Update Student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id,
                                                 @Valid @RequestBody Student student){

        Student updatedStudent = service.updateStudent(id,student);

        return ResponseEntity.ok(updatedStudent);

    }

    // Delete Student
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteStudent(@PathVariable int id){

        service.deleteStudent(id);

        Map<String,String> response = new HashMap<>();

        response.put("message","Student Deleted Successfully");

        return ResponseEntity.ok(response);

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
    public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody Student student){

        String message = service.signUp(student);

        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.CREATED.value());
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Student student){

        String message = service.login(student.getEmail(), student.getPassword());

        Map<String,Object> response = new HashMap<>();

        response.put("status", HttpStatus.OK.value());
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String,Object>> logout(){

        String message = service.logout();

        Map<String,Object> response = new HashMap<>();

        response.put("status", HttpStatus.OK.value());
        response.put("message", message);

        return ResponseEntity.ok(response);

    }

}

