package app;

import dao.StudentFileDAO;
import model.Student;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentFileDAO dao = new StudentFileDAO();

        while (true) {

            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.println("\n----- Add Student -----");

                    System.out.print("Enter Student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Student Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Course: ");
                    String course = sc.nextLine();

                    Student student = new Student(id, name, age, course);

                    dao.addStudent(student);

                    break;

                case 2:

                    System.out.println("\n----- View Students -----");

                    dao.viewStudents();

                    break;

                case 3:

                    System.out.println("\n----- Search Student -----");

                    System.out.print("Enter Student ID: ");

                    int searchId = sc.nextInt();

                    dao.searchStudent(searchId);

                    break;

                case 4:

                    System.out.println("\n----- Update Student -----");

                    System.out.print("Enter Student ID: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter New Name: ");
                    String updateName = sc.nextLine();

                    System.out.print("Enter New Age: ");
                    int updateAge = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter New Course: ");
                    String updateCourse = sc.nextLine();

                    Student updateStudent = new Student(updateId, updateName, updateAge, updateCourse);

                    dao.updateStudent(updateStudent);

                    break;

                case 5:

                    System.out.println("\n----- Delete Student -----");

                    System.out.print("Enter Student ID: ");

                    int deleteId = sc.nextInt();

                    dao.deleteStudent(deleteId);

                    break;

                case 6:

                    System.out.println("Thank You!");
                    sc.close();
                    System.exit(0);

                default:

                    System.out.println("Invalid Choice!");

            }

        }

    }

}