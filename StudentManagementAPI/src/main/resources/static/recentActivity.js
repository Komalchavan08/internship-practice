/**
 * Recently Added notification bell.
 * Drop the markup (see dashboard.html or viewStudents.html) on any page
 * and include this script — it wires itself up automatically.
 */
(function () {

    const API_BASE = "http://localhost:8081/students";

    const bell = document.getElementById("recentBell");
    const dropdown = document.getElementById("recentDropdown");
    const list = document.getElementById("recentList");
    const badge = document.getElementById("recentBadge");

    if (!bell || !dropdown || !list) {
        return;
    }

    function initials(name) {
        return (name || "?").trim().charAt(0).toUpperCase();
    }

    function renderRecent(students) {
        // No createdAt field on the backend yet, so "recent" means
        // "most recently inserted" (highest studentId), not a real timestamp.
        // Legacy records with no linked user (created before roles existed)
        // are excluded — they predate this feature.
        const recent = students
            .filter(student => !!student.user)
            .sort((a, b) => b.studentId - a.studentId)
            .slice(0, 3);

        if (recent.length === 0) {
            list.innerHTML = `<div class="notif-empty">No students added yet.</div>`;
            badge.classList.remove("show");
            return;
        }

        badge.textContent = recent.length;
        badge.classList.add("show");

        list.innerHTML = recent.map(student => `
            <div class="notif-item">
                <div class="notif-avatar">${initials(student.user.name)}</div>
                <div class="notif-info">
                    <strong>${student.user.name}</strong>
                    <span>${student.department} · ${student.course}</span>
                </div>
                <span class="notif-tag">New</span>
            </div>
        `).join("");
    }

    fetch(API_BASE)
        .then(response => response.json())
        .then(renderRecent)
        .catch(error => {
            console.error(error);
            list.innerHTML = `<div class="notif-empty">Unable to load recent students.</div>`;
        });

    bell.addEventListener("click", event => {
        event.stopPropagation();
        dropdown.classList.toggle("open");
    });

    document.addEventListener("click", event => {
        if (!dropdown.contains(event.target) && event.target !== bell) {
            dropdown.classList.remove("open");
        }
    });

})();