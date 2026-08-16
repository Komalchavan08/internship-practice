const API_BASE = "http://localhost:8081/students";

if (localStorage.getItem("loggedIn") !== "true") {
    window.location.href = "login.html";
}

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

const registerBtn = document.getElementById("registerBtn");
const formInputs = document.querySelectorAll(".field input");

formInputs.forEach(input => input.addEventListener("input", updateButtonState));

function updateButtonState() {
    const allValid = [...formInputs].every(input => input.classList.contains("valid"));
    registerBtn.disabled = !allValid;
}

function addStudent() {
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
        age: document.getElementById("age").value.trim(),
        course: document.getElementById("course").value.trim(),
        mobile: document.getElementById("mobile").value.trim(),
        dob: document.getElementById("dob").value,
        address: document.getElementById("address").value.trim()
    };

    const messageEl = document.getElementById("message");

    fetch(API_BASE, {
        method: "POST",
        headers: withAuditHeader({ "Content-Type": "application/json" }),
        body: JSON.stringify(student)
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) throw data;
            return data;
        })
        .then(data => {
            messageEl.className = "form-message success";
            messageEl.innerHTML = data.message;

            setTimeout(() => {
                window.location.href = "viewStudents.html";
            }, 1500);
        })
        .catch(error => {
            messageEl.className = "form-message error";
            messageEl.innerHTML = error.message || "Could not add student.";
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