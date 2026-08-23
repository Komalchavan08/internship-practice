const API_BASE = "http://localhost:8081/users";

let verifiedEmail = "";

function requestOtp() {
    const email = document.getElementById("email").value.trim();
    const emailError = document.getElementById("emailError");
    const emailMessage = document.getElementById("emailMessage");

    emailError.innerHTML = "";
    emailMessage.innerHTML = "";

    const result = Validators.email(email);
    if (!result.valid) {
        emailError.innerHTML = result.message;
        return;
    }

    fetch(`${API_BASE}/forgot-password`, {
        method: "POST",
        headers: withAuditHeader({ "Content-Type": "application/json" }, email),
        body: JSON.stringify({ email })
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) throw data;
            return data;
        })
        .then(data => {
            verifiedEmail = email;

            emailMessage.className = "form-message success";
            emailMessage.innerHTML = data.message;

            document.getElementById("sentToEmail").textContent = email;
            document.getElementById("stepEmail").style.display = "none";
            document.getElementById("stepReset").style.display = "block";

            document.getElementById("resetMessage").innerHTML = "";
        })
        .catch(error => {
            emailMessage.className = "form-message error";
            emailMessage.innerHTML = error.message || "Could not send OTP.";
        });
}

function resetPassword() {
    const otp = document.getElementById("otp").value.trim();
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const otpError = document.getElementById("otpError");
    const newPasswordError = document.getElementById("newPasswordError");
    const confirmPasswordError = document.getElementById("confirmPasswordError");
    const resetMessage = document.getElementById("resetMessage");

    otpError.innerHTML = "";
    newPasswordError.innerHTML = "";
    confirmPasswordError.innerHTML = "";
    resetMessage.innerHTML = "";

    let valid = true;

    if (!/^[0-9]{6}$/.test(otp)) {
        otpError.innerHTML = "Enter the 6-digit code from your email.";
        valid = false;
    }

    const passwordCheck = Validators.password(newPassword);
    if (!passwordCheck.valid) {
        newPasswordError.innerHTML = passwordCheck.message;
        valid = false;
    }

    if (confirmPassword !== newPassword) {
        confirmPasswordError.innerHTML = "Passwords do not match.";
        valid = false;
    }

    if (!valid) return;

    fetch(`${API_BASE}/reset-password`, {
        method: "POST",
        headers: withAuditHeader({ "Content-Type": "application/json" }, verifiedEmail),
        body: JSON.stringify({ email: verifiedEmail, otp, newPassword })
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) throw data;
            return data;
        })
        .then(data => {
            resetMessage.className = "form-message success";
            resetMessage.innerHTML = data.message;

            setTimeout(() => {
                window.location.href = "login.html";
            }, 1500);
        })
        .catch(error => {
            resetMessage.className = "form-message error";
            resetMessage.innerHTML = error.message || "Could not reset password.";
        });
}