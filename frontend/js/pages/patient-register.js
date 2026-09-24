document.addEventListener("DOMContentLoaded", () => {

  const registerForm =
    document.getElementById("patientRegisterForm");

  const passwordInput =
    document.getElementById("registerPassword");

  const confirmPasswordInput =
    document.getElementById("confirmPassword");

  const togglePasswordButton =
    document.getElementById("toggleRegisterPassword");

  const toggleConfirmButton =
    document.getElementById("toggleConfirmPassword");

  const errorMessage =
    document.getElementById("registrationError");

  const submitButton =
    document.querySelector(".patient-register-submit");


  togglePasswordButton.addEventListener("click", () => {

    passwordInput.type =
      passwordInput.type === "password"
        ? "text"
        : "password";

  });


  toggleConfirmButton.addEventListener("click", () => {

    confirmPasswordInput.type =
      confirmPasswordInput.type === "password"
        ? "text"
        : "password";

  });


  registerForm.addEventListener("submit", async (event) => {

    event.preventDefault();

    errorMessage.textContent = "";


    const fullName =
      document.getElementById("fullName").value.trim();

    const idNumber =
      document.getElementById("idNumber").value.trim();

    const phoneNumber =
      document.getElementById("phoneNumber").value.trim().replace(/\s+/g, "");

    const dateOfBirth =
      document.getElementById("dateOfBirth").value;

    const gender =
      document.getElementById("gender").value;

    const address =
      document.getElementById("address").value.trim();

    const allergies =
      document.getElementById("allergies").value.trim();

    const email =
      document.getElementById("emailAddress").value.trim();

    const password =
      passwordInput.value;

    const confirmPassword =
      confirmPasswordInput.value;


    if (!/^[0-9]{13}$/.test(idNumber)) {
      errorMessage.textContent =
        "Please enter a valid 13-digit South African ID number.";
      return;
    }

    if (!email) {
      errorMessage.textContent =
        "Please enter your email address.";
      return;
    }

    if (password !== confirmPassword) {
      errorMessage.textContent =
        "Passwords do not match.";
      return;
    }

    if (password.length < 6) {
      errorMessage.textContent =
        "Password must be at least 6 characters long.";
      return;
    }

    const [firstName, lastName] = splitName(fullName);

    /*
      Register with the backend. The account is only created when the
      server accepted it — no fake success. On success the response
      already contains a session token, so the patient is logged in
      and sent straight to their dashboard.
    */

    if (submitButton) {
      submitButton.disabled = true;
      submitButton.textContent = "Creating account...";
    }

    const res =
      await registerPatientAsync({
        firstName,
        lastName,
        email,
        password,
        phoneNumber,
        idNumber,
        dateOfBirth,
        gender,
        address,
        allergies
      });

    if (!res.ok || !res.data || !res.data.token) {

      errorMessage.textContent =
        authError(res);

      if (submitButton) {
        submitButton.disabled = false;
        submitButton.textContent = "Create Account";
      }

      return;

    }


    if (firstName) {
      localStorage.setItem(
        "mq_patient",
        JSON.stringify({ fullName, patientId: res.data.patientId })
      );
    }


    saveSession(res.data);


    window.location.href =
      "patient-dashboard.html";

  });

});