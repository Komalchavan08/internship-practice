package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.dto.StudentRequest;
import com.example.StudentManagementAPI.entity.Student;
import com.example.StudentManagementAPI.entity.User;
import com.example.StudentManagementAPI.exception.StudentNotFoundException;
import com.example.StudentManagementAPI.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditLogService auditLogService;

    // Add Student — creates the login identity (User, role=STUDENT) and the
    // academic profile (Student) together, linked, in one transaction.
    @Transactional
    public Student addStudent(StudentRequest request) {

        User user = new User();
        user.setName(request.getStudentName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setMobile(request.getMobile());
        user.setDob(request.getDob());
        user.setAddress(request.getAddress());

        User savedUser = userService.createUserWithRole(user, "STUDENT");

        Student student = new Student();
        student.setDepartment(request.getDepartment());
        student.setCity(request.getCity());
        student.setAge(request.getAge());
        student.setCourse(request.getCourse());
        student.setStatus("ACTIVE");
        student.setUser(savedUser);

        Student saved = repository.save(student);

        auditLogService.log("CREATE", "Student", String.valueOf(saved.getStudentId()),
                "Added student: " + request.getStudentName());

        return saved;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudentById(int id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with ID : " + id));
    }

    public List<Student> getStudentsByStatus(String status){

        return repository.findByStatus(status);

    }

    // Update Student — updates both the linked User's login info and the
    // Student's academic fields.
    @Transactional
    public Student updateStudent(int id, StudentRequest request) {

        Student existing = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with ID : " + id));

        User user = existing.getUser();
        user.setName(request.getStudentName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setMobile(request.getMobile());
        user.setDob(request.getDob());
        user.setAddress(request.getAddress());
        userService.updateUser(user);

        existing.setAge(request.getAge());
        existing.setCourse(request.getCourse());
        existing.setDepartment(request.getDepartment());
        existing.setCity(request.getCity());

        Student saved = repository.save(existing);

        auditLogService.log("UPDATE", "Student", String.valueOf(id),
                "Updated student: " + request.getStudentName());

        return saved;
    }

    // Delete Student — removes the Student profile, then its linked User.
    @Transactional
    public String deleteStudent(int id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with ID : " + id));

        User user = student.getUser();
        String studentName = (user != null) ? user.getName() : ("Student #" + id);

        repository.delete(student);

        if (user != null) {
            userService.deleteUser(user);
        }

        auditLogService.log("DELETE", "Student", String.valueOf(id),
                "Deleted student: " + studentName);

        return "Student Deleted Successfully";
    }

    public List<Student> searchByName(String studentName) {
        return repository.findByUser_NameContainingIgnoreCase(studentName);
    }

    public List<Student> searchByEmail(String email) {
        return repository.findByUser_Email(email);
    }

    public List<Student> searchByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    public List<Student> searchByCity(String city) {
        return repository.findByCity(city);
    }

    public Page<Student> getStudentsWithPagination(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findAll(pageable);
    }

    public List<Student> getStudentsAscending(String field) {

        return repository.findAll(Sort.by(Sort.Direction.ASC, resolveSortField(field)));
    }

    public List<Student> getStudentDescending(String field) {

        return repository.findAll(Sort.by(Sort.Direction.DESC, resolveSortField(field)));
    }

    private String resolveSortField(String field) {
        if ("studentName".equals(field)) {
            return "user.name";
        }
        if ("email".equals(field)) {
            return "user.email";
        }
        return field;
    }

    public List<Student> filterByDepartmentAndCity(String department, String city) {

        return repository.findByDepartmentAndCity(department, city);
    }

    public Student activateStudent(int id){

        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));

        student.setStatus("ACTIVE");

        return repository.save(student);

    }

    public Student deactivateStudent(int id){

        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));

        student.setStatus("INACTIVE");

        return repository.save(student);

    }

}