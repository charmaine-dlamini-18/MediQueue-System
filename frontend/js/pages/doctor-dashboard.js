// js/pages/doctor-dashboard.js

let consultDraft = {
  complaint: "",
  diagnosis: "",
  notes: ""
};


/* =========================================================
   HELPERS
========================================================= */

function currentPatient(data) {
  return data.queue.find(
    patient => patient.status === "In Consultation"
  );
}


function getTodayLabel() {
  return new Date().toLocaleDateString("en-ZA", {
    day: "2-digit",
    month: "short",
    year: "numeric"
  });
}


function getTodayISO() {
  return new Date().toISOString().split("T")[0];
}


/*
  Different parts of the frontend may use different names
  for the vital-signs collection.

  This lets the Doctor dashboard work even if the Nurse
  workflow stores the information as:

  data.vitalSigns
  data.vitals
  data.vital_signs
*/
function getVitalSignsCollection(data) {
  if (Array.isArray(data.vitalSigns)) {
    return data.vitalSigns;
  }

  if (Array.isArray(data.vitals)) {
    return data.vitals;
  }

  if (Array.isArray(data.vital_signs)) {
    return data.vital_signs;
  }

  return [];
}


/*
  Find the most recent vital signs recorded for the
  current patient.

  We check several common property names so that the
  Doctor page can connect to the Nurse workflow without
  breaking older mock data.
*/
function getPatientVitals(data, patient) {
  const vitalSigns = getVitalSignsCollection(data);

  const matches = vitalSigns.filter(vital => {
    const vitalPatientId =
      vital.patientId ||
      vital.patient_id ||
      vital.patientID ||
      "";

    const vitalPatientName =
      vital.patient ||
      vital.patientName ||
      vital.patient_name ||
      "";

    return (
      String(vitalPatientId) === String(patient.id) ||
      String(vitalPatientName).toLowerCase() ===
        String(patient.patient).toLowerCase()
    );
  });

  if (matches.length === 0) {
    return null;
  }

  return matches[matches.length - 1];
}


function getVitalValue(vitals, possibleKeys, fallback = "Not recorded") {
  if (!vitals) {
    return fallback;
  }

  for (const key of possibleKeys) {
    if (
      vitals[key] !== undefined &&
      vitals[key] !== null &&
      vitals[key] !== ""
    ) {
      return vitals[key];
    }
  }

  return fallback;
}


function callNext(data) {
  /*
    If a patient is already in consultation,
    we do not replace them.
  */
  const existing = currentPatient(data);

  if (existing) {
    return existing;
  }

  const nextPatient = data.queue.find(
    patient => patient.status === "Waiting"
  );

  if (!nextPatient) {
    return null;
  }

  nextPatient.status = "In Consultation";

  return nextPatient;
}


/* =========================================================
   DASHBOARD COUNTS
========================================================= */

function getConsultationsToday(data) {
  const today = getTodayISO();

  return data.records.filter(record => {
    return (
      record.visitDate === today ||
      record.date === today ||
      record.createdDate === today
    );
  }).length;
}


/* =========================================================
   RENDER DASHBOARD
========================================================= */

function renderDoctorDashboard() {
  const data = getData();

  const inConsult = currentPatient(data);

  const waitingCount = data.queue.filter(
    patient => patient.status === "Waiting"
  ).length;


  /* ---------- Summary cards ---------- */

  document.getElementById("waitingCount").textContent =
    waitingCount;

  document.getElementById("seenTodayVal").textContent =
    data.seenToday || 0;

  document.getElementById("consultCount").textContent =
    getConsultationsToday(data);


  /* ---------- Queue label ---------- */

  document.getElementById("queueNoLabel").textContent =
    inConsult
      ? `Queue ${inConsult.no}`
      : "";


  const area = document.getElementById("consultArea");


  /* =====================================================
     NO CURRENT PATIENT
  ===================================================== */

  if (!inConsult) {
    area.innerHTML = `
      <div class="empty-state">
        <div style="margin-bottom:12px;">
          No patient is currently in consultation.
        </div>

        ${
          waitingCount > 0
            ? `
              <button
                class="btn btn-gold"
                type="button"
                id="callNextPatientBtn"
              >
                Call next patient →
              </button>
            `
            : `
              <div style="
                font-size:12px;
                color:var(--slate);
              ">
                There are currently no patients waiting.
              </div>
            `
        }
      </div>
    `;

    const callButton =
      document.getElementById("callNextPatientBtn");

    if (callButton) {
      callButton.addEventListener(
        "click",
        callNextPatient
      );
    }

    return;
  }


  /* =====================================================
     CURRENT PATIENT
  ===================================================== */

  const vitals = getPatientVitals(data, inConsult);


  const temperature = getVitalValue(
    vitals,
    ["temperature", "temp"]
  );

  const systolic = getVitalValue(
    vitals,
    ["systolic", "bpSystolic", "bloodPressureSystolic"],
    ""
  );

  const diastolic = getVitalValue(
    vitals,
    ["diastolic", "bpDiastolic", "bloodPressureDiastolic"],
    ""
  );

  const directBP = getVitalValue(
    vitals,
    ["bp", "bloodPressure"],
    ""
  );

  const bloodPressure =
    directBP ||
    (
      systolic && diastolic
        ? `${systolic}/${diastolic}`
        : "Not recorded"
    );

  const pulse = getVitalValue(
    vitals,
    ["heartRate", "heart_rate", "pulse"]
  );

  const weight = getVitalValue(
    vitals,
    ["weight"]
  );


  area.innerHTML = `

    <div style="padding:20px;">

      <!-- ================= PATIENT DETAILS ================= -->

      <div class="consult-header">

        <div class="consult-avatar">

          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <circle cx="12" cy="8" r="4"/>
            <path d="M4 21v-2a6 6 0 0 1 12 0v2"/>
          </svg>

        </div>

        <div>

          <div class="consult-name">
            ${esc(inConsult.patient)}
          </div>

          <div class="consult-meta">

            ${esc(inConsult.id || "")}

            ${
              inConsult.gender
                ? ` · ${esc(inConsult.gender)}`
                : ""
            }

            ${
              inConsult.age
                ? `, ${esc(String(inConsult.age))} yrs`
                : ""
            }

            ${
              inConsult.phone
                ? ` · ${esc(inConsult.phone)}`
                : ""
            }

          </div>

        </div>

      </div>


      <!-- ================= VITAL SIGNS ================= -->

      <div style="
        margin-top:22px;
        margin-bottom:22px;
        padding:18px;
        border:1px solid var(--line);
        border-radius:10px;
        background:var(--bg);
      ">

        <div style="
          display:flex;
          justify-content:space-between;
          align-items:center;
          margin-bottom:15px;
        ">

          <div>

            <div style="
              font-size:13px;
              font-weight:700;
              color:var(--ink);
            ">
              Latest Vital Signs
            </div>

            <div style="
              font-size:11px;
              color:var(--slate);
              margin-top:3px;
            ">
              Recorded by the nursing team
            </div>

          </div>

          ${
            vitals
              ? `
                <span
                  class="badge"
                  style="
                    background:var(--teal-tint);
                    color:var(--teal);
                  "
                >
                  Recorded
                </span>
              `
              : `
                <span
                  style="
                    font-size:11px;
                    color:var(--slate);
                  "
                >
                  No vitals found
                </span>
              `
          }

        </div>


        <div class="consult-grid4">

          <!-- Temperature -->

          <div>

            <div style="
              font-size:11px;
              color:var(--slate);
              margin-bottom:4px;
            ">
              Temperature
            </div>

            <div style="
              font-size:16px;
              font-weight:700;
              color:var(--ink);
            ">
              ${
                temperature !== "Not recorded"
                  ? `${esc(String(temperature))} °C`
                  : "Not recorded"
              }
            </div>

          </div>


          <!-- Blood Pressure -->

          <div>

            <div style="
              font-size:11px;
              color:var(--slate);
              margin-bottom:4px;
            ">
              Blood Pressure
            </div>

            <div style="
              font-size:16px;
              font-weight:700;
              color:var(--ink);
            ">
              ${
                bloodPressure !== "Not recorded"
                  ? `${esc(String(bloodPressure))} mmHg`
                  : "Not recorded"
              }
            </div>

          </div>


          <!-- Heart Rate -->

          <div>

            <div style="
              font-size:11px;
              color:var(--slate);
              margin-bottom:4px;
            ">
              Heart Rate
            </div>

            <div style="
              font-size:16px;
              font-weight:700;
              color:var(--ink);
            ">
              ${
                pulse !== "Not recorded"
                  ? `${esc(String(pulse))} bpm`
                  : "Not recorded"
              }
            </div>

          </div>


          <!-- Weight -->

          <div>

            <div style="
              font-size:11px;
              color:var(--slate);
              margin-bottom:4px;
            ">
              Weight
            </div>

            <div style="
              font-size:16px;
              font-weight:700;
              color:var(--ink);
            ">
              ${
                weight !== "Not recorded"
                  ? `${esc(String(weight))} kg`
                  : "Not recorded"
              }
            </div>

          </div>

        </div>

      </div>


      <!-- ================= CONSULTATION FORM ================= -->

      <div class="field">

        <label>
          Chief complaint
        </label>

        <input
          class="input"
          id="cComplaint"
          type="text"
          placeholder="Reason for today's consultation"
          value="${esc(consultDraft.complaint)}"
        />

      </div>


      <div class="field">

        <label>
          Diagnosis
        </label>

        <input
          class="input"
          id="cDiagnosis"
          type="text"
          placeholder="Enter diagnosis"
          value="${esc(consultDraft.diagnosis)}"
        />

      </div>


      <div class="field">

        <label>
          Consultation notes
        </label>

        <textarea
          class="input"
          id="cNotes"
          rows="4"
          placeholder="Add examination findings, treatment plan or other clinical notes"
        >${esc(consultDraft.notes)}</textarea>

      </div>


      <!-- ================= ACTIONS ================= -->

      <div class="consult-actions">

        <button
          class="btn btn-ghost"
          type="button"
          id="viewHistoryBtn"
        >
          View history
        </button>


        <button
          class="btn btn-primary"
          type="button"
          id="saveRecordBtn"
        >
          Save record
        </button>


        <button
          class="btn btn-gold"
          type="button"
          id="completeConsultBtn"
        >
          Complete & Next Patient →
        </button>

      </div>

    </div>
  `;


  /* =====================================================
     CONSULTATION FORM EVENTS
  ===================================================== */

  document
    .getElementById("cComplaint")
    .addEventListener("input", event => {
      consultDraft.complaint =
        event.target.value;
    });


  document
    .getElementById("cDiagnosis")
    .addEventListener("input", event => {
      consultDraft.diagnosis =
        event.target.value;
    });


  document
    .getElementById("cNotes")
    .addEventListener("input", event => {
      consultDraft.notes =
        event.target.value;
    });


  document
    .getElementById("viewHistoryBtn")
    .addEventListener(
      "click",
      viewPatientHistory
    );


  document
    .getElementById("saveRecordBtn")
    .addEventListener(
      "click",
      saveRecord
    );


  document
    .getElementById("completeConsultBtn")
    .addEventListener(
      "click",
      completeConsultation
    );
}


/* =========================================================
   VALIDATION
========================================================= */

function validateConsultation() {
  if (!consultDraft.complaint.trim()) {
    alert(
      "Please enter the patient's chief complaint."
    );

    document
      .getElementById("cComplaint")
      ?.focus();

    return false;
  }


  if (!consultDraft.diagnosis.trim()) {
    alert(
      "Please enter a diagnosis before saving the consultation."
    );

    document
      .getElementById("cDiagnosis")
      ?.focus();

    return false;
  }


  return true;
}


/* =========================================================
   CREATE / UPDATE PATIENT RECORD
========================================================= */

function saveConsultationRecord(data, patient) {
  const todayISO = getTodayISO();

  /*
    We give the consultation a stable reference linked
    to the current queue entry.

    This allows "Save record" to update the same
    consultation rather than adding duplicates every
    time the Doctor presses Save.
  */
  const consultationRef =
    `CONS-${patient.no}`;


  const existingIndex =
    data.records.findIndex(record =>
      record.consultationRef === consultationRef
    );


  const record = {
    consultationRef: consultationRef,

    patient: patient.patient,

    patientId: patient.id,

    complaint:
      consultDraft.complaint.trim(),

    diagnosis:
      consultDraft.diagnosis.trim(),

    notes:
      consultDraft.notes.trim(),

    /*
      Keep condition and lastVisit because the existing
      doctor-records.js currently expects those fields.
    */
    condition:
      consultDraft.diagnosis.trim(),

    lastVisit:
      getTodayLabel(),

    visitDate:
      todayISO,

    doctor:
      "Dr. N. Zulu",

    department:
      patient.dept || "General Medicine",

    queueNo:
      patient.no,

    status:
      "Completed"
  };


  if (existingIndex !== -1) {
    data.records[existingIndex] = {
      ...data.records[existingIndex],
      ...record
    };
  } else {
    data.records.unshift(record);
  }
}


/* =========================================================
   SAVE RECORD
========================================================= */

async function saveRecord() {
  if (!validateConsultation()) {
    return;
  }


  const data = getData();

  const patient = currentPatient(data);


  if (!patient) {
    alert(
      "There is no patient currently in consultation."
    );

    return;
  }


  saveConsultationRecord(
    data,
    patient
  );


  setData(data);


  /*
    Persist the consultation on the backend when possible.
  */
  if (window.__mqConnected && patient.id) {

    const doctor =
      (data.users || []).find(u =>
        userMatchesLabel(u, "Dr. N. Zulu")
      );

    if (doctor && doctor.id) {

      const saved =
        await apiRequest(
          "POST",
          "/medical-record/create",
          buildMedicalRecordPayload({
            recordId: uniqueId("REC"),
            patientId: patient.id,
            doctorId: doctor.id,
            diagnosis: consultDraft.diagnosis.trim(),
            notes: consultDraft.notes.trim(),
            recordDate: getTodayISO()
          })
        );

      if (!saved.ok) {
        alert(
          "Consultation record could not be saved to the server."
        );
      }

    }

  }


  alert(
    "Consultation record saved successfully."
  );


  renderDoctorDashboard();
}


/* =========================================================
   COMPLETE CONSULTATION
========================================================= */

async function completeConsultation() {
  if (!validateConsultation()) {
    return;
  }


  const data = getData();

  const patient = currentPatient(data);


  if (!patient) {
    return;
  }


  /*
    Backend: complete this patient's queue entry.
  */
  if (window.__mqConnected && patient.entryId) {

    const queued =
      await setQueueEntryStatusAsync(
        patient.entryId,
        "Completed"
      );

    if (!queued.ok) {
      alert(
        "Could not update the queue on the server."
      );
    }

  }


  /*
    Save the consultation before removing the patient.
  */
  saveConsultationRecord(
    data,
    patient
  );


  /*
    Remove the completed patient from the active queue.
  */
  data.queue = data.queue.filter(
    item => item.no !== patient.no
  );


  /*
    Count patient as seen today.
  */
  data.seenToday =
    (data.seenToday || 0) + 1;


  /*
    Automatically call the next waiting patient.
  */
  callNext(data);


  setData(data);


  /*
    Clear the Doctor's form for the next patient.
  */
  consultDraft = {
    complaint: "",
    diagnosis: "",
    notes: ""
  };


  alert(
    `${patient.patient}'s consultation has been completed.`
  );


  renderDoctorDashboard();
}


/* =========================================================
   CALL NEXT PATIENT
========================================================= */

async function callNextPatient() {
  const data = getData();

  const patient = callNext(data);


  if (!patient) {
    alert(
      "There are no patients waiting in the queue."
    );

    return;
  }


  /*
    Persist the "In Consultation" status on the backend.
  */
  if (window.__mqConnected && patient.entryId) {

    const updated =
      await setQueueEntryStatusAsync(
        patient.entryId,
        "In Consultation"
      );

    if (!updated.ok) {
      setData(data);
    }

  } else {

    setData(data);

  }


  consultDraft = {
    complaint: "",
    diagnosis: "",
    notes: ""
  };


  renderDoctorDashboard();
}


/* =========================================================
   VIEW PATIENT HISTORY
========================================================= */

function viewPatientHistory() {
  const data = getData();

  const patient = currentPatient(data);


  if (!patient) {
    return;
  }


  /*
    Store the selected patient so the Patient Records
    page can use it when we upgrade that page next.
  */
  localStorage.setItem(
    "mq_selected_patient_id",
    patient.id || ""
  );

  localStorage.setItem(
    "mq_selected_patient_name",
    patient.patient || ""
  );


  window.location.href =
    "doctor-records.html";
}


/* =========================================================
   MY APPOINTMENTS
========================================================= */

function getAppointmentPatientName(appointment) {
  const patient = appointment.patient;
  if (!patient) {
    return "Unknown patient";
  }
  const name = patient.name || patient.patientName || patient.patient || "";
  const id = patient.patientId || patient.userId || "";
  return name ? `${name}${id ? " · " + id : ""}` : (id || "Unknown patient");
}

function getAppointmentLabel(appointment) {
  const clinic = appointment.clinic && appointment.clinic.clinicName;
  const dept = appointment.department && appointment.department.departmentName;
  return [clinic, dept].filter(Boolean).join(" · ") || "General Medicine";
}

async function loadDoctorAppointments() {
  const listEl = document.getElementById("doctorAppointmentsList");
  if (!listEl) {
    return;
  }

  const doctorId = localStorage.getItem("mq_staffId");

  if (!doctorId) {
    listEl.innerHTML =
      '<div class="admin-appointments-empty" style="font-size:13px;color:var(--slate);padding:12px 0;">Log in again to display your appointments.</div>';
    return;
  }

  const res = await apiRequest("GET", "/appointment/doctor/" + encodeURIComponent(doctorId));

  if (!res.ok) {
    listEl.innerHTML =
      '<div style="font-size:13px;color:var(--slate);padding:12px 0;">Could not load appointments.</div>';
    return;
  }

  const appointments = Array.isArray(res.data) ? res.data : [];

  const statusOrder = {
    Confirmed: 0,
    Scheduled: 1,
    Pending: 2,
    Completed: 3,
    Cancelled: 4
  };

  const sorted = [...appointments].sort((a, b) => {
    const byStatus = (statusOrder[a.status] ?? 9) - (statusOrder[b.status] ?? 9);
    if (byStatus !== 0) {
      return byStatus;
    }
    return String(a.scheduledDate).localeCompare(String(b.scheduledDate));
  });

  if (sorted.length === 0) {
    listEl.innerHTML =
      '<div style="font-size:13px;color:var(--slate);padding:12px 0;">No appointments have been assigned to you yet.</div>';
    return;
  }

  listEl.innerHTML = sorted.map(appointment => `
    <div style="
      display:flex;
      align-items:center;
      gap:14px;
      padding:14px 0;
      border-bottom:1px solid var(--line, #e9ebf0);
    ">
      <div style="
        min-width:44px;
        text-align:center;
      ">
        <div style="
          font-size:20px;
          font-weight:800;
          color:var(--ink, #132238);
          line-height:1;
        ">
          ${esc(formatDay(appointment.scheduledDate))}
        </div>
        <div style="
          font-size:10px;
          color:var(--slate, #6b7280);
          margin-top:3px;
          text-transform:uppercase;
        ">
          ${esc(formatMonth(appointment.scheduledDate))}
        </div>
      </div>

      <div style="flex:1;min-width:0;">
        <div style="
          font-size:14px;
          font-weight:700;
          color:var(--ink, #132238);
        ">
          ${esc(getAppointmentPatientName(appointment))}
        </div>
        <div style="
          font-size:12px;
          color:var(--slate, #6b7280);
          margin-top:2px;
        ">
          ${esc(appointment.reason || "General consultation")}
        </div>
        <div style="
          font-size:12px;
          color:var(--teal, #0d9488);
          margin-top:2px;
        ">
          ${esc(getAppointmentLabel(appointment))} ·
          ${esc(appointment.scheduledTime || "TBC")}
        </div>
      </div>

      <span class="badge" style="
        background:${appointment.status === "Confirmed" ? "var(--teal-tint, #e0f2f1)" : "var(--gold-tint, #fff4d6)"};
        color:${appointment.status === "Confirmed" ? "var(--teal, #0d9488)" : "var(--gold, #b8860b)"};
        white-space:nowrap;
      ">
        ${esc(appointment.status || "Scheduled")}
      </span>
    </div>
  `).join("");
}

function formatDay(dateValue) {
  if (!dateValue) {
    return "--";
  }
  return String(dateValue).slice(8, 10).replace(/^0/, "");
}

function formatMonth(dateValue) {
  if (!dateValue) {
    return "";
  }
  const months = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
  const month = parseInt(String(dateValue).slice(5, 7), 10);
  return months[month - 1] || "";
}


/* =========================================================
   START
========================================================= */

document.addEventListener(
  "DOMContentLoaded",
  () => {
    renderDoctorDashboard();
    loadDoctorAppointments();
  }
);