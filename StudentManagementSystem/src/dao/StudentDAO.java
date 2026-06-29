package dao;

import model.Student;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDAO {

    // Add Student
    public void addStudent(Student student) {

        if(studentExists(student.getStudentId())){

            System.out.println("Student ID already exists.");

            return;
        }

        String query = "INSERT INTO Student VALUES (?, ?, ?, ?)";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, student.getStudentId());
            ps.setString(2, student.getStudentName());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getCourse());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✓ Student Added Successfully!");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean studentExists(int studentId) {

        String query = "SELECT * FROM Student WHERE student_id = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            con.close();

            return exists;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }

    // View Students
    public void viewStudents() {

        String query = "SELECT * FROM Student";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n------ Student List ------");

            while (rs.next()) {

                System.out.println("--------------------------------");
                System.out.println("ID      : " + rs.getInt("student_id"));
                System.out.println("Name    : " + rs.getString("name"));
                System.out.println("Age     : " + rs.getInt("age"));
                System.out.println("Course  : " + rs.getString("course"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search Student
    public void searchStudent(int studentId) {

        String query = "SELECT * FROM Student WHERE student_id = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\n------ Student Found ------");
                System.out.println("ID      : " + rs.getInt("student_id"));
                System.out.println("Name    : " + rs.getString("Name"));
                System.out.println("Age     : " + rs.getInt("age"));
                System.out.println("Course  : " + rs.getString("course"));

            } else {

                System.out.println("Student Not Found!");

            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update Student
    public void updateStudent(Student student) {

        String query = "UPDATE Student SET Name=?, age=?, course=? WHERE student_id=?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, student.getStudentName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getCourse());
            ps.setInt(4, student.getStudentId());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("✓ Student updated successfully.");

            } else {

                System.out.println("No student found with ID: " + student.getStudentId());

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // Delete Student
    public void deleteStudent(int studentId) {

        String query = "DELETE FROM Student WHERE student_id = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, studentId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("✓ Student deleted successfully.");

            } else {

                System.out.println("No student found with ID: " + studentId);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}