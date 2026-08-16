if (localStorage.getItem("loggedIn") !== "true") {
    window.location.href = "login.html";
}

function logout() {
    function finishLogout(message) {
        if (message) alert(message);
        localStorage.removeItem("loggedIn");
        localStorage.removeItem("role");
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