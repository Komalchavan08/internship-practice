// ============================================================
// dashboard.html behavior: role-based navigation + live API data
// ============================================================

// Get the logged-in session immediately (safe here - doesn't depend on
// anything defined further down). Redirects to /login.html if not logged in.
const session = requireAuth();

// Startup code (the actual function CALLS) lives at the BOTTOM of this
// file - it needs to run AFTER renderNav/NAV_ITEMS/etc are all defined above it.

function renderUserInfo() {
  document.getElementById('userNameLabel').textContent = session.fullName;

  const stampsHtml = session.roles.length
    ? session.roles.map(r => `<span class="role-stamp">${r}</span>`).join('')
    : `<span class="role-stamp">NO ROLE</span>`;
  document.getElementById('roleStamps').innerHTML = stampsHtml;

  document.getElementById('todayLabel').textContent =
    new Date().toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
}

document.getElementById('logoutBtn').addEventListener('click', logout);

// ------------------------------------------------------------
// NAV CONFIG
// Each entry becomes a clickable card - but only if the current user
// has one of the roles listed (or requiredRoles is null = anyone logged in).
// This is what "role-based navigation" actually means: the SAME dashboard
// code runs for every user, but what they SEE depends on their roles.
// ------------------------------------------------------------
const NAV_ITEMS = [
  { key: 'contracts', title: 'Contracts', desc: 'View all contracts in the system', endpoint: '/api/contracts', requiredRoles: null },
  { key: 'versions', title: 'Versions', desc: 'Browse version history', endpoint: '/api/versions', requiredRoles: null },
  { key: 'approvals', title: 'Approvals', desc: 'Review pending & past approvals', endpoint: '/api/approvals', requiredRoles: ['APPROVER', 'ADMIN'] },
  { key: 'auditlogs', title: 'Audit Log', desc: 'Full history of system actions', endpoint: '/api/audit-logs', requiredRoles: null },
  { key: 'roles', title: 'Manage Roles', desc: 'Admin: view configured roles', endpoint: '/api/roles', requiredRoles: ['ADMIN'] },
  { key: 'users', title: 'Manage Users', desc: 'Admin: view all system users', endpoint: '/api/users', requiredRoles: ['ADMIN'] },
];

function userCanSee(item) {
  if (!item.requiredRoles) return true; // open to any logged-in user
  return item.requiredRoles.some(r => session.roles.includes(r));
}

function renderNav() {
  const grid = document.getElementById('navGrid');
  grid.innerHTML = '';

  NAV_ITEMS.filter(userCanSee).forEach(item => {
    const card = document.createElement('button');
    card.className = 'nav-card';
    card.dataset.key = item.key;
    card.innerHTML = `
      <p class="nav-card-title">${item.title}</p>
      <p class="nav-card-desc">${item.desc}</p>
    `;
    card.addEventListener('click', () => loadSection(item, card));
    grid.appendChild(card);
  });
}

async function loadSection(item, cardEl) {
  document.querySelectorAll('.nav-card').forEach(c => c.classList.remove('active'));
  cardEl.classList.add('active');

  const title = document.getElementById('resultsTitle');
  const body = document.getElementById('resultsBody');
  title.textContent = item.title;
  body.innerHTML = '<p class="loading-note">Loading...</p>';

  try {
    const response = await apiFetch(item.endpoint);

    if (!response.ok) {
      // e.g. 403 - logged in, but this role isn't actually allowed
      // server-side either (belt and suspenders vs. the client-side check above)
      body.innerHTML = `<p class="empty-note">You don't have permission to view this (server responded ${response.status}).</p>`;
      return;
    }

    const data = await response.json();
    body.innerHTML = renderTable(item.key, data);

  } catch (err) {
    body.innerHTML = `<p class="empty-note">Something went wrong: ${err.message}</p>`;
  }
}

// Builds a small, readable table per section - just picks a few
// sensible columns per entity type rather than dumping raw JSON.
function renderTable(key, rows) {
  if (!Array.isArray(rows) || rows.length === 0) {
    return '<p class="empty-note">No records found.</p>';
  }

  const statusPill = (s) => s ? `<span class="status-pill status-${s}">${s}</span>` : '';

  let columns;
  switch (key) {
    case 'contracts':
      columns = [
        ['title', 'Title'],
        ['status', 'Status', statusPill],
        ['createdByName', 'Created By'],
        ['createdAt', 'Created At'],
      ];
      break;
    case 'versions':
      columns = [
        ['versionNumber', 'Version #'],
        ['status', 'Status', statusPill],
        ['createdByName', 'Created By'],
        ['changeSummary', 'Summary'],
      ];
      break;
    case 'approvals':
      columns = [
        ['requestedByName', 'Requested By'],
        ['approverName', 'Approver'],
        ['status', 'Status', statusPill],
        ['comments', 'Comments'],
      ];
      break;
    case 'auditlogs':
      columns = [
        ['entityType', 'Entity'],
        ['action', 'Action'],
        ['performedByName', 'Performed By'],
        ['timestamp', 'Timestamp'],
      ];
      break;
    case 'roles':
      columns = [['id', 'ID'], ['name', 'Role Name']];
      break;
    case 'users':
      columns = [
        ['fullName', 'Name'],
        ['email', 'Email'],
        ['roles', 'Roles', (v) => (v || []).join(', ') || '—'],
      ];
      break;
    default:
      columns = Object.keys(rows[0]).map(k => [k, k]);
  }

  const thead = `<tr>${columns.map(([, label]) => `<th>${label}</th>`).join('')}</tr>`;
  const tbody = rows.map(row => {
    const cells = columns.map(([field, , formatter]) => {
      const raw = row[field];
      const value = formatter ? formatter(raw) : (raw ?? '—');
      return `<td>${value}</td>`;
    }).join('');
    return `<tr>${cells}</tr>`;
  }).join('');

  return `<table><thead>${thead}</thead><tbody>${tbody}</tbody></table>`;
}

// ------------------------------------------------------------
// ACTUAL STARTUP - runs down here, now that everything above
// (renderUserInfo, renderNav, NAV_ITEMS, etc) is fully defined.
// ------------------------------------------------------------
if (session) {
  renderUserInfo();
  renderNav();
}