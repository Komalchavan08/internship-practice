// ============================================================
// login.html behavior: login form, register form, toggling between them
// ============================================================

// If someone's already logged in and lands here, send them straight to the dashboard
if (getToken()) {
  window.location.href = '/dashboard.html';
}

const loginView = document.getElementById('loginView');
const registerView = document.getElementById('registerView');

document.getElementById('showRegister').addEventListener('click', () => {
  loginView.classList.add('hidden');
  registerView.classList.remove('hidden');
});

document.getElementById('showLogin').addEventListener('click', () => {
  registerView.classList.add('hidden');
  loginView.classList.remove('hidden');
});

// --- LOGIN ---
document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();

  const errorBox = document.getElementById('loginError');
  errorBox.style.display = 'none';

  const email = document.getElementById('loginEmail').value;
  const password = document.getElementById('loginPassword').value;
  const btn = document.getElementById('loginBtn');

  btn.disabled = true;
  btn.textContent = 'Signing in...';

  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      // Our AuthController/GlobalExceptionHandler sends back a clean
      // { message: "Invalid email or password" } style body on 401
      const errBody = await response.json().catch(() => ({}));
      throw new Error(errBody.message || 'Invalid email or password');
    }

    const data = await response.json();
    saveSession(data); // stores token + user info in sessionStorage
    window.location.href = '/dashboard.html';

  } catch (err) {
    errorBox.textContent = err.message;
    errorBox.style.display = 'block';
  } finally {
    btn.disabled = false;
    btn.textContent = 'Sign In';
  }
});

// --- REGISTER ---
// Calls the same POST /api/users endpoint we built back in the CRUD task -
// it's deliberately left public in SecurityConfig so new users can sign up.
document.getElementById('registerForm').addEventListener('submit', async (e) => {
  e.preventDefault();

  const errorBox = document.getElementById('registerError');
  const successBox = document.getElementById('registerSuccess');
  errorBox.style.display = 'none';
  successBox.style.display = 'none';

  const fullName = document.getElementById('regName').value;
  const email = document.getElementById('regEmail').value;
  const password = document.getElementById('regPassword').value;
  const btn = document.getElementById('registerBtn');

  btn.disabled = true;
  btn.textContent = 'Creating account...';

  try {
    const response = await fetch('/api/users', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ fullName, email, password }),
    });

    if (!response.ok) {
      const errBody = await response.json().catch(() => ({}));
      const message = errBody.message || (errBody.email ? errBody.email : 'Could not create account');
      throw new Error(message);
    }

    successBox.textContent = 'Account created! You can now sign in.';
    successBox.style.display = 'block';
    document.getElementById('registerForm').reset();

    // Small pause so they can read the success message, then flip to login
    setTimeout(() => {
      registerView.classList.add('hidden');
      loginView.classList.remove('hidden');
      document.getElementById('loginEmail').value = email;
    }, 1200);

  } catch (err) {
    errorBox.textContent = err.message;
    errorBox.style.display = 'block';
  } finally {
    btn.disabled = false;
    btn.textContent = 'Create Account';
  }
});