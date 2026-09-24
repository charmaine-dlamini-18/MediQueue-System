const ROLE_HOME = {
  ADMIN: "admin-dashboard.html",
  RECEPTIONIST: "receptionist-dashboard.html",
  DOCTOR: "doctor-dashboard.html",
  NURSE: "nurse-dashboard.html",
  PHARMACIST: "pharmacist-dashboard.html"
};

const loginForm = document.getElementById("loginForm");
const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const roleSelect = document.getElementById("roleSelect");
const loginError = document.getElementById("loginError");
const loginButton = document.getElementById("loginButton");
const togglePwBtn = document.getElementById("togglePwBtn");

togglePwBtn.addEventListener("click", () => {
  if (passwordInput.type === "password") {
    passwordInput.type = "text";
  } else {
    passwordInput.type = "password";
  }
});

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  const username = usernameInput.value.trim();
  const password = passwordInput.value.trim();
  const role = roleSelect.value;

  loginError.style.display = "none";
  loginError.textContent = "";

  if (!username) {
    showLoginError("Please enter your email/username.");
    usernameInput.focus();
    return;
  }

  if (!password) {
    showLoginError("Please enter your password.");
    passwordInput.focus();
    return;
  }

  if (!role) {
    showLoginError("Please select your user role.");
    roleSelect.focus();
    return;
  }

  if (!ROLE_HOME[role]) {
    showLoginError("Invalid user role selected.");
    return;
  }

  /*
    Authenticate against the backend. The token is only saved when the
    server validated the credentials — no fake logins allowed. The
    selected role must match the account's real role.
  */

  loginButton.disabled = true;
  loginButton.textContent = "Logging in...";

  const res = await loginAsync({ email: username, password });

  if (!res.ok || !res.data || !res.data.token) {
    showLoginError(authError(res));
    loginButton.disabled = false;
    loginButton.textContent = "Log in";
    return;
  }

  const actualRole = res.data.role;

  if (actualRole !== role) {
    showLoginError(
      "That account is registered as " + actualRole + ", not " + role + "."
    );
    loginButton.disabled = false;
    loginButton.textContent = "Log in";
    return;
  }

  saveSession(res.data);
  window.location.href = ROLE_HOME[role];
});

function showLoginError(message) {
  loginError.style.display = "none";
  loginError.textContent = message;
  // force reflow so the display change applies before showing again
  void loginError.offsetHeight;
  loginError.style.display = "block";
}