package dao;

import model.Student;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StudentFileDAO {

    private final String FILE_NAME = "students.txt";

    public void addStudent(Student student) {

        try {

            FileWriter writer = new FileWriter(FILE_NAME, true);

            writer.write(
                    student.getStudentId() + "," +
                            student.getStudentName() + "," +
                            student.getAge() + "," +
                            student.getCourse() +
                            "\n"
            );

            writer.close();

            System.out.println("✓ Student added successfully.");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // View Students
    public void viewStudents() {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));

            String line;

            System.out.println("\n----- Student List -----");

            while ((line = reader.readLine()) != null) {

                System.out.println(line);

            }

            reader.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // Search Student
    public void searchStudent(int studentId) {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);

                if (id == studentId) {

                    System.out.println("\n----- Student Found -----");
                    System.out.println("ID      : " + data[0]);
                    System.out.println("Name    : " + data[1]);
                    System.out.println("Age     : " + data[2]);
                    System.out.println("Course  : " + data[3]);

                    found = true;
                    break;
                }

            }

            if (!found) {
                System.out.println("No student found with ID: " + studentId);
            }

            reader.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // Update Student
    public void updateStudent(Student student) {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));

            ArrayList<String> students = new ArrayList<>();

            String line;

            boolean found = false;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);

                if (id == student.getStudentId()) {

                    line = student.getStudentId() + "," +
                            student.getStudentName() + "," +
                            student.getAge() + "," +
                            student.getCourse();

                    found = true;
                }

                students.add(line);

            }

            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));

            for (String s : students) {

                writer.write(s);
                writer.newLine();

            }

            writer.close();

            if (found) {

                System.out.println("✓ Student updated successfully.");

            } else {

                System.out.println("No student found with ID: " + student.getStudentId());

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // Delete Student
    public void deleteStudent(int studentId) {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));

            ArrayList<String> students = new ArrayList<>();

            String line;

            boolean found = false;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);

                if (id != studentId) {

                    students.add(line);

                } else {

                    found = true;

                }

            }

            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));

            for (String s : students) {

                writer.write(s);
                writer.newLine();

            }

            writer.close();

            if (found) {

                System.out.println("✓ Student deleted successfully.");

            } else {

                System.out.println("No student found with ID: " + studentId);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}