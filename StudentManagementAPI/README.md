# 🎓 Student Management REST API

A Spring Boot REST API for managing student records using MySQL and Spring Data JPA, along with a simple HTML, CSS, and JavaScript frontend for Student Authentication.

## 📖 About

This project was developed as part of my Java Full Stack internship to practice building REST APIs using Spring Boot, Spring Data JPA, and MySQL. It demonstrates CRUD operations, searching, filtering, pagination, sorting, and a basic authentication system (Signup, Login, Logout) with a simple frontend integrated with the backend.

## 🚀 Features

### Student Management

- ✅ Add Student
- ✅ Get All Students
- ✅ Get Student By ID
- ✅ Update Student
- ✅ Delete Student

### Search & Filter

- ✅ Search by Name
- ✅ Search by Email
- ✅ Search by Department
- ✅ Search by City
- ✅ Filter by Department and City

### Pagination & Sorting

- ✅ Pagination
- ✅ Sorting (Ascending)
- ✅ Sorting (Descending)

### Authentication

- ✅ Student Signup
- ✅ Student Login
- ✅ Student Logout

### Frontend

- ✅ Signup Page
- ✅ Login Page
- ✅ Dashboard
- ✅ Form Validation
- ✅ Dashboard Protection using Local Storage

---

## 🛠 Technologies Used

- Java 22
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- HTML5
- CSS3
- JavaScript
- Maven
- Postman

---

## 📂 Project Structure

```
src
├── controller
├── entity
├── repository
├── service
└── resources
    ├── static
    │   ├── signup.html
    │   ├── login.html
    │   ├── dashboard.html
    │   ├── style.css
    │   ├── signup.js
    │   └── login.js
    └── application.properties
```

---

## 📌 API Endpoints

### Student APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/students` | Add Student |
| GET | `/students` | Get All Students |
| GET | `/students/{id}` | Get Student By ID |
| PUT | `/students/{id}` | Update Student |
| DELETE | `/students/{id}` | Delete Student |

### Search APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/students/search/name/{name}` | Search by Name |
| GET | `/students/search/email/{email}` | Search by Email |
| GET | `/students/search/department/{department}` | Search by Department |
| GET | `/students/search/city/{city}` | Search by City |
| GET | `/students/filter?department=IT&city=Pune` | Filter Students |
| GET | `/students/pagination?page=0&size=5` | Pagination |
| GET | `/students/sort/asc/studentName` | Sort Ascending |
| GET | `/students/sort/desc/studentName` | Sort Descending |

### Authentication APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/students/signup` | Register Student |
| POST | `/students/login` | Student Login |
| POST | `/students/logout` | Student Logout |

---

## 🗄 Database

**Database:** MySQL

**Database Name:** `StudentAPI_db`

**Table:** `student`

Fields:

- studentId
- studentName
- email
- password
- department
- city
- age
- course

---

## ▶️ How to Run

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Configure MySQL in `application.properties`.
4. Create the database:

```sql
CREATE DATABASE StudentAPI_db;
```

5. Run the Spring Boot application.
6. Open your browser:

```
http://localhost:8081/signup.html
```

7. Register a new student.
8. Login using registered credentials.
9. Access the Dashboard.
10. Test remaining APIs using Postman.

