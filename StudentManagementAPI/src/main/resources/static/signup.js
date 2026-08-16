const API_BASE = "http://localhost:8081/users";

// If someone reaches this page directly (not via the hidden Register link),
// block registration once an admin already exists.
fetch(`${API_BASE}/admin-exists`)
    .then(response => response.json())
    .then(data => {
        if (data.adminExists) {
            document.querySelector(".form-box").innerHTML = `
                <h2>Registration closed</h2>
                <p>An admin has already been registered for this system.</p>
                <a href="login.html" class="btn btn-primary" style="display:flex;text-decoration:none;">Go to Login</a>
            `;
        }
    })
    .catch(error => console.error(error));

const fields = {
    fullName: attachValidator(
        document.getElementById("fullName"),
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

function signup() {
    const allValid = Object.values(fields).map(run => run()).every(Boolean);
    if (!allValid) {
        return;
    }

    const user = {
        name: document.getElementById("fullName").value.trim(),
        email: document.getElementById("email").value.trim(),
        password: document.getElementById("password").value.trim(),
        mobile: document.getElementById("mobile").value.trim(),
        dob: document.getElementById("dob").value,
        address: document.getElementById("address").value.trim(),
        roleName: document.getElementById("role").value
    };

    const messageEl = document.getElementById("message");

    fetch(`${API_BASE}/signup`, {
        method: "POST",
        headers: withAuditHeader({ "Content-Type": "application/json" }, user.email),
        body: JSON.stringify(user)
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
                window.location.href = "login.html";
            }, 1500);
        })
        .catch(error => {
            messageEl.className = "form-message error";
            messageEl.innerHTML = error.message || "Registration failed.";
        });
}