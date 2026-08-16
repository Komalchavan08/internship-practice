const API_BASE = "http://localhost:8081/students";

if (localStorage.getItem("loggedIn") !== "true") {
    window.location.href = "login.html";
}

const studentId = new URLSearchParams(window.location.search).get("id");

const fields = {
    studentName: attachValidator(
        document.getElementById("studentName"),
        document.getElementById("nameError"),
        Validators.name
    ),
    email: attachValidator(
        document.getElementById("email"),
        document.getElementById("emailError"),
        Validators.email
    ),
    password: attachValidator(
        document.getElementById("password"),
        document.getElementById("passwordError"),
        Validators.password
    ),
    department: attachValidator(
        document.getElementById("department"),
        document.getElementById("departmentError"),
        Validators.department
    ),
    city: attachValidator(
        document.getElementById("city"),
        document.getElementById("cityError"),
        Validators.city
    ),
    age: attachValidator(
        document.getElementById("age"),
        document.getElementById("ageError"),
        Validators.age
    ),
    course: attachValidator(
        document.getElementById("course"),
        document.getElementById("courseError"),
        Validators.course
    ),
    mobile: attachValidator(
        document.getElementById("mobile"),
        document.getElementById("mobileError"),
        Validators.mobile
    ),
    dob: attachValidator(
        document.getElementById("dob"),
        document.getElementById("dobError"),
        Validators.dob
    ),
    address: attachValidator(
        document.getElementById("address"),
        document.getElementById("addressError"),
        Validators.address
    )
};

loadStudent();

function loadStudent() {
    fetch(`${API_BASE}/${studentId}`)
        .then(response => response.json())
        .then(student => {
            if (!student.user) {
                alert("This record has no linked login (it's a legacy record from before roles were added) and can't be edited here. Please delete it instead.");
                window.location.href = "viewStudents.html";
                return;
            }

            document.getElementById("studentName").value = student.user.name;
            document.getElementById("email").value = student.user.email;
            document.getElementById("password").value = student.user.password;
            document.getElementById("department").value = student.department;
            document.getElementById("city").value = student.city;
            document.getElementById("age").value = student.age;
            document.getElementById("course").value = student.course;
            document.getElementById("mobile").value = student.user.mobile || "";
            document.getElementById("dob").value = student.user.dob || "";
            document.getElementById("address").value = student.user.address || "";

            document.querySelectorAll(".field input").forEach(input => input.classList.add("valid"));
        })
        .catch(error => {
            console.error(error);
            alert("Unable to load this student.");
        });
}

function updateStudent() {
    const allValid = Object.values(fields).map(run => run()).every(Boolean);
    if (!allValid) {
        return;
    }

    const student = {
        studentName: document.getElementById("studentName").value.trim(),
        email: document.getElementById("email").value.trim(),
        password: document.getElementById("password").value.trim(),
        department: document.getElementById("department").value.trim(),
        city: document.getElementById("city").value.trim(),
        age: document.getElementById("age").value,
        course: document.getElementById("course").value.trim(),
        mobile: document.getElementById("mobile").value.trim(),
        dob: document.getElementById("dob").value,
        address: document.getElementById("address").value.trim()
    };

    const messageEl = document.getElementById("message");

    fetch(`${API_BASE}/${studentId}`, {
        method: "PUT",
        headers: withAuditHeader({ "Content-Type": "application/json" }),
        body: JSON.stringify(student)
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) throw data;
            return data;
        })
        .then(() => {
            messageEl.className = "form-message success";
            messageEl.innerHTML = "Student updated successfully.";

            setTimeout(() => {
                window.location.href = "viewStudents.html";
            }, 1200);
        })
        .catch(error => {
            messageEl.className = "form-message error";
            messageEl.innerHTML = error.message || "Could not update student.";
        });
}

function logout() {
    function finishLogout(message) {
        if (message) alert(message);
        localStorage.removeItem("loggedIn");
        localStorage.removeItem("role");
        localStorage.removeItem("userId");
        localStorage.removeItem("userEmail");
        window.location.href = "login.html";
    }

    fetch("http://localhost:8081/users/logout", { method: "POST", headers: withAuditHeader() })
        .then(response => response.json())
        .then(data => finishLogout(data.message))
        .catch(error => {
            console.error(error);
            finishLogout(null);
        });
}