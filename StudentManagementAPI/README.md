# 🎓 Student Management REST API

A Spring Boot REST API for managing student records using MySQL and Spring Data JPA.

## 📖 About

This project was developed as part of my Java Full Stack internship to practice building REST APIs using Spring Boot, Spring Data JPA, and MySQL. It demonstrates CRUD operations, searching, filtering, pagination, and sorting through REST endpoints tested with Postman.

## 🚀 Features

- ✅ Add Student
- ✅ Get All Students
- ✅ Get Student By ID
- ✅ Update Student
- ✅ Delete Student
- ✅ Search by Name
- ✅ Search by Email
- ✅ Search by Department
- ✅ Search by City
- ✅ Filter by Department and City
- ✅ Pagination
- ✅ Sorting (Ascending & Descending)

## 🛠 Technologies Used

- Java 22
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Postman

## 📂 Project Structure

```
src
├── controller
├── entity
├── repository
├── service
└── resources
```

## 📌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/students` | Add Student |
| GET | `/students` | Get All Students |
| GET | `/students/{id}` | Get Student By ID |
| PUT | `/students/{id}` | Update Student |
| DELETE | `/students/{id}` | Delete Student |
| GET | `/students/search/name/{name}` | Search by Name |
| GET | `/students/search/email/{email}` | Search by Email |
| GET | `/students/search/department/{department}` | Search by Department |
| GET | `/students/search/city/{city}` | Search by City |
| GET | `/students/filter?department=IT&city=Pune` | Filter Students |
| GET | `/students/pagination?page=0&size=5` | Pagination |
| GET | `/students/sort/asc/studentName` | Sort Ascending |
| GET | `/students/sort/desc/studentName` | Sort Descending |

## 🗄 Database

**Database:** MySQL

**Database Name:** `StudentAPI_db`

## ▶️ How to Run

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Configure MySQL in `application.properties`.
4. Create the database:
   ```sql
   CREATE DATABASE StudentAPI_db;
   ```
5. Run the Spring Boot application.
6. Test APIs using Postman.

