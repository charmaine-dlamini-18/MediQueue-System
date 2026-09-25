document.addEventListener("DOMContentLoaded", () => {

  const appointment =
    JSON.parse(localStorage.getItem("mq_appointment") || "null");


  /*
    Guard against visiting this page without a real booking.
  */

  if (!appointment || !appointment.patientId) {
    window.location.href = "patient-login.html";
    return;
  }


  const patientName =
    localStorage.getItem("mq_name") || "Patient";


  /*
    Format appointment date.
  */

  let formattedDate =
    appointment.date;

  if (appointment.date) {

    const date =
      new Date(`${appointment.date}T00:00:00`);

    formattedDate =
      date.toLocaleDateString(
        "en-ZA",
        {
          day: "numeric",
          month: "long",
          year: "numeric"
        }
      );

  }


  /*
    Display appointment information.
  */

  document.getElementById(
    "confirmedClinic"
  ).textContent =
    appointment.clinic;


  document.getElementById(
    "confirmedBookingId"
  ).textContent =
    appointment.appointmentId || "Pending";


  document.getElementById(
    "confirmedQueueNumber"
  ).textContent =
    appointment.queueNumber || "—";


  document.getElementById(
    "confirmedPatientName"
  ).textContent =
    patientName;


  document.getElementById(
    "confirmedPatientId"
  ).textContent =
    appointment.patientId;


  document.getElementById(
    "confirmedDate"
  ).textContent =
    formattedDate;


  document.getElementById(
    "confirmedTime"
  ).textContent =
    appointment.time;


  document.getElementById(
    "confirmedLocation"
  ).textContent =
    appointment.clinic;


  document.getElementById(
    "confirmedDepartment"
  ).textContent =
    appointment.department;


  document.getElementById(
    "confirmedReason"
  ).textContent =
    appointment.reason;


  /*
    Booking confirmation time.
  */

  const confirmedAt =
    new Date();


  document.getElementById(
    "confirmedTimestamp"
  ).textContent =
    `Booking confirmed on ${confirmedAt.toLocaleDateString(
      "en-ZA",
      {
        day: "numeric",
        month: "long",
        year: "numeric"
      }
    )} at ${confirmedAt.toLocaleTimeString(
      "en-ZA",
      {
        hour: "2-digit",
        minute: "2-digit"
      }
    )}`;


  /*
    Delete appointment — removes it from the backend too, but only if
    confirmation is given.
  */

  document.getElementById(
    "deleteAppointmentBtn"
  ).addEventListener("click", async () => {

    const confirmed =
      window.confirm(
        "Are you sure you want to delete this appointment?"
      );


    if (!confirmed) {
      return;
    }


    if (appointment.appointmentId) {

      const deleteBtn =
        document.getElementById("deleteAppointmentBtn");

      deleteBtn.disabled = true;
      deleteBtn.textContent = "Deleting...";

      const res =
        await deleteAppointmentAsync(appointment.appointmentId);


      if (res.status === 401) {
        clearSession();
        window.location.href = "patient-login.html";
        return;
      }

    }


    localStorage.removeItem(
      "mq_appointment"
    );


    window.location.href =
      "patient-dashboard.html";

  });

});