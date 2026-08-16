const API_BASE = "http://localhost:8081/audit-logs";

if (localStorage.getItem("loggedIn") !== "true") {
    window.location.href = "login.html";
}

if (localStorage.getItem("role") !== "ADMIN") {
    alert("Only an admin can view activity logs.");
    window.location.href = "studentDashboard.html";
}

const PAGE_SIZE = 10;
let currentPage = 0;

fetchLogs();

function buildFilterParams() {
    const params = new URLSearchParams();

    const action = document.getElementById("actionFilter").value;
    const entityType = document.getElementById("entityTypeFilter").value;
    const performedBy = document.getElementById("performedByFilter").value.trim();
    const from = document.getElementById("fromDate").value;
    const to = document.getElementById("toDate").value;

    if (action) params.set("action", action);
    if (entityType) params.set("entityType", entityType);
    if (performedBy) params.set("performedBy", performedBy);
    if (from) params.set("from", `${from}T00:00:00`);
    if (to) params.set("to", `${to}T23:59:59`);

    return params;
}

function applyFilters() {
    currentPage = 0;
    fetchLogs();
}

function resetFilters() {
    document.getElementById("actionFilter").value = "";
    document.getElementById("entityTypeFilter").value = "";
    document.getElementById("performedByFilter").value = "";
    document.getElementById("fromDate").value = "";
    document.getElementById("toDate").value = "";
    currentPage = 0;
    fetchLogs();
}

function goToPage(page) {
    if (page < 0) return;
    currentPage = page;
    fetchLogs();
}

function fetchLogs() {
    const params = buildFilterParams();
    params.set("page", currentPage);
    params.set("size", PAGE_SIZE);

    fetch(`${API_BASE}?${params.toString()}`, {
        headers: withAuditHeader()
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) throw data;
            return data;
        })
        .then(data => {
            renderLogs(data.content);
            renderPagination(data);
        })
        .catch(error => {
            console.error(error);
            document.getElementById("logsTable").innerHTML =
                `<tr><td colspan="7" class="empty-state">${error.message || "Unable to load activity logs."}</td></tr>`;
            document.getElementById("pagination").innerHTML = "";
        });
}

function renderLogs(logs) {
    const table = document.getElementById("logsTable");
    table.innerHTML = "";

    if (!logs || logs.length === 0) {
        table.innerHTML = `<tr><td colspan="7" class="empty-state">No activity found for these filters.</td></tr>`;
        return;
    }

    logs.forEach(log => {
        const actionClass = "action-" + (log.action || "").toLowerCase();

        table.innerHTML += `
        <tr>
            <td>${log.id}</td>
            <td><span class="action-badge ${actionClass}">${(log.action || "").replace("_", " ")}</span></td>
            <td>${log.performedBy || "—"}</td>
            <td>${log.entityType || "—"}</td>
            <td>${log.entityId || "—"}</td>
            <td class="description-cell" title="${log.description || ""}">${log.description || "—"}</td>
            <td>${formatTimestamp(log.timestamp)}</td>
        </tr>
        `;
    });
}

function formatTimestamp(ts) {
    if (!ts) return "—";
    const date = new Date(ts);
    if (isNaN(date.getTime())) return ts;
    return date.toLocaleString();
}

function renderPagination(pageData) {
    const el = document.getElementById("pagination");
    const totalPages = Math.max(pageData.totalPages, 1);

    el.innerHTML = `
        <button onclick="goToPage(${currentPage - 1})" ${pageData.first ? "disabled" : ""}>◀ Previous</button>
        <span>Page ${pageData.number + 1} of ${totalPages}</span>
        <button onclick="goToPage(${currentPage + 1})" ${pageData.last ? "disabled" : ""}>Next ▶</button>
    `;
}

function exportCsv() {
    const params = buildFilterParams();

    fetch(`${API_BASE}/export?${params.toString()}`, {
        headers: withAuditHeader()
    })
        .then(async response => {
            if (!response.ok) {
                const data = await response.json();
                throw data;
            }
            return response.text();
        })
        .then(csvText => {
            const blob = new Blob([csvText], { type: "text/csv" });
            const url = URL.createObjectURL(blob);

            const link = document.createElement("a");
            link.href = url;
            link.download = "activity-logs.csv";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            URL.revokeObjectURL(url);
        })
        .catch(error => {
            console.error(error);
            alert(error.message || "Could not export logs.");
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