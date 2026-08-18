# 🎓 Student Management System

A Spring Boot REST API for managing student records with role-based authentication (Admin/Student), a profile module, automatic audit trails, and an activity log with CSV export — built with MySQL, Spring Data JPA, and a vanilla HTML/CSS/JS frontend.

## 📖 About

This project was developed as part of my Java Full Stack internship. It started as a straightforward Student CRUD API and has grown into a small role-based system: an Admin account manages student records, each student gets their own login, every user has a profile they can maintain themselves, and every meaningful action in the system is automatically logged for auditing.

## 🚀 Features

### Role-Based Authentication
- ✅ One-time Admin registration (self-registration disabled after the first admin exists)
- ✅ Admin and Student login, routed to separate dashboards based on role
- ✅ Dynamic role creation (no hardcoded role IDs)
- ✅ Logout

### Student Management (Admin only)
- ✅ Add Student — creates both the student's login and their academic profile together
- ✅ Get All Students
- ✅ Get Student By ID
- ✅ Update Student
- ✅ Delete Student — removes both linked records

### Search, Filter, Sort & Pagination
- ✅ Search by Name, Email, Department, City
- ✅ Filter by Department and City
- ✅ Sorting (Ascending / Descending)
- ✅ Pagination

### Profile Module (Admin & Student)
- ✅ View Profile — includes academic info (department/course/city/age) for students only
- ✅ Update Profile (name, mobile, date of birth, address)
- ✅ Upload Profile Photo
- ✅ Change Password

### Audit Fields
- ✅ `createdBy`, `createdDate`, `updatedBy`, `updatedDate` on every table
- ✅ Populated automatically via Spring Data JPA Auditing — no manual bookkeeping required

### Activity Log & CSV Export (Admin only)
- ✅ Automatic logging of Login, Logout, Registration, Create, Update, Delete, and Password Change events
- ✅ Filterable, paginated log viewer (by action, entity type, performed-by, date range)
- ✅ CSV export of the currently filtered results

### Frontend
- ✅ Login, Registration, Admin Dashboard, Student Dashboard, View/Add/Edit Student, Profile, Activity Logs
- ✅ Client-side form validation
- ✅ Route protection via Local Storage (session state)

---

## 🛠 Technologies Used

- Java 22
- Spring Boot
- Spring Data JPA (Hibernate) + JPA Auditing
- MySQL
- HTML5 / CSS3 / JavaScript
- Maven
- Postman

---

## 📂 Project Structure

```
src
├── config          # JPA auditing config, audit-header filter, static file serving
├── controller
├── dto             # Request/response shapes for Student, Profile
├── entity          # Role, User, Student, AuditLog, Auditable (shared base)
├── exception
├── repository
├── service
└── resources
    ├── static
    │   ├── login.html / login.js
    │   ├── signup.html / signup.js         (Admin registration only)
    │   ├── dashboard.html / dashboard.js    (Admin)
    │   ├── studentDashboard.html / .js      (Student)
    │   ├── viewStudents.html / .js
    │   ├── addStudent.html / .js
    │   ├── editStudent.html / .js
    │   ├── profile.html / .js               (shared by both roles)
    │   ├── activityLogs.html / .js          (Admin only)
    │   ├── theme.css / dashboard.css / etc.
    │   └── auditHeader.js / validation.js   (shared helpers)
    └── application.properties
```

---

## 📌 API Endpoints

### Authentication (`/users`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users/signup` | Register Admin (once only) |
| GET | `/users/admin-exists` | Check whether an admin already exists |
| POST | `/users/login` | Login (Admin or Student) — returns role + userId |
| POST | `/users/logout` | Logout |

### Profile (`/users/profile`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/users/profile/{id}` | View profile (includes academic info for students) |
| PUT | `/users/profile/{id}` | Update profile |
| POST | `/users/profile/{id}/photo` | Upload profile photo |
| PUT | `/users/profile/{id}/change-password` | Change password |

### Student Management (`/students`) — Admin only

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

### Activity Logs (`/audit-logs`) — Admin only (requires `X-User-Role: ADMIN` header)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/audit-logs?action=&entityType=&performedBy=&from=&to=&page=&size=` | Filtered, paginated logs |
| GET | `/audit-logs/export?...` | CSV export of the filtered logs |

---

## 🗄 Database

**Database:** MySQL
**Database Name:** `StudentAPI_db`

### `role`
`id`, `name` — created dynamically (e.g. `ADMIN`, `STUDENT`) the first time each is needed.

### `app_user` — login identity for both Admin and Student
`user_id`, `name`, `email`, `password`, `mobile`, `dob`, `address`, `status`, `profile_photo`, `role_id`, plus audit columns.

### `student` — academic profile, linked to `app_user`
`student_id`, `department`, `city`, `age`, `course`, `status`, `user_id`, plus audit columns.

### `audit_log` — activity trail
`id`, `action`, `entity_type`, `entity_id`, `performed_by`, `description`, `timestamp`.

All of `role`, `app_user`, and `student` also carry: `created_by`, `created_date`, `updated_by`, `updated_date`.

---

## ▶️ How to Run

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Configure MySQL credentials in `application.properties`.
4. Create the database:

```sql
CREATE DATABASE StudentAPI_db;
```

5. Run the Spring Boot application (`spring.jpa.hibernate.ddl-auto=update` creates all tables automatically).
6. Open your browser:

```
http://localhost:8081/
```

7. Register the Admin account (first-time only — this option disappears afterward).
8. Log in as Admin → add students from the dashboard.
9. Log out and log back in with a student's credentials to see the Student side.
10. Check **Activity Logs** (Admin only) to see every action tracked automatically.
11. Test remaining APIs using Postman.