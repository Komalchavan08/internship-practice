if (localStorage.getItem("loggedIn") !== "true") {

    window.location.href = "login.html";

}

loadDashboard();

function loadDashboard() {

    fetch("http://localhost:8081/students")

        .then(response => response.json())

        .then(students => {

            // Total Students
            document.getElementById("totalStudents").innerHTML = students.length;

            // Active Students
            const activeCount = students.filter(student => student.status === "ACTIVE").length;

            document.getElementById("activeStudents").innerHTML = activeCount;

            // Inactive Students
            const inactiveCount = students.filter(student => student.status === "INACTIVE").length;

            document.getElementById("inactiveStudents").innerHTML = inactiveCount;

        })

        .catch(error => {

            console.error(error);

        });

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

    .then(data => {

        finishLogout(data.message);

    })

    .catch(error => {
        console.error(error);
        finishLogout(null);
    });

}