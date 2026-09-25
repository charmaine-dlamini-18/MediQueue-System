// js/pages/receptionist-dashboard.js

function avgWaitMinutes(queue) {
  const mins = queue.map(q => parseInt(q.wait, 10)).filter(n => !isNaN(n));
  if (!mins.length) return "0 min";
  return `${Math.round(mins.reduce((a, b) => a + b, 0) / mins.length)} min`;
}

function renderReceptionistDashboard() {
  const data = getData();
  const role = localStorage.getItem("mq_role");
  const profile = data.profiles[role] || data.profiles.RECEPTIONIST;

  document.getElementById("welcomeMsg").textContent = `Welcome, ${profile.name}`;
  document.getElementById("dateLabel").textContent = new Date().toLocaleDateString("en-ZA", { weekday: "long", day: "numeric", month: "long", year: "numeric" });

  const waiting = data.queue.filter(q => q.status === "Waiting");
  const inConsult = data.queue.find(q => q.status === "In Consultation");

  document.getElementById("statCards").innerHTML = `
    <div class="card"><div class="card-label">Total patients today</div><div class="card-value font-display">45</div><a href="patients.html" class="link" style="font-size:12px;">View all patients →</a></div>
    <div class="card"><div class="card-label">Appointments today</div><div class="card-value font-display">${data.appointments.length}</div><a href="appointments.html" class="link" style="font-size:12px;">View all appointments →</a></div>
    <div class="card"><div class="card-label">Patients in queue</div><div class="card-value font-display" style="color:var(--gold)">${waiting.length}</div><a href="queue.html" class="link" style="font-size:12px;">View queue →</a></div>
    <div class="card"><div class="card-label">Average wait time</div><div class="card-value font-display">${avgWaitMinutes(data.queue)}</div></div>
    <div class="card"><div class="card-label">Available slots</div><div class="card-value font-display">8</div></div>
  `;

  document.getElementById("apptsBody").innerHTML = data.appointments.map(a => `
    <tr><td>${a.time}</td><td>${esc(a.patient)}</td><td>${a.dept}</td><td>${badge(a.status)}</td></tr>
  `).join("");

  document.getElementById("nowServing").innerHTML = inConsult
    ? `<div style="font-size:22px;font-weight:800;color:var(--teal);font-family:'Zilla Slab',Georgia,serif;">${inConsult.no}</div><div style="font-size:13px;color:var(--slate);">${esc(inConsult.patient)} — ${inConsult.dept}</div>`
    : `<div style="font-size:13px;color:var(--slate);">No one currently being served</div>`;

  document.getElementById("nextInLine").innerHTML = waiting.length
    ? waiting.map(q => `<div style="display:flex;justify-content:space-between;padding:6px 0;font-size:13px;"><span>${q.no} — ${esc(q.patient)}</span><span style="color:var(--slate);">${q.dept}</span></div>`).join("")
    : `<div style="font-size:13px;color:var(--slate);">Queue is empty</div>`;

  document.getElementById("recentPatients").innerHTML = data.patients.slice(0, 3).map(p => `
    <div style="display:flex;justify-content:space-between;align-items:center;padding:12px 20px;border-bottom:1px solid var(--line);">
      <div><div style="font-weight:600;font-size:13.5px;">${esc(p.name)}</div><div style="font-size:12px;color:var(--slate);">${p.id}</div></div>
      ${badge("Active")}
    </div>`).join("");

  document.getElementById("announcements").innerHTML = data.announcements.map(a => `
    <div style="margin-bottom:14px;"><div style="font-weight:700;font-size:13.5px;margin-bottom:2px;">${esc(a.title)}</div><div style="font-size:12.5px;color:var(--slate);">${esc(a.body)}</div></div>
  `).join("");

  // populate department dropdowns
  document.querySelectorAll("#npDept, #naDept, #aqDept").forEach(sel => {
    sel.innerHTML = DEPARTMENTS.map(d => `<option>${d.name}</option>`).join("");
  });
  document.getElementById("aqPatient").innerHTML = data.patients.map(p => `<option>${esc(p.name)}</option>`).join("");
}

async function callNext() {
  const data0 = getData();
  const waiting = data0.queue.filter(item => item.status === "Waiting" || item.status === "Ready for Doctor");
  const current = data0.queue.find(item => item.status === "In Consultation");
  const next = waiting[0];

  if (window.__mqConnected) {
    if (current && current.entryId) await setQueueEntryStatusAsync(current.entryId, "Waiting");
    if (next && next.entryId) await setQueueEntryStatusAsync(next.entryId, "In Consultation");
  }

  const data = getData();
  const withoutCurrent = data.queue.filter(item => item.status !== "In Consultation");
  const idx = withoutCurrent.findIndex(item => item.status === "Waiting" || item.status === "Ready for Doctor");
  if (idx !== -1) withoutCurrent[idx] = { ...withoutCurrent[idx], status: "In Consultation" };
  if (current && current.entryId) {
    const ci = withoutCurrent.findIndex(item => item.entryId === current.entryId);
    if (ci === -1) withoutCurrent.push({ ...current, status: "Waiting" });
  }
  data.queue = withoutCurrent;
  setData(data);
  renderReceptionistDashboard();
}

document.addEventListener("DOMContentLoaded", () => {
  renderReceptionistDashboard();
  document.getElementById("callNextBtn").addEventListener("click", callNext);

  document.getElementById("addPatientForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const data = getData();
    const name = document.getElementById("npName").value.trim();
    if (!name) return;
    const id = `P-0${240 + data.patients.length}`;
    const entry = {
      id, name,
      gender: document.getElementById("npGender").value,
      age: Number(document.getElementById("npAge").value) || 0,
      phone: document.getElementById("npPhone").value,
      dept: document.getElementById("npDept").value,
    };
    if (window.__mqConnected) {
      const res = await createPatientAsync({
        patientId: id, name, gender: entry.gender, age: entry.age,
        phone: entry.phone, dept: entry.dept,
      });
      if (res.ok) {
        document.getElementById("addPatientForm").reset();
        closeModal("addPatientModal");
        renderReceptionistDashboard();
        return;
      }
      alert("Could not save the patient to the server – saved locally only.");
    }
    data.patients.unshift(entry);
    setData(data);
    document.getElementById("addPatientForm").reset();
    closeModal("addPatientModal");
    renderReceptionistDashboard();
  });

  document.getElementById("bookApptForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const data = getData();
    const patientName = document.getElementById("naPatient").value.trim();
    const doctorName = document.getElementById("naDoctor").value;
    const dept = document.getElementById("naDept").value;
    const time = document.getElementById("naTime").value;
    const patient = data.patients.find(p => p.name === patientName);
    const doctor = data.users.find(u => userMatchesLabel(u, doctorName));

    if (window.__mqConnected) {
      if (patient && doctor && doctor.id) {
        const res = await createAppointmentAsync({
          appointmentId: uniqueId("APP"),
          patientId: patient.id,
          doctorId: doctor.id,
          scheduledDate: todayIso(),
          scheduledTime: to24h(time),
          appointmentType: "booked",
          status: "Pending",
        });
        if (res.ok) {
          document.getElementById("bookApptForm").reset();
          document.getElementById("naDoctor").value = "Dr. N. Zulu";
          closeModal("bookApptModal");
          renderReceptionistDashboard();
          return;
        }
        alert("Could not save the appointment to the server – saved locally only.");
      } else {
        alert("Could not find the selected patient or doctor on the server.");
      }
    }

    data.appointments.unshift({
      patient: patientName,
      time: document.getElementById("naTime").value.trim(),
      dept,
      doctor: doctorName,
      status: "Pending",
    });
    setData(data);
    document.getElementById("bookApptForm").reset();
    document.getElementById("naDoctor").value = "Dr. N. Zulu";
    closeModal("bookApptModal");
    renderReceptionistDashboard();
  });

  document.getElementById("addQueueForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const data = getData();
    const patientName = document.getElementById("aqPatient").value;
    const dept = document.getElementById("aqDept").value;
    const patient = data.patients.find(p => p.name === patientName);

    if (window.__mqConnected) {
      const doctors = data.users.filter(u => u.department === dept && /doctor/i.test(u.role || ""));
      const doctor = doctors[0] || data.users.find(u => /doctor/i.test(u.role || ""));
      if (patient && doctor && doctor.id) {
        const res = await createQueueEntryAsync({ patientId: patient.id, doctorId: doctor.id });
        if (res.ok) {
          closeModal("addQueueModal");
          renderReceptionistDashboard();
          return;
        }
        alert("Could not add the patient to the server queue – saved locally only.");
      } else {
        alert("Could not resolve the selected patient or a doctor for the queue.");
      }
    }

    const nextNo = `Q0${17 + data.queue.length}`;
    data.queue.push({
      no: nextNo, patient: patientName, dept,
      status: "Waiting", wait: "0 min",
      id: patient ? patient.id : "P-0000", age: patient ? patient.age : 0,
      gender: patient ? patient.gender : "-", phone: patient ? patient.phone : "-",
    });
    setData(data);
    closeModal("addQueueModal");
    renderReceptionistDashboard();
  });
});