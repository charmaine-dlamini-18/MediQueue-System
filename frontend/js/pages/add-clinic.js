document.addEventListener("DOMContentLoaded", () => {

  const form = document.getElementById("addClinicPageForm");

  form.addEventListener("submit", async (event) => {

    event.preventDefault();

    const data = getData();

    const clinicName =
      document
        .getElementById("clinicName")
        .value
        .trim();

    const clinicLocation =
      document
        .getElementById("clinicLocation")
        .value
        .trim();

    const clinicContact =
      document
        .getElementById("clinicContact")
        .value
        .trim();

    const clinicId =
      `CL${String(data.clinics.length + 1).padStart(3, "0")}`;

    /*
      Create the clinic on the backend when it is reachable.
    */
    if (window.__mqConnected) {

      const saved =
        await createClinicAsync({
          clinicId,
          clinicName,
          location: clinicLocation,
          contactNumber: clinicContact
        });

      if (!saved.ok) {
        alert(
          "Could not save the clinic to the server – saved locally only."
        );
      }

    }

    const newClinic = {
      id: clinicId,
      name: clinicName,
      location: clinicLocation,
      contact: clinicContact,
      dateCreated: new Date().toLocaleDateString("en-ZA", {
        day: "2-digit",
        month: "short",
        year: "numeric"
      })
    };

    data.clinics.push(newClinic);

    setData(data);

    window.location.href = "clinic-success.html";

  });

});