document.addEventListener("DOMContentLoaded", () => {

  const form = document.getElementById("addDepartmentForm");

  form.addEventListener("submit", async (event) => {

    event.preventDefault();

    const data = getData();

    const departmentId =
      document
        .getElementById("departmentId")
        .value
        .trim();

    const departmentName =
      document
        .getElementById("departmentName")
        .value
        .trim();

    const departmentDescription =
      document
        .getElementById("departmentDescription")
        .value
        .trim();

    /*
      Create the department on the backend when it is reachable.
    */
    if (window.__mqConnected) {

      const saved =
        await createDepartmentAsync({
          departmentId,
          departmentName,
          description: departmentDescription
        });

      if (!saved.ok) {
        alert(
          "Could not save the department to the server – saved locally only."
        );
      }

    }

    const newDepartment = {

      id: departmentId,

      name: departmentName,

      description: departmentDescription,

      dateCreated: new Date().toLocaleDateString("en-ZA", {
        day: "2-digit",
        month: "short",
        year: "numeric"
      })

    };

    data.departments.push(newDepartment);

    setData(data);

    window.location.href = "department-success.html";

  });

});