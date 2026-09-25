document.addEventListener("DOMContentLoaded", () => {

  const loginForm =
    document.getElementById("patientLoginForm");

  const passwordInput =
    document.getElementById("patientPassword");

  const togglePassword =
    document.getElementById("togglePatientPassword");

  const errorEl =
    document.getElementById("patientLoginError");

  const loginButton =
    document.getElementById("patientLoginButton");


  togglePassword.addEventListener("click", () => {

    if (passwordInput.type === "password") {
      passwordInput.type = "text";
    } else {
      passwordInput.type = "password";
    }

  });


  loginForm.addEventListener("submit", async (event) => {

    event.preventDefault();

    errorEl.textContent = "";

    const email =
      document
        .getElementById("patientEmail")
        .value
        .trim();

    const password =
      passwordInput
        .value
        .trim();


    if (!email || !password) {
      errorEl.textContent =
        "Please enter your email and password.";
      return;
    }


    /*
      Authenticate against the backend. A session token is only saved
      when the server actually validated the credentials — no fake
      logins allowed.
    */

    loginButton.disabled = true;
    loginButton.textContent = "Logging in...";

    const res =
      await loginAsync({ email, password });


    if (!res.ok || !res.data || !res.data.token) {

      errorEl.textContent =
        authError(res);

      loginButton.disabled = false;
      loginButton.textContent = "Login";
      return;

    }


    /*
      Only a PATIENT role can use the patient portal. Anyone else is
      redirected to their own dashboard.
    */

    const role = res.data.role;
    saveSession(res.data);


    if (role === "PATIENT") {
      window.location.href =
        "patient-dashboard.html";
    } else {
      window.location.href =
        homeForRole(role);
    }

  });

});