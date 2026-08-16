let currentPage = 0;
let pageSize = 10;

if (localStorage.getItem("loggedIn") !== "true") {

    window.location.href = "login.html";

}

let students = [];

loadStudents();

function loadStudents() {

    fetch(`http://localhost:8081/students/pagination?page=${currentPage}&size=${pageSize}`)

        .then(response => response.json())

        .then(data => {

            students = data.content;

            displayStudents(students);

            createPagination(data);

        })

        .catch(error => {

            console.error(error);

            alert("Unable to load students.");

        });

}

function displayStudents(studentList) {

    const table = document.getElementById("studentTable");

    table.innerHTML = "";

    if (studentList.length === 0) {
        table.innerHTML = `<tr><td colspan="8" class="empty-state">No students found.</td></tr>`;
        return;
    }

    studentList.forEach(student => {

        const isActive = student.status === "ACTIVE";
        const hasUser = !!student.user;
        const displayName = hasUser ? student.user.name : "(no login — legacy record)";
        const displayEmail = hasUser ? student.user.email : "—";

        table.innerHTML += `

        <tr class="${isActive ? 'row-active' : 'row-inactive'}">

            <td>${student.studentId}</td>

            <td>${displayName}</td>

            <td>${displayEmail}</td>

            <td>${student.department}</td>

            <td>${student.city}</td>

            <td>${student.course}</td>

            <td>
                <span class="badge ${isActive ? 'badge-active' : 'badge-inactive'}">
                    ${student.status}
                </span>
            </td>

            <td>
                <div class="row-actions">
                    <button class="icon-btn" title="${hasUser ? 'Edit' : 'No login to edit'}" onclick="editStudent(${student.studentId})" ${hasUser ? '' : 'disabled'}>
                        <svg class="icon" viewBox="0 0 24 24"><path d="M12 20h9"/><path d="M16.5 3.5a2.1 2.1 0 0 1 3 3L7 19l-4 1 1-4 12.5-12.5Z"/></svg>
                    </button>
                    <button class="icon-btn icon-btn-danger" title="Delete" onclick="deleteStudent(${student.studentId})">
                        <svg class="icon" viewBox="0 0 24 24"><path d="M4 7h16"/><path d="M10 11v6"/><path d="M14 11v6"/><path d="M6 7l1 13a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2l1-13"/><path d="M9 7V4h6v3"/></svg>
                    </button>
                </div>
            </td>

        </tr>

        `;

    });

}

function createPagination(pageData){

    const pagination = document.getElementById("pagination");

    pagination.innerHTML = "";

    const previousButton = document.createElement("button");

    previousButton.innerHTML = "◀ Previous";

    previousButton.disabled = pageData.first;

    previousButton.onclick = function(){

        currentPage--;

        loadStudents();

    };

    pagination.appendChild(previousButton);

    const pageInfo = document.createElement("span");

    pageInfo.innerHTML =
        ` Page ${pageData.number + 1} of ${pageData.totalPages} `;

    pagination.appendChild(pageInfo);

    const nextButton = document.createElement("button");

    nextButton.innerHTML = "Next ▶";

    nextButton.disabled = pageData.last;

    nextButton.onclick = function(){

        currentPage++;

        loadStudents();

    };

    pagination.appendChild(nextButton);

}

document.getElementById("searchInput").addEventListener("keyup", function () {

    const keyword = this.value.toLowerCase();

    const filteredStudents = students.filter(student =>

        (student.user ? student.user.name : "").toLowerCase().includes(keyword)

    );

    displayStudents(filteredStudents);

});

function editStudent(id) {

    window.location.href = `editStudent.html?id=${id}`;

}


function deleteStudent(id) {

    if (!confirm("Are you sure you want to delete this student?")) {

        return;

    }

    fetch(`http://localhost:8081/students/${id}`, {

        method: "DELETE"

    })

    .then(response => response.json())

    .then(data => {

        alert(data.message);

        loadStudents();

    })

    .catch(error => {

        console.error(error);

        alert("Unable to delete student.");

    });

}

document.getElementById("pageSize").addEventListener("change", function () {

    pageSize = parseInt(this.value);

    currentPage = 0;

    loadStudents();

});

document.getElementById("statusFilter").addEventListener("change", function () {

    const status = this.value;

    if (status === "ALL") {

        currentPage = 0;

        loadStudents();

        return;

    }

    fetch(`http://localhost:8081/students/status/${status}`)

        .then(response => response.json())

        .then(data => {

            students = data;

            displayStudents(students);

            document.getElementById("pagination").innerHTML = "";

        })

        .catch(error => {

            console.error(error);

            alert("Unable to filter students.");

        });

});

document.getElementById("sortBy").addEventListener("change", function () {

    const value = this.value;

    if (value === "") {

        loadStudents();
        return;

    }

    let url = "";

    if (value === "nameAsc") {

        url = "http://localhost:8081/students/sort/asc/studentName";

    }

    else if (value === "nameDesc") {

        url = "http://localhost:8081/students/sort/desc/studentName";

    }

    fetch(url)

        .then(response => response.json())

        .then(data => {

            displayStudents(data);

        })

        .catch(error => {

            console.error(error);

            alert("Unable to sort students.");

        });

});

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