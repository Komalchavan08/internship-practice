package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Name and email now live on the linked User, not on Student directly.
    List<Student> findByUser_NameContainingIgnoreCase(String name);

    List<Student> findByUser_Email(String email);

    List<Student> findByDepartment(String department);

    List<Student> findByStatus(String status);

    long countByStatus(String status);

    List<Student> findByCity(String city);

    List<Student> findByDepartmentAndCity(String department, String city);

    java.util.Optional<Student> findByUser_UserId(int userId);
}