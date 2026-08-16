// Hide the Register link once an admin already exists — only one admin
// can ever self-register.
fetch("http://localhost:8081/users/admin-exists")
    .then(response => response.json())
    .then(data => {
        if (data.adminExists) {
            const registerLink = document.getElementById("registerLinkWrap");
            if (registerLink) registerLink.style.display = "none";
        }
    })
    .catch(error => console.error(error));

function login() {

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    if (email === "" || password === "") {

        document.getElementById("message").className = "form-message error";
        document.getElementById("message").innerHTML =
            "Please enter email and password.";

        return;
    }

    const user = {
        email: email,
        password: password
    };

    fetch("http://localhost:8081/users/login", {

        method: "POST",

        headers: withAuditHeader({ "Content-Type": "application/json" }, email),

        body: JSON.stringify(user)

    })

    .then(async response => {

        const data = await response.json();

        if (!response.ok) {
            throw data;
        }

        return data;

    })

    .then(data => {

        localStorage.setItem("loggedIn", "true");
        localStorage.setItem("role", data.role);
        localStorage.setItem("userId", data.userId);
        localStorage.setItem("userEmail", email);

        document.getElementById("message").className = "form-message success";
        document.getElementById("message").innerHTML = data.message;

        const destination = data.role === "ADMIN" ? "dashboard.html" : "studentDashboard.html";

        setTimeout(() => {

            window.location.href = destination;

        }, 1500);

    })

    .catch(error => {

        document.getElementById("message").className = "form-message error";

        document.getElementById("message").innerHTML =
            error.message || "Login Failed";

    });

}