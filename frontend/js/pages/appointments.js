// js/pages/appointments.js
// The table renders real appointments from the backend-synced cache.
// Create/edit/delete write through to the REST API (and update the cache),
// so the list always reflects what is actually in the database.

function renderAppts() {
  const data = getData();
  const body = document.getElementById("apptsBody");
  document.getElementById("apptsBodyCount").textContent = `${data.appointments.length} records`;
  body.innerHTML = data.appointments.map((a) => `
    <tr>
      <td>${esc(a.time)}</td>
      <td>${esc(a.patient)}</td>
      <td>${esc(a.dept)}</td>
      <td>${a.doctor ? esc(a.doctor) : '<span class="muted">Unassigned</span>'}</td>
      <td>${badge(a.status)}</td>
      <td class="action-cell">
        <button class="btn btn-ghost btn-sm" onclick="openEditAppt('${esc(a.id)}')">Edit</button>
        <button class="btn btn-danger btn-sm" onclick="deleteAppt('${esc(a.id)}')">Delete</button>
      </td>
    </tr>
  `).join("");
}

function populateDeptDropdowns() {
  document.querySelectorAll("#naDept, #eaDept").forEach(sel => {
    if (sel) sel.innerHTML = DEPARTMENTS.map(d => `<option>${d.name}</option>`).join("");
  });
}

function populateDoctorDropdowns() {
  const data = getData();
  const doctors = (data.users || []).filter((u) => /doctor/i.test(u.role || "") && u.id);
  document.querySelectorAll("#eaDoctor").forEach(sel => {
    if (sel) sel.innerHTML = doctors.map(d => `<option value="${esc(d.id)}">${esc(d.name)}</option>`).join("");
  });
}

function openEditAppt(appointmentId) {
  const data = getData();
  const a = (data.appointments || []).find((x) => x.id === appointmentId);
  if (!a) return;
  document.getElementById("eaEditId").value = appointmentId;
  document.getElementById("eaPatient").value = a.patient;
  document.getElementById("eaDept").value = a.dept;
  const docSelect = document.getElementById("eaDoctor");
  docSelect.value = a.doctorId || "";
  if (!docSelect.value) docSelect.value = (docSelect.options[0] || {}).value || "";
  document.getElementById("eaTime").value = a.time;
  document.getElementById("eaStatus").value = a.status;
  openModal("editApptModal");
}

async function deleteAppt(appointmentId) {
  const data = getData();
  const a = (data.appointments || []).find((x) => x.id === appointmentId);
  if (!a) return;
  if (!confirm(`Cancel and remove ${a.patient}'s appointment at ${a.time}? This can't be undone.`)) return;
  const res = await deleteAppointmentAsync(appointmentId);
  if (res.ok) {
    renderAppts();
  } else {
    alert("Could not delete the appointment. Please try again.");
  }
}

document.addEventListener("DOMContentLoaded", () => {
  renderAppts();
  populateDeptDropdowns();
  populateDoctorDropdowns();

  document.getElementById("bookApptForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const data = getData();

    const patientName = document.getElementById("naPatient").value.trim();
    const dept = document.getElementById("naDept").value;
    const time = document.getElementById("naTime").value.trim();
    const doctorLabel = document.getElementById("naDoctor").value.trim();

    if (!patientName || !time) return;

    const patient = (data.patients || []).find((p) =>
      String(p.name || "").toLowerCase().replace(/\s+/g, " ") ===
      String(patientName).toLowerCase().replace(/\s+/g, " ")
    );

    if (!patient || !patient.id) {
      alert("Could not find that patient. Check the patient list for the exact name.");
      return;
    }

    const doctor = (data.users || []).find((u) =>
      userMatchesLabel(u, doctorLabel)
    );

    const department = (data.departments || []).find((d) => d.name === dept);

    const res = await createAppointmentAsync({
      appointmentId: uniqueId("APP"),
      patientId: patient.id,
      doctorId: doctor ? doctor.id : null,
      clinicId: (data.clinics && data.clinics[0] && data.clinics[0].id) || null,
      departmentId: department ? department.id : null,
      reason: "Booked at reception",
      scheduledDate: todayIso(),
      scheduledTime: to24h(time),
      appointmentType: "booked",
      status: "Pending"
    });

    if (!res.ok) {
      alert(res.data && res.data.error ? res.data.error : "Could not create the booking.");
      return;
    }

    document.getElementById("bookApptForm").reset();
    populateDoctorDropdowns();
    populateDeptDropdowns();
    closeModal("bookApptModal");
    renderAppts();
  });

  document.getElementById("editApptForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const appointmentId = document.getElementById("eaEditId").value;
    const newDoctorId = document.getElementById("eaDoctor").value;
    const newStatus = document.getElementById("eaStatus").value;

    if (!appointmentId) return;

    if (newDoctorId) {
      await assignAppointmentDoctorAsync(appointmentId, newDoctorId);
    }

    const statusRes = await updateAppointmentStatusAsync(appointmentId, newStatus);
    if (!statusRes.ok) {
      alert("Could not update the appointment. Please try again.");
    }

    closeModal("editApptModal");
    renderAppts();
  });
});