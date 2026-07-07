package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{

    List<Student> findByStudentName(String studentName);
    List<Student> findByEmail(String email);
    List<Student> findByDepartment(String department);
    List<Student> findByCity(String city);

    List<Student> findByDepartmentAndCity(String department, String city);
}
