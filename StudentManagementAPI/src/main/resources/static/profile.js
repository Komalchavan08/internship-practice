const API_BASE = "http://localhost:8081/users";

if (localStorage.getItem("loggedIn") !== "true") {
    window.location.href = "login.html";
}

const userId = localStorage.getItem("userId");
const role = localStorage.getItem("role");

if (!userId || userId === "null" || userId === "undefined") {
    alert("Your session is out of date. Please log in again.");
    localStorage.removeItem("loggedIn");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
    window.location.href = "login.html";
}

// Show the right sidebar for whichever role is logged in
if (role === "STUDENT") {
    document.getElementById("navAdminDashboard").style.display = "none";
    document.getElementById("navStudents").style.display = "none";
    document.getElementById("navAddStudent").style.display = "none";
    const activityLogsNav = document.getElementById("navActivityLogs");
    if (activityLogsNav) activityLogsNav.style.display = "none";
    document.getElementById("navStudentDashboard").style.display = "";
    document.getElementById("sidebarSubtitle").textContent = "Student Portal";
}

const fields = {
    name: attachValidator(
        document.getElementById("name"),
        document.getElementById("nameError"),
        Validators.name
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

loadProfile();

function loadProfile() {
    fetch(`${API_BASE}/profile/${userId}`)
        .then(response => response.json())
        .then(profile => {
            document.getElementById("name").value = profile.name || "";
            document.getElementById("email").value = profile.email || "";
            document.getElementById("roleDisplay").value = profile.role || "";
            document.getElementById("mobile").value = profile.mobile || "";
            document.getElementById("dob").value = profile.dob || "";
            document.getElementById("address").value = profile.address || "";

            document.querySelectorAll(".field-grid input:not([disabled])")
                .forEach(input => input.classList.add("valid"));

            updateAvatar(profile.name, profile.profilePhoto);

            if (profile.role === "STUDENT") {
                document.getElementById("academicSection").style.display = "";
                document.getElementById("academicDepartment").textContent = profile.department || "—";
                document.getElementById("academicCourse").textContent = profile.course || "—";
                document.getElementById("academicCity").textContent = profile.city || "—";
                document.getElementById("academicAge").textContent = profile.age || "—";
            }
        })
        .catch(error => {
            console.error(error);
            alert("Unable to load your profile.");
        });
}

function updateAvatar(name, photoPath) {
    const avatar = document.getElementById("avatarCircle");
    if (photoPath) {
        avatar.innerHTML = `<img src="http://localhost:8081${photoPath}" alt="Profile photo">`;
    } else {
        avatar.textContent = (name || "?").trim().charAt(0).toUpperCase();
    }
}

function saveProfile() {
    const allValid = Object.values(fields).map(run => run()).every(Boolean);
    if (!allValid) {
        return;
    }

    const payload = {
        name: document.getElementById("name").value.trim(),
        mobile: document.getElementById("mobile").value.trim(),
        dob: document.getElementById("dob").value,
        address: document.getElementById("address").value.trim()
    };

    const messageEl = document.getElementById("message");

    fetch(`${API_BASE}/profile/${userId}`, {
        method: "PUT",
        headers: withAuditHeader({ "Content-Type": "application/json" }),
        body: JSON.stringify(payload)
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) throw data;
            return data;
        })
        .then(profile => {
            messageEl.className = "form-message success";
            messageEl.innerHTML = "Profile updated successfully.";
            updateAvatar(profile.name, profile.profilePhoto);
        })
        .catch(error => {
            messageEl.className = "form-message error";
            messageEl.innerHTML = error.message || "Could not update profile.";
        });
}

document.getElementById("photoInput").addEventListener("change", function () {
    const file = this.files[0];
    if (!file) return;

    if (file.size > 5 * 1024 * 1024) {
        alert("File is too large. Please choose an image under 5MB.");
        this.value = "";
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    fetch(`${API_BASE}/profile/${userId}/photo`, {
        method: "POST",
        headers: withAuditHeader(),
        body: formData
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) throw data;
            return data;
        })
        .then(profile => {
            updateAvatar(profile.name, profile.profilePhoto);
        })
        .catch(error => {
            console.error(error);
            alert(error.message || "Could not upload photo.");
        });
});

function toggleChangePassword() {
    const section = document.getElementById("changePasswordSection");
    section.style.display = section.style.display === "none" ? "" : "none";
}

function changePassword() {
    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const newPasswordError = document.getElementById("newPasswordError");
    const confirmPasswordError = document.getElementById("confirmPasswordError");
    newPasswordError.innerHTML = "";
    confirmPasswordError.innerHTML = "";

    let valid = true;

    if (newPassword.length < 5) {
        newPasswordError.innerHTML = "New password must be at least 5 characters.";
        valid = false;
    }
    if (confirmPassword !== newPassword) {
        confirmPasswordError.innerHTML = "Passwords do not match.";
        valid = false;
    }
    if (!valid) return;

    const messageEl = document.getElementById("passwordMessage");

    fetch(`${API_BASE}/profile/${userId}/change-password`, {
        method: "PUT",
        headers: withAuditHeader({ "Content-Type": "application/json" }),
        body: JSON.stringify({ currentPassword, newPassword })
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) throw data;
            return data;
        })
        .then(data => {
            messageEl.className = "form-message success";
            messageEl.innerHTML = data.message;
            document.getElementById("currentPassword").value = "";
            document.getElementById("newPassword").value = "";
            document.getElementById("confirmPassword").value = "";
        })
        .catch(error => {
            messageEl.className = "form-message error";
            messageEl.innerHTML = error.message || "Could not change password.";
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