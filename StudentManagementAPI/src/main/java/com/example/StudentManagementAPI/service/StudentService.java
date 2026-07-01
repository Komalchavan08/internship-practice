package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.entity.Student;
import com.example.StudentManagementAPI.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        Optional<Student> student = repository.findById(id);

        if (student.isPresent()) {
            return student.get();
        }

        return null;
    }

    // Update Student
    public Student updateStudent(int id, Student student) {

        Optional<Student> existingStudent = repository.findById(id);

        if (existingStudent.isPresent()) {

            Student updateStudent = existingStudent.get();

            updateStudent.setStudentName(student.getStudentName());
            updateStudent.setAge(student.getAge());
            updateStudent.setCourse(student.getCourse());

            return repository.save(updateStudent);
        }

        return null;
    }

    // Delete Student
    public String deleteStudent(int id) {

        if (repository.existsById(id)) {

            repository.deleteById(id);

            return "Student Deleted Successfully";

        }

        return "Student Not Found";
    }
}
