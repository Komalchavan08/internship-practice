// ============================================================
// Shared auth helpers - used by both login.html and dashboard.html
// ============================================================

const API_BASE = ''; // same origin, since Spring Boot serves these HTML files too

// --- Session storage (sessionStorage clears when the tab closes -
//     a reasonable default for a security-sensitive demo app) ---

function saveSession(loginResponse) {
  sessionStorage.setItem('token', loginResponse.token);
  sessionStorage.setItem('userId', loginResponse.userId);
  sessionStorage.setItem('fullName', loginResponse.fullName);
  sessionStorage.setItem('email', loginResponse.email);
  sessionStorage.setItem('roles', JSON.stringify(loginResponse.roles || []));
}

function getToken() {
  return sessionStorage.getItem('token');
}

function getSession() {
  const token = getToken();
  if (!token) return null;
  return {
    token,
    userId: sessionStorage.getItem('userId'),
    fullName: sessionStorage.getItem('fullName'),
    email: sessionStorage.getItem('email'),
    roles: JSON.parse(sessionStorage.getItem('roles') || '[]'),
  };
}

function clearSession() {
  sessionStorage.clear();
}

function logout() {
  clearSession();
  window.location.href = '/login.html';
}

// Redirect to login if there's no token - call this at the top of
// any page that requires being logged in.
function requireAuth() {
  if (!getToken()) {
    window.location.href = '/login.html';
    return null;
  }
  return getSession();
}

// Wrapper around fetch() that automatically attaches the JWT token
// as "Authorization: Bearer <token>" on every call - this is the
// client-side equivalent of clicking "Authorize" in Swagger.
async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  };
  if (token) {
    headers['Authorization'] = 'Bearer ' + token;
  }

  const response = await fetch(API_BASE + path, { ...options, headers });

  // If the token is invalid/expired, the server returns 401/403 -
  // treat that as "session expired" and send the user back to login.
  if (response.status === 401) {
    clearSession();
    window.location.href = '/login.html';
    throw new Error('Session expired - please log in again');
  }

  return response;
}

function hasRole(roleName) {
  const session = getSession();
  return !!session && session.roles.includes(roleName);
}