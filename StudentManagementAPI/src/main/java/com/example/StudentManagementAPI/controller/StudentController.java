package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.dto.StudentRequest;
import com.example.StudentManagementAPI.entity.Student;
import com.example.StudentManagementAPI.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Map<String, Object>> addStudent(@Valid @RequestBody StudentRequest request) {

        Student savedStudent = service.addStudent(request);

        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "Student Added Successfully");
        response.put("student", savedStudent);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
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
                                                 @Valid @RequestBody StudentRequest request){

        Student updatedStudent = service.updateStudent(id, request);

        return ResponseEntity.ok(updatedStudent);

    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Student> activateStudent(@PathVariable int id){

        return ResponseEntity.ok(service.activateStudent(id));

    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Student> deactivateStudent(@PathVariable int id){

        return ResponseEntity.ok(service.deactivateStudent(id));

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

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Student>> getStudentsByStatus(@PathVariable String status){

        return ResponseEntity.ok(service.getStudentsByStatus(status));

    }

}