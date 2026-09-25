document.addEventListener("DOMContentLoaded", () => {

  if (!requireRole("PATIENT")) return;

  const appointmentForm =
    document.getElementById("bookAppointmentForm");

  const patientTopbarName =
    document.getElementById("patientTopbarName");

  const patientIdInput =
    document.getElementById("appointmentPatientId");

  const appointmentDate =
    document.getElementById("appointmentDate");

  const appointmentError =
    document.getElementById("appointmentError");

  const submitButton =
    appointmentForm.querySelector(".btn-primary");

  const patientLogout =
    document.getElementById("patientLogout");


  /*
    Fill in the logged-in patient from the real session. The patient
    ID comes from the backend session and is read-only so bookings can
    never be created for someone else.
  */

  const patientId =
    localStorage.getItem("mq_patientId") || "";

  const patientName =
    localStorage.getItem("mq_name") || "";


  if (patientName) {
    patientTopbarName.textContent =
      patientName.split(" ")[0];
  }

  if (patientId) {
    patientIdInput.value = patientId;
    patientIdInput.setAttribute("readonly", "readonly");
  }


  /*
    Prevent dates before today.
  */

  const today =
    new Date();

  const year =
    today.getFullYear();

  const month =
    String(today.getMonth() + 1).padStart(2, "0");

  const day =
    String(today.getDate()).padStart(2, "0");

  appointmentDate.min =
    `${year}-${month}-${day}`;


  /*
    Resolve the backend clinic/department ids for the selected names so
    the appointment is linked to the real entities on the server.
  */

  function matchIdByKey(key, list, name, nameKey, idKey) {
    const item = (list || []).find((x) => String(x[nameKey]) === String(name));
    return item && item[idKey] ? item[idKey] : null;
  }

  function clinicIdFor(name) {
    const cached = typeof getData === "function" ? getData() : null;
    return matchIdByKey("clinics", cached && cached.clinics, name, "name", "id");
  }

  function departmentIdFor(name) {
    const cached = typeof getData === "function" ? getData() : null;
    return matchIdByKey("departments", cached && cached.departments, name, "name", "id");
  }


  /*
    Handle appointment form submission. Bookings are created without a
    doctor — staff assign one later. The backend only accepts the
    booking when the caller is authenticated.
  */

  appointmentForm.addEventListener("submit", async (event) => {

    event.preventDefault();

    appointmentError.textContent = "";


    const clinic =
      document.getElementById(
        "appointmentClinic"
      ).value;

    const department =
      document.getElementById(
        "appointmentDepartment"
      ).value;

    const date =
      appointmentDate.value;

    const time =
      document.getElementById(
        "appointmentTime"
      ).value;

    const reason =
      document.getElementById(
        "appointmentReason"
      ).value.trim();


    if (
      !patientId ||
      !clinic ||
      !department ||
      !date ||
      !time ||
      !reason
    ) {

      appointmentError.textContent =
        "Please complete all appointment fields.";

      return;

    }


    if (!getToken()) {
      window.location.href = "patient-login.html";
      return;
    }


    if (submitButton) {
      submitButton.disabled = true;
      submitButton.textContent = "Booking...";
    }


    const saved =
      await createAppointmentAsync({
        appointmentId: uniqueId("APP"),
        patientId,
        clinicId: clinicIdFor(clinic),
        departmentId: departmentIdFor(department),
        reason,
        scheduledDate: date,
        scheduledTime: to24h(time),
        appointmentType: "booked",
        status: "Pending"
      });


    if (saved.status === 401) {
      clearSession();
      window.location.href = "patient-login.html";
      return;
    }

    if (!saved.ok) {

      appointmentError.textContent =
        (saved.data && saved.data.error) ||
        "Could not create the booking. Please try again.";

      if (submitButton) {
        submitButton.disabled = false;
        submitButton.textContent = "Confirm";
      }

      return;

    }


    /*
      Persist a summary for the confirmation page (the real appointment
      id + the form values). The confirmation page renders from this.
    */

    const createdId =
      (saved.created && saved.created.appointmentId) || "";

    localStorage.setItem(
      "mq_appointment",
      JSON.stringify({
        appointmentId: createdId,
        patientId,
        clinic,
        department,
        date,
        time,
        reason,
        status: "Pending"
      })
    );


    window.location.href =
      "appointment-confirmed.html";

  });


  /*
    Logout
  */

  if (patientLogout) {
    patientLogout.addEventListener("click", (e) => {
      e.preventDefault();
      mqLogout();
    });
  }

});

/* =========================================================
   PATIENT MOBILE MENU
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

  const menuButton =
    document.getElementById("patientMenuButton");

  const closeButton =
    document.getElementById("patientMenuClose");

  const sidebar =
    document.getElementById("patientSidebar");

  const overlay =
    document.getElementById("patientMenuOverlay");


  if (
    !menuButton ||
    !closeButton ||
    !sidebar ||
    !overlay
  ) {
    return;
  }


  function openPatientMenu() {

    sidebar.classList.add(
      "patient-sidebar-open"
    );

    overlay.classList.add(
      "patient-menu-overlay-open"
    );

    document.body.classList.add(
      "patient-menu-open"
    );

    menuButton.setAttribute(
      "aria-expanded",
      "true"
    );
  }


  function closePatientMenu() {

    sidebar.classList.remove(
      "patient-sidebar-open"
    );

    overlay.classList.remove(
      "patient-menu-overlay-open"
    );

    document.body.classList.remove(
      "patient-menu-open"
    );

    menuButton.setAttribute(
      "aria-expanded",
      "false"
    );
  }


  menuButton.addEventListener(
    "click",
    openPatientMenu
  );


  closeButton.addEventListener(
    "click",
    closePatientMenu
  );


  overlay.addEventListener(
    "click",
    closePatientMenu
  );


  sidebar
    .querySelectorAll(".nav-item")
    .forEach(link => {

      link.addEventListener(
        "click",
        closePatientMenu
      );

    });


  document.addEventListener(
    "keydown",
    event => {

      if (event.key === "Escape") {
        closePatientMenu();
      }

    }
  );

});