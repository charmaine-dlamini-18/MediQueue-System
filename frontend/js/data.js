// js/data.js
// Loaded on every page. Tries to pull real data from the MediQueue
// backend REST API first; falls back to the seeded mock data (localStorage)
// when the backend is not reachable. getData()/setData() keep the same
// synchronous API so every page works unchanged either way.

const DEFAULT_DATA = {
  patients: [
    { id: "P-0231", name: "Sipho Dlamini", gender: "M", age: 34, phone: "082 123 4567", dept: "General Medicine" },
    { id: "P-0232", name: "Nomsa Khumalo", gender: "F", age: 27, phone: "071 555 9081", dept: "Pediatrics" },
    { id: "P-0233", name: "Thabo Mokoena", gender: "M", age: 45, phone: "063 887 2210", dept: "Antenatal Care" },
    { id: "P-0234", name: "Lerato Molefe", gender: "F", age: 22, phone: "060 234 8890", dept: "HIV Care" },
    { id: "P-0235", name: "Jacob van Wyk", gender: "M", age: 58, phone: "082 776 1290", dept: "General Medicine" },
    { id: "P-0236", name: "Amahle Ndlovu", gender: "F", age: 31, phone: "071 903 4471", dept: "Pediatrics" },
  ],
  appointments: [
    { time: "09:00 AM", patient: "Sipho Dlamini", dept: "General Medicine", doctor: "Dr. N. Zulu", status: "Checked In" },
    { time: "10:30 AM", patient: "Nomsa Khumalo", dept: "Pediatrics", doctor: "Dr. L. Pillay", status: "Confirmed" },
    { time: "11:00 AM", patient: "Thabo Mokoena", dept: "Antenatal Care", doctor: "Dr. T. Mthembu", status: "Confirmed" },
    { time: "12:00 PM", patient: "Lerato Molefe", dept: "HIV Care", doctor: "Dr. R. Jacobs", status: "Pending" },
    { time: "01:00 PM", patient: "Jacob van Wyk", dept: "General Medicine", doctor: "Dr. N. Zulu", status: "Pending" },
  ],
  queue: [
    { no: "Q012", patient: "Sipho Dlamini", dept: "General Medicine", status: "In Consultation", wait: "15 min", id: "P-0231", age: 34, gender: "M", phone: "082 123 4567" },
    { no: "Q013", patient: "Nomsa Khumalo", dept: "Pediatrics", status: "Waiting", wait: "10 min", id: "P-0232", age: 27, gender: "F", phone: "071 555 9081" },
    { no: "Q014", patient: "Thabo Mokoena", dept: "Antenatal Care", status: "Waiting", wait: "8 min", id: "P-0233", age: 45, gender: "M", phone: "063 887 2210" },
    { no: "Q015", patient: "Lerato Molefe", dept: "HIV Care", status: "Waiting", wait: "5 min", id: "P-0234", age: 22, gender: "F", phone: "060 234 8890" },
    { no: "Q016", patient: "Jacob van Wyk", dept: "General Medicine", status: "Waiting", wait: "2 min", id: "P-0235", age: 58, gender: "M", phone: "082 776 1290" },
  ],
  visits: [
    { id: "V-1042", patient: "Sipho Dlamini", date: "01 Aug 2026", dept: "General Medicine", outcome: "Prescribed" },
    { id: "V-1041", patient: "Nomsa Khumalo", date: "01 Aug 2026", dept: "Pediatrics", outcome: "Referred" },
    { id: "V-1040", patient: "Thabo Mokoena", date: "31 Jul 2026", dept: "Antenatal Care", outcome: "Follow-up" },
    { id: "V-1039", patient: "Lerato Molefe", date: "31 Jul 2026", dept: "HIV Care", outcome: "Prescribed" },
  ],
  records: [
    { patient: "Sipho Dlamini", condition: "Hypertension", lastVisit: "01 Aug 2026", doctor: "Dr. N. Zulu" },
    { patient: "Nomsa Khumalo", condition: "Asthma", lastVisit: "01 Aug 2026", doctor: "Dr. L. Pillay" },
    { patient: "Thabo Mokoena", condition: "Routine antenatal", lastVisit: "31 Jul 2026", doctor: "Dr. T. Mthembu" },
  ],
  prescriptions: [
    { id: "RX-501", patient: "Sipho Dlamini", medication: "Amlodipine 5mg", dosage: "1 tab daily", prescribedBy: "Dr. N. Zulu", duration: "30 days", date: "01 Aug 2026", status: "Pending" },
    { id: "RX-502", patient: "Nomsa Khumalo", medication: "Salbutamol Inhaler", dosage: "2 puffs as needed", prescribedBy: "Dr. L. Pillay", duration: "N/A", date: "01 Aug 2026", status: "Pending" },
    { id: "RX-503", patient: "Thabo Mokoena", medication: "Folic Acid 5mg", dosage: "1 tab daily", prescribedBy: "Dr. T. Mthembu", duration: "60 days", date: "31 Jul 2026", status: "Dispensed" },
    { id: "RX-504", patient: "Lerato Molefe", medication: "Tenofovir/Emtricitabine", dosage: "1 tab daily", prescribedBy: "Dr. R. Jacobs", duration: "30 days", date: "31 Jul 2026", status: "Pending" },
    { id: "RX-505", patient: "Jacob van Wyk", medication: "Paracetamol 500mg", dosage: "2 tabs 3x daily", prescribedBy: "Dr. N. Zulu", duration: "5 days", date: "30 Jul 2026", status: "Dispensed" },
  ],
  users: [
    { name: "Thandi M.", role: "Receptionist", email: "thandi.m@mediqueue.co.za", status: "Active", clinic: "District Six Clinic", department: "General Medicine" },
    { name: "Dr. N. Zulu", role: "Doctor", email: "n.zulu@mediqueue.co.za", status: "Active", clinic: "District Six Clinic", department: "General Medicine" },
    { name: "Dr. L. Pillay", role: "Doctor", email: "l.pillay@mediqueue.co.za", status: "Active", clinic: "Bellville Clinic", department: "Pediatrics" },
    { name: "P. Adams", role: "Pharmacist", email: "p.adams@mediqueue.co.za", status: "Active", clinic: "Bellville Clinic", department: "General Medicine" },
  ],


 seenToday: 14,
  departments: [
    { name: "General Medicine", doctors: 4, today: 18, avgWait: "12 min" },
    { name: "Pediatrics", doctors: 2, today: 11, avgWait: "9 min" },
    { name: "Antenatal Care", doctors: 2, today: 7, avgWait: "14 min" },
    { name: "HIV Care", doctors: 3, today: 9, avgWait: "6 min" },
  ],
  clinics: [
    { id: "CL001", name: "District Six Clinic", code: "DS-01", location: "Cape Town", contact: "021 555 1001", email: "district6@mediqueue.co.za", hours: "08:00 - 17:00", status: "Active" },
    { id: "CL002", name: "Bellville Clinic", code: "BV-02", location: "Bellville", contact: "021 555 1002", email: "bellville@mediqueue.co.za", hours: "08:00 - 17:00", status: "Active" },
    { id: "CL003", name: "Khayelitsha Clinic", code: "KL-03", location: "Khayelitsha", contact: "021 555 1003", email: "khayelitsha@mediqueue.co.za", hours: "07:30 - 16:30", status: "Inactive" },
    { id: "CL004", name: "Mitchells Plain Clinic", code: "MP-04", location: "Mitchells Plain", contact: "021 555 1004", email: "mitchellsplain@mediqueue.co.za", hours: "08:00 - 17:00", status: "Active" },
  ],

  announcements: [
    { title: "Staff Meeting", body: "There will be a staff meeting on Friday at 10:00 AM in the boardroom." },
    { title: "System Update", body: "The system will be undergoing maintenance on Sunday from 00:00–02:00." },
  ],
profiles: {
    ADMIN: { name: "S. Ndaba", email: "s.ndaba@mediqueue.co.za", phone: "071 111 2222", clinic: "District Six Clinic", department: "General Medicine", joinDate: "Jan 2025" },
    RECEPTIONIST: { name: "Thandi M.", email: "thandi.m@mediqueue.co.za", phone: "071 234 5678", clinic: "District Six Clinic", department: "General Medicine", joinDate: "Mar 2025" },
    DOCTOR: { name: "Dr. N. Zulu", email: "n.zulu@mediqueue.co.za", phone: "082 345 6789", clinic: "District Six Clinic", department: "General Medicine", joinDate: "Aug 2023" },
    NURSE: { name: "Bongikazi M.", email: "b.mnyamana@mediqueue.co.za", phone: "072 111 2233", clinic: "Bellville Clinic", department: "Pediatrics", joinDate: "Feb 2024" },
    PHARMACIST: { name: "P. Adams", email: "p.adams@mediqueue.co.za", phone: "063 456 7890", clinic: "Bellville Clinic", department: "General Medicine", joinDate: "Jun 2024" },
  },
};

const DATA_KEY = "mq_data";
const API_BASE = "http://localhost:8080/MediQueue";

// Endpoint for each collection, keyed by the name used in the frontend data object.
const API_COLLECTIONS = {
  patients: "/patient/getAll",
  staff: "/staff/getAll",
  clinics: "/clinic/getAll",
  departments: "/department/getAll",
  appointments: "/appointment/getAll",
  queueEntries: "/mediqueue/queue-entry/all",
  visits: "/mediqueue/visit/all",
  medicalRecords: "/medical-record/getAll",
  prescriptions: "/prescription/getAll",
};

// Becomes true once the frontend has successfully talked to the backend.
// Implemented as a live property instead of a plain flag: whenever the
// connection state is still unknown (i.e. the backend was not reachable when
// the page loaded), reading it re-probes the backend on the spot and, on
// success, re-syncs the local cache from the database. This means actions
// taken after the backend comes up (registering, booking, queue updates) are
// written to MySQL instead of silently staying in the browser's localStorage.
let __mqBackendUp = false;
let __mqPollTimer = null;

// Synchronous connection attempt. Probes the backend once and, when it
// answers, rebuilds the whole cache from the database and flips the session
// into "connected" mode for the rest of the page's life. Returns true when
// connected (already connected counts as success).
function connectBackendSync() {
  if (__mqBackendUp) return true;
  if (syncGetJSON(API_COLLECTIONS.patients) === null) return false;
  const raw = fetchFromApi(syncGetJSON);
  if (!raw || !Object.keys(API_COLLECTIONS).every((k) => raw[k] !== null)) return false;
  __mqBackendUp = true;
  stopBackendPoller();
  localStorage.setItem(DATA_KEY, JSON.stringify(buildDataFromApi(raw)));
  hideOfflineNotice();
  console.log("[MediQueue] Connected to backend at " + API_BASE);
  return true;
}

// While the backend is down, poll quietly in the background. As soon as it
// becomes reachable the app adopts it (and reloads the cache from the
// database), so the user does not have to refresh the page for saves to start
// going to MySQL.
function startBackendPoller() {
  if (__mqPollTimer) return;
  __mqPollTimer = setInterval(async () => {
    if (__mqBackendUp) {
      stopBackendPoller();
      return;
    }
    if (await asyncGetJSON(API_COLLECTIONS.patients) !== null) {
      connectBackendSync();
    }
  }, 3000);
}

function stopBackendPoller() {
  if (__mqPollTimer) {
    clearInterval(__mqPollTimer);
    __mqPollTimer = null;
  }
}

Object.defineProperty(window, "__mqConnected", {
  configurable: true,
  get() {
    if (!__mqBackendUp) connectBackendSync();
    return __mqBackendUp;
  },
  set(value) {
    __mqBackendUp = Boolean(value);
  },
});


/* =========================================================
   LOW-LEVEL API HELPERS
========================================================= */

// Synchronous GET (used on the very first visit so the page can
// render real backend data before any page script runs).
function syncGetJSON(path) {
  try {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", API_BASE + path, false);
    xhr.send(null);
    if (xhr.status >= 200 && xhr.status < 300) {
      return JSON.parse(xhr.responseText);
    }
  } catch (e) {
    // backend down / not reachable
  }
  return null;
}

// Async GET with a short timeout (used to refresh the cache in the background).
function asyncGetJSON(path) {
  return new Promise((resolve) => {
    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), 4000);
    fetch(API_BASE + path, { signal: controller.signal })
      .then((r) => (r.ok ? r.json() : null))
      .then(resolve)
      .catch(() => resolve(null))
      .finally(() => clearTimeout(timer));
  });
}

function fetchFromApi(fetchJson) {
  const raw = {};
  Object.keys(API_COLLECTIONS).forEach((key) => {
    raw[key] = fetchJson(API_COLLECTIONS[key]);
  });
  return raw;
}


/* =========================================================
   FIELD FORMATTING / MAPPING HELPERS
========================================================= */

function fullName(user) {
  return user ? [user.firstName, user.lastName].filter(Boolean).join(" ") : "";
}

// Backend staff names have no titles (e.g. "N. Zulu"). Frontend pages show
// doctors as "Dr. N. Zulu", so add the prefix here so names line up between
// the two sides everywhere (lists, doctor-filtered tables, lookups).
function staffDisplayName(staff) {
  if (!staff) return "";
  const n = fullName(staff);
  if (/doctor/i.test(staff.position || "")) return "Dr. " + n;
  return n;
}

function deptOfStaff(staff) {
  return staff && staff.department ? staff.department.departmentName : "";
}

function ageFromDob(dob) {
  if (!dob) return "";
  const birth = new Date(dob);
  if (isNaN(birth)) return "";
  return Math.floor((Date.now() - birth.getTime()) / (365.25 * 24 * 3600 * 1000));
}

function fmtDate(iso) {
  if (!iso) return "";
  const parts = String(iso).split("-");
  if (parts.length !== 3) return iso;
  const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  const m = months[Number(parts[1]) - 1];
  return `${parts[2]} ${m} ${parts[0]}`;
}

function fmtTime12(t) {
  if (!t) return "";
  const [h, m] = String(t).split(":").map(Number);
  if (isNaN(h)) return t;
  const ampm = h >= 12 ? "PM" : "AM";
  const hh = ((h + 11) % 12) + 1;
  return `${String(hh).padStart(2, "0")}:${String(m).padStart(2, "0")} ${ampm}`;
}

function waitFromCheckIn(t) {
  if (!t) return "";
  const [h, m] = String(t).split(":").map(Number);
  if (isNaN(h)) return "";
  const then = new Date();
  then.setHours(h, m, 0, 0);
  const mins = Math.round((then - new Date()) / 60000);
  return mins < 0 ? "now" : `${mins} min`;
}

// Turns the raw backend JSON into the shape the frontend pages expect.
function buildDataFromApi(raw) {
  const patients = raw.patients || [];
  const appointments = raw.appointments || [];
  const staff = raw.staff || [];

  // Best-effort department per patient, taken from their latest appointment.
  const deptByPatient = {};
  appointments.forEach((a) => {
    const pid = a.patient && a.patient.userId;
    const d = deptOfStaff(a.doctor);
    if (pid && d) deptByPatient[pid] = d;
  });

  return {
    patients: patients.map((p) => mapPatient(p, deptByPatient)),
    appointments: appointments.map(mapAppointment),
    queue: (raw.queueEntries || []).map(mapQueueEntry),
    visits: (raw.visits || []).map(mapVisit),
    records: (raw.medicalRecords || []).map(mapRecord),
    prescriptions: (raw.prescriptions || []).map(mapPrescription),
    users: staff.map(mapUser),
    clinics: (raw.clinics || []).map(mapClinic),
    departments: (raw.departments || []).map((d) => mapDepartment(d, staff)),
    announcements: DEFAULT_DATA.announcements,
    profiles: DEFAULT_DATA.profiles,
    seenToday: patients.length,
  };
}

// Per-collection mappers: backend domain JSON -> frontend display shape.
// Kept as named functions so newly created entities can be mapped into the
// in-memory cache right after a successful write.
function mapPatient(p, deptByPatient) {
  return {
    id: p.userId,
    name: fullName(p),
    gender: p.gender || "",
    age: ageFromDob(p.dateOfBirth),
    phone: p.phoneNumber || "",
    dept: (deptByPatient && deptByPatient[p.userId]) || "",
  };
}

function mapAppointment(a) {
  return {
    id: a.appointmentId,
    time: fmtTime12(a.scheduledTime),
    date: a.scheduledDate,
    patient: fullName(a.patient),
    patientId: a.patient && a.patient.userId,
    dept: (a.department && a.department.departmentName) || deptOfStaff(a.doctor),
    departmentId: a.department && a.department.departmentId,
    clinic: a.clinic && a.clinic.clinicName,
    clinicId: a.clinic && a.clinic.clinicId,
    doctor: a.doctor ? staffDisplayName(a.doctor) : "",
    doctorId: a.doctor && a.doctor.userId,
    status: a.status || "Pending",
    reason: a.reason || "",
  };
}

function mapQueueEntry(qe) {
  const pat = qe.patient || {};
  return {
    no: "Q" + String(qe.queueNumber).padStart(3, "0"),
    patient: fullName(pat),
    dept: deptOfStaff(qe.doctor),
    status: qe.status || "Waiting",
    wait: waitFromCheckIn(qe.checkInTime),
    id: pat.userId || "",
    age: ageFromDob(pat.dateOfBirth),
    gender: pat.gender || "",
    phone: pat.phoneNumber || "",
    entryId: qe.queueEntryId,
  };
}

function mapVisit(v) {
  return {
    id: v.visitId,
    patient: fullName(v.patient),
    date: fmtDate(v.visitDate),
    dept: (v.appointment && deptOfStaff(v.appointment.doctor)) || "",
    outcome: v.status || "Completed",
  };
}

function mapRecord(r) {
  return {
    patient: fullName(r.patient),
    condition: r.diagnosis || r.notes || "",
    lastVisit: fmtDate(r.recordDate),
    doctor: staffDisplayName(r.createdBy),
    date: r.recordDate,
    visitDate: r.recordDate,
    createdDate: r.recordDate,
    recordId: r.recordId,
    patientId: r.patient && r.patient.userId,
  };
}

function mapPrescription(pres) {
  return {
    id: pres.prescriptionId,
    patient: fullName((pres.medicalRecord || {}).patient),
    medication: pres.medicationName,
    dosage: pres.dosage || "",
    prescribedBy: staffDisplayName((pres.medicalRecord || {}).createdBy),
    duration: pres.instructions || "",
    date: fmtDate(pres.prescriptionDate),
    status: "",
    recordId: (pres.medicalRecord || {}).recordId,
    patientId: ((pres.medicalRecord || {}).patient || {}).userId,
  };
}

function mapUser(s) {
  return {
    id: s.userId,
    name: staffDisplayName(s),
    role: s.position || (s.role && s.role.roleName) || "",
    email: s.email || "",
    status: s.status ? "Active" : "Inactive",
    clinic: "",
    department: deptOfStaff(s),
  };
}

function mapClinic(c) {
  return {
    id: c.clinicId,
    name: c.clinicName,
    code: c.clinicId,
    location: c.location || "",
    contact: c.contactNumber || "",
    email: "",
    hours: "",
    status: "Active",
  };
}

function mapDepartment(d, staff) {
  staff = staff || [];
  return {
    id: d.departmentId,
    name: d.departmentName,
    doctors: staff.filter(
      (s) => deptOfStaff(s) === d.departmentName && /doctor/i.test(s.position || "")
    ).length,
    today: 0,
    avgWait: "",
  };
}


/* =========================================================
   WRITE LAYER (frontend -> backend)
   The backend entity field names differ slightly from the
   frontend display names, so payloads are built here once and
   every page uses the same helpers.
========================================================= */

// Generic async request. Resolves { ok, status, data }. When a session
// token exists it is sent as "Authorization: Bearer <token>" so the
// backend can identify the logged-in user on protected endpoints.
async function apiRequest(method, path, body) {
  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), 6000);
  const headers = {};
  if (body) headers["Content-Type"] = "application/json";
  const token = getToken();
  if (token) headers["Authorization"] = "Bearer " + token;
  try {
    const res = await fetch(API_BASE + path, {
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined,
      signal: controller.signal,
    });
    let data = null;
    try { data = await res.json(); } catch (e) { /* empty body */ }
    return { ok: res.ok, status: res.status, data };
  } catch (e) {
    return { ok: false, status: 0, data: null };
  } finally {
    clearTimeout(timer);
  }
}

/* =========================================================
   SESSION + AUTH HELPERS
   The logged-in session is stored in localStorage so protected
   pages can send the bearer token (guard.js) and pages can
   redirect based on the authenticated role. No fake logins:
   a token is only saved after /api/auth/login|register returned
   a real, validated session from the backend.
========================================================= */

const SESSION_KEYS = [
  "mq_token", "mq_userId", "mq_role",
  "mq_patientId", "mq_staffId", "mq_name"
];

function getToken() {
  return localStorage.getItem("mq_token");
}

function saveSession(res) {
  if (!res || !res.token) return false;
  clearSession();
  localStorage.setItem("mq_token", res.token);
  localStorage.setItem("mq_userId", res.userId || "");
  localStorage.setItem("mq_role", res.role || "");
  if (res.patientId) localStorage.setItem("mq_patientId", res.patientId);
  if (res.staffId) localStorage.setItem("mq_staffId", res.staffId);
  localStorage.setItem("mq_name", fullName(res));
  return true;
}

function clearSession() {
  SESSION_KEYS.forEach((k) => localStorage.removeItem(k));
}

function homeForRole(role) {
  return ({
    PATIENT: "patient-dashboard.html",
    ADMIN: "admin-dashboard.html",
    RECEPTIONIST: "receptionist-dashboard.html",
    DOCTOR: "doctor-dashboard.html",
    NURSE: "nurse-dashboard.html",
    PHARMACIST: "pharmacist-dashboard.html"
  })[role] || "index.html";
}

// Extracts the human-readable error from an apiRequest result.
function authError(r) {
  return (r && r.data && r.data.error) || "Something went wrong. Please try again.";
}

// Registers a new patient (POST /api/auth/register). On success the
// response already contains a session token, so the caller should
// saveSession() to log the patient straight in.
async function registerPatientAsync(reg) {
  return apiRequest("POST", "/api/auth/register", {
    firstName: reg.firstName,
    lastName: reg.lastName,
    email: reg.email,
    password: reg.password,
    phoneNumber: reg.phoneNumber,
    idNumber: reg.idNumber,
    dateOfBirth: reg.dateOfBirth,
    gender: reg.gender,
    address: reg.address,
    allergies: reg.allergies || ""
  });
}

// Authenticates any user (patient/staff) by email + password.
async function loginAsync(login) {
  return apiRequest("POST", "/api/auth/login", {
    email: login.email,
    password: login.password
  });
}

// Invalidates the current session token on the backend.
async function logoutAsync() {
  return apiRequest("POST", "/api/auth/logout");
}

// Resolves the profile for the current token (POST-less /api/auth/me).
async function meAsync() {
  return apiRequest("GET", "/api/auth/me");
}

// Cached list of roles from the backend, used to reference roles in payloads.
async function ensureRoles() {
  if (Array.isArray(window.__mqRoles)) return true;
  const r = await apiRequest("GET", "/api/roles");
  if (r.ok && Array.isArray(r.data)) {
    window.__mqRoles = r.data;
    return true;
  }
  return false;
}

function roleRef(roleName) {
  const r = (window.__mqRoles || []).find(
    (x) => String(x.roleName || "").toUpperCase() === String(roleName || "").toUpperCase()
  );
  return r ? { roleId: r.roleId, roleName: r.roleName } : null;
}

function nowIsoLocal() {
  const d = new Date();
  const p = (n) => String(n).padStart(2, "0");
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`;
}

function todayIso() {
  return nowIsoLocal().split("T")[0];
}

function uniqueId(prefix) {
  return `${prefix}-${new Date().getTime()}`;
}

function splitName(full) {
  const parts = String(full || "").trim().split(/\s+/);
  const first = parts.shift() || "";
  return [first, parts.join(" ")];
}

function dateFromAge(age) {
  const years = Number(age);
  if (!years) return "2000-01-01";
  const d = new Date();
  d.setFullYear(d.getFullYear() - years);
  const p = (n) => String(n).padStart(2, "0");
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`;
}

function saIdFromDob(dob) {
  const digits = String(dob || "").replace(/\D/g, "");
  const base = (digits + "00000000000").slice(0, 11);
  return base + String(Math.floor(Math.random() * 90) + 10);
}

function to24h(t) {
  const m = String(t || "").match(/^(\d{1,2}):(\d{2})(?:\s*(AM|PM))?$/i);
  if (!m) return t;
  let h = Number(m[1]);
  if (/PM/i.test(m[3]) && h < 12) h += 12;
  if (/AM/i.test(m[3]) && h === 12) h = 0;
  return `${String(h).padStart(2, "0")}:${m[2]}`;
}

function nextQueueNumber(data) {
  let max = 0;
  (data.queue || []).forEach((q) => {
    const n = parseInt(q.no, 10);
    if (!isNaN(n) && n > max) max = n;
  });
  return max + 1;
}

// Matches a user against a display label like "Dr. N. Zulu".
function userMatchesLabel(user, label) {
  const a = String((user && user.name) || "").toLowerCase().replace(/\s+/g, " ");
  const b = String(label || "").toLowerCase().replace(/\s+/g, " ").replace(/^(dr\.|dr)\s+/, "");
  return a === b;
}

// ---- Payload builders (backend entity field names) ----

function buildPatientPayload(f) {
  const [firstName, lastName] = splitName(f.name);
  const dob = f.dateOfBirth || dateFromAge(f.age);
  const role = roleRef("PATIENT") || { roleId: 1, roleName: "PATIENT" };
  return {
    type: "patient",
    userId: f.patientId,
    firstName,
    lastName,
    email: f.email || (String(f.patientId || "p").toLowerCase() + "@mediqueue.co.za"),
    password: f.password || "pass123",
    phoneNumber: String(f.phone == null ? "" : f.phone).replace(/\s+/g, ""),
    status: true,
    createdAt: nowIsoLocal(),
    role,
    idNumber: f.idNumber || saIdFromDob(dob),
    dateOfBirth: dob,
    gender: f.gender || "M",
    address: f.address || "",
    allergies: f.allergies || "",
  };
}

function buildAppointmentPayload(f) {
  return {
    appointmentId: f.appointmentId,
    patient: { type: "patient", userId: f.patientId },
    doctor: f.doctorId ? { type: "staff", userId: f.doctorId } : null,
    clinic: f.clinicId ? { clinicId: f.clinicId } : null,
    department: f.departmentId ? { departmentId: f.departmentId } : null,
    reason: f.reason || "",
    scheduledDate: f.scheduledDate,
    scheduledTime: f.scheduledTime,
    appointmentType: f.appointmentType || "booked",
    status: f.status || "Pending",
    createdBy: null,
  };
}

function buildClinicPayload(f) {
  return {
    clinicId: f.clinicId,
    clinicName: f.clinicName,
    location: f.location || "",
    contactNumber: f.contactNumber || "",
  };
}

function buildDepartmentPayload(f) {
  return {
    departmentId: f.departmentId,
    departmentName: f.departmentName,
    description: f.description || "",
  };
}

function buildMedicalRecordPayload(f) {
  return {
    recordId: f.recordId,
    patient: { type: "patient", userId: f.patientId },
    createdBy: { type: "staff", userId: f.doctorId },
    diagnosis: f.diagnosis || "",
    notes: f.notes || "",
    recordDate: f.recordDate,
  };
}

function buildPrescriptionPayload(f) {
  return {
    prescriptionId: f.prescriptionId,
    medicalRecord: { recordId: f.recordId },
    medicationName: f.medicationName,
    dosage: f.dosage || "",
    instructions: f.instructions || "",
    prescriptionDate: f.prescriptionDate,
  };
}

function buildVisitPayload(f) {
  return {
    visitId: f.visitId,
    patient: { type: "patient", userId: f.patientId },
    appointment: null,
    visitDate: f.visitDate,
    checkInTime: f.checkInTime || null,
    checkOutTime: f.checkOutTime || null,
    status: f.status || "In Progress",
  };
}

function buildVitalSignsPayload(f) {
  return {
    vitalId: f.vitalId,
    visit: { visitId: f.visitId },
    temperature: f.temperature == null ? null : String(f.temperature),
    bloodPressure: f.bloodPressure || null,
    heartRate: f.heartRate == null ? null : String(f.heartRate),
    weight: f.weight == null ? null : String(f.weight),
    recordedBy: null,
    recordedAt: null,
  };
}

function buildQueuePayload(f) {
  return {
    queueId: f.queueId,
    clinic: { clinicId: f.clinicId },
    date: f.date,
    maxCapacity: f.maxCapacity || 50,
  };
}

function buildQueueEntryPayload(f) {
  return {
    queueEntryId: f.queueEntryId,
    queue: { queueId: f.queueId },
    patient: { type: "patient", userId: f.patientId },
    doctor: { type: "staff", userId: f.doctorId },
    visit: null,
    queueNumber: f.queueNumber,
    priorityLevel: f.priorityLevel || "Normal",
    status: f.status || "Waiting",
    checkInTime: f.checkInTime || null,
  };
}

// ---- Orchestration helpers: write + update the local cache ----

async function createPatientAsync(input) {
  await ensureRoles();
  const payload = buildPatientPayload(input);
  const r = await apiRequest("POST", "/patient/create", payload);
  if (!r.ok) return { ok: false };
  const data = getData();
  const deptMap = {};
  deptMap[payload.userId] = input.dept || "";
  data.patients = [mapPatient(r.data || payload, deptMap), ...data.patients];
  setData(data);
  return { ok: true, created: r.data };
}

async function createAppointmentAsync(input) {
  const r = await apiRequest("POST", "/appointment/create", buildAppointmentPayload(input));
  if (!r.ok) return { ok: false, status: r.status, data: r.data };
  const data = getData();
  data.appointments = [mapAppointment(r.data || {}), ...data.appointments];
  setData(data);
  return { ok: true, created: r.data };
}

// Assigns a doctor to an appointment; the backend also sets its status
// to "Confirmed". Updates the local cache so lists re-render instantly.
async function assignAppointmentDoctorAsync(appointmentId, doctorId) {
  const r = await apiRequest("PUT", "/appointment/assign/" + encodeURIComponent(appointmentId) + "/" + encodeURIComponent(doctorId));
  if (!r.ok || !r.data) return { ok: false, status: r.status, data: r.data };
  const data = getData();
  const item = (data.appointments || []).find((a) => a.id === appointmentId);
  if (item) {
    const doctor = (data.users || []).find((u) => u.id === doctorId);
    item.doctor = doctor ? doctor.name : "Dr. Assigned";
    item.doctorId = doctorId;
    item.status = "Confirmed";
  }
  setData(data);
  return { ok: true };
}

// Updates an appointment's status via the backend.
async function updateAppointmentStatusAsync(appointmentId, status) {
  const r = await apiRequest("PUT", "/appointment/status/" + encodeURIComponent(appointmentId) + "/" + encodeURIComponent(status));
  if (!r.ok || !r.data) return { ok: false, status: r.status, data: r.data };
  const data = getData();
  const item = (data.appointments || []).find((a) => a.id === appointmentId);
  if (item) item.status = status;
  setData(data);
  return { ok: true };
}

// Deletes an appointment via the backend and drops it from the cache.
async function deleteAppointmentAsync(appointmentId) {
  const r = await apiRequest("DELETE", "/appointment/delete/" + encodeURIComponent(appointmentId));
  if (!r.ok) return { ok: false, status: r.status, data: r.data };
  const data = getData();
  data.appointments = (data.appointments || []).filter((a) => a.id !== appointmentId);
  setData(data);
  return { ok: true };
}

async function createMedicalRecordAsync(input) {
  const r = await apiRequest("POST", "/medical-record/create", buildMedicalRecordPayload(input));
  if (!r.ok) return { ok: false };
  const data = getData();
  data.records = [mapRecord(r.data || {}), ...data.records];
  setData(data);
  return { ok: true, created: r.data };
}

async function createPrescriptionAsync(input) {
  const r = await apiRequest("POST", "/prescription/create", buildPrescriptionPayload(input));
  if (!r.ok) return { ok: false };
  const data = getData();
  data.prescriptions = [mapPrescription(r.data || {}), ...data.prescriptions];
  setData(data);
  return { ok: true, created: r.data };
}

async function createClinicAsync(input) {
  const r = await apiRequest("POST", "/clinic/create", buildClinicPayload(input));
  if (!r.ok) return { ok: false };
  const data = getData();
  data.clinics = [mapClinic(r.data || {}), ...data.clinics];
  setData(data);
  return { ok: true, created: r.data };
}

async function createDepartmentAsync(input) {
  const r = await apiRequest("POST", "/department/create", buildDepartmentPayload(input));
  if (!r.ok) return { ok: false };
  const data = getData();
  const deptDisplay = { name: input.departmentName, doctors: 0, today: 0, avgWait: "" };
  data.departments = [deptDisplay, ...data.departments];
  setData(data);
  return { ok: true, created: r.data };
}

// Adds a patient to the queue by creating a QueueEntry on the backend.
async function createQueueEntryAsync(input) {
  let queueId = input.queueId;
  if (!queueId) {
    const qr = await apiRequest("GET", "/mediqueue/queue/all");
    if (qr.ok && qr.data && qr.data.length) queueId = qr.data[0].queueId;
    else return { ok: false };
  }
  const data = getData();
  const queueNumber = input.queueNumber || nextQueueNumber(data);
  const payload = buildQueueEntryPayload({
    queueEntryId: uniqueId("QE"),
    queueId,
    patientId: input.patientId,
    doctorId: input.doctorId,
    queueNumber,
    priorityLevel: "Normal",
    status: input.status || "Waiting",
    checkInTime: input.checkInTime || null,
  });
  const r = await apiRequest("POST", "/mediqueue/queue-entry/create", payload);
  if (!r.ok) return { ok: false };
  const d2 = getData();
  d2.queue = [...d2.queue, mapQueueEntry(r.data || {})];
  setData(d2);
  return { ok: true, created: r.data };
}

// Fetches the full QueueEntry, changes its status, saves it back.
async function setQueueEntryStatusAsync(entryId, status) {
  const r = await apiRequest("GET", "/mediqueue/queue-entry/read/" + encodeURIComponent(entryId));
  if (!r.ok || !r.data) return { ok: false };
  r.data.status = status;
  const u = await apiRequest("PUT", "/mediqueue/queue-entry/update", r.data);
  if (!u.ok) return { ok: false };
  const data = getData();
  const item = (data.queue || []).find((q) => q.entryId === entryId);
  if (item) item.status = status;
  setData(data);
  return { ok: true };
}

async function deleteQueueEntryAsync(entryId) {
  const r = await apiRequest("DELETE", "/mediqueue/queue-entry/delete/" + encodeURIComponent(entryId));
  if (!r.ok) return { ok: false };
  const data = getData();
  data.queue = (data.queue || []).filter((q) => q.entryId !== entryId);
  setData(data);
  return { ok: true };
}

// Deletes a patient and everything that references them (appointments,
// prescriptions, medical records, queue entries, visits + vital signs), in
// the correct order so the backend's foreign keys never block the delete.
async function deletePatientAsync(patientId) {
  try {
    // queue entries (a queue entry can't be looked up again once gone)
    const qes = await apiRequest("GET", "/mediqueue/queue-entry/all");
    if (qes.ok && Array.isArray(qes.data)) {
      for (const q of qes.data) {
        if (q.patient && q.patient.userId === patientId) {
          await deleteQueueEntryAsync(q.queueEntryId);
        }
      }
    }
    // appointments
    const appts = await apiRequest("GET", "/appointment/getAll");
    if (appts.ok && Array.isArray(appts.data)) {
      for (const a of appts.data) {
        if (a.patient && a.patient.userId === patientId) {
          await apiRequest("DELETE", "/appointment/delete/" + encodeURIComponent(a.appointmentId));
        }
      }
    }
    // prescriptions of this patient's medical records, then the records
    const recs = await apiRequest("GET", "/medical-record/getAll");
    const recordIds = new Set();
    if (recs.ok && Array.isArray(recs.data)) {
      for (const r of recs.data) {
        if (r.patient && r.patient.userId === patientId) recordIds.add(r.recordId);
      }
    }
    const rxs = await apiRequest("GET", "/prescription/getAll");
    if (rxs.ok && Array.isArray(rxs.data)) {
      for (const px of rxs.data) {
        if (px.medicalRecord && recordIds.has(px.medicalRecord.recordId)) {
          await apiRequest("DELETE", "/prescription/delete/" + encodeURIComponent(px.prescriptionId));
        }
      }
    }
    for (const rid of recordIds) {
      await apiRequest("DELETE", "/medical-record/delete/" + encodeURIComponent(rid));
    }
    // visits (with their vital signs) belonging to the patient
    const vs = await apiRequest("GET", "/mediqueue/visit/all");
    const visitIds = new Set();
    if (vs.ok && Array.isArray(vs.data)) {
      for (const v of vs.data) {
        if (v.patient && v.patient.userId === patientId) visitIds.add(v.visitId);
      }
    }
    const vitals = await apiRequest("GET", "/mediqueue/vital-signs/all");
    if (vitals.ok && Array.isArray(vitals.data)) {
      for (const vt of vitals.data) {
        if (vt.visit && visitIds.has(vt.visit.visitId)) {
          await apiRequest("DELETE", "/mediqueue/vital-signs/delete/" + encodeURIComponent(vt.vitalId));
        }
      }
    }
    for (const vid of visitIds) {
      await apiRequest("DELETE", "/mediqueue/visit/delete/" + encodeURIComponent(vid));
    }
    // finally the patient itself
    const r = await apiRequest("DELETE", "/patient/delete/" + encodeURIComponent(patientId));
    if (!r.ok) return { ok: false };
    const d = getData();
    const gone = (d.patients || []).find((p) => p.id === patientId);
    const goneName = (gone && gone.name) || "";
    d.patients = (d.patients || []).filter((p) => p.id !== patientId);
    d.appointments = (d.appointments || []).filter(
      (a) => !goneName || a.patient !== goneName
    );
    d.records = (d.records || []).filter((rc) => rc.patientId !== patientId);
    d.queue = (d.queue || []).filter((q) => q.id !== patientId);
    d.visits = (d.visits || []).filter((v) => !goneName || v.patient !== goneName);
    setData(d);
    return { ok: true };
  } catch (e) {
    return { ok: false };
  }
}

// Creates a Visit (if needed) then the VitalSigns record for an assessment.
async function createVitalSignsAsync(input) {
  let visitId = input.visitId;
  if (!visitId) {
    const v = await apiRequest(
      "POST",
      "/mediqueue/visit/create",
      buildVisitPayload({
        visitId: uniqueId("V"),
        patientId: input.patientId,
        visitDate: todayIso(),
        status: "In Progress",
      })
    );
    if (!v.ok) return { ok: false };
    visitId = (v.data || {}).visitId;
  }
  const r = await apiRequest(
    "POST",
    "/mediqueue/vital-signs/create",
    buildVitalSignsPayload({
      vitalId: uniqueId("VIT"),
      visitId,
      temperature: input.temperature,
      bloodPressure: input.bloodPressure,
      heartRate: input.heartRate,
      weight: input.weight,
    })
  );
  if (!r.ok) return { ok: false };
  return { ok: true, visitId };
}


/* =========================================================
   BOOTSTRAP
========================================================= */

// Pull fresh data from the backend in the background and cache it
// (only replaces local data if every endpoint answered).
async function refreshFromApiAsync() {
  const keys = Object.keys(API_COLLECTIONS);
  const results = await Promise.all(keys.map((k) => asyncGetJSON(API_COLLECTIONS[k])));
  if (!results.every((r) => r !== null)) return; // backend not fully reachable
  window.__mqConnected = true;
  hideOfflineNotice();
  const raw = {};
  keys.forEach((k, i) => (raw[k] = results[i]));
  setData(buildDataFromApi(raw));
  console.log("[MediQueue] Connected to backend at " + API_BASE);
}

// Writes sample data into localStorage the first time the site is opened.
// If the backend answers, the real data is used instead of the mock. The
// backend is probed again on every page load (and in the background while it
// is down), so registered patients, bookings and queue changes made earlier
// show up on the next page load instead of stale cached data.
function initData() {
  if (localStorage.getItem(DATA_KEY)) {
    connectBackendSync();
    return;
  }

  if (connectBackendSync()) {
    // Connected: the cache was already built from the database above.
    console.log("[MediQueue] Synced data from backend at " + API_BASE);
  } else {
    localStorage.setItem(DATA_KEY, JSON.stringify(DEFAULT_DATA));
  }
}

// Small, dismissable banner telling users when nothing is being saved to the
// database because the Spring Boot backend is not reachable.
function showOfflineNotice() {
  if (typeof document === "undefined" || !document.body || window.__mqConnected) return;
  if (document.getElementById("mqOfflineBar")) return;
  const bar = document.createElement("div");
  bar.id = "mqOfflineBar";
  bar.style.cssText =
    "position:fixed;top:0;left:0;right:0;z-index:99999;background:#b3261e;color:#fff;" +
    "padding:8px 40px 8px 14px;font-size:12.5px;text-align:center;";
  bar.textContent =
    "Offline: the backend is not reachable, so changes are saved in your browser only " +
    "(nothing is written to the database). Start Spring Boot with mvnw.cmd spring-boot:run, then reload this page.";
  const close = document.createElement("button");
  close.type = "button";
  close.textContent = "×";
  close.style.cssText = "position:absolute;right:8px;top:3px;background:none;border:none;color:#fff;font-size:18px;cursor:pointer;";
  close.addEventListener("click", () => bar.remove());
  bar.appendChild(close);
  document.body.prepend(bar);
}

function hideOfflineNotice() {
  if (typeof document === "undefined") return;
  const bar = document.getElementById("mqOfflineBar");
  if (bar) bar.remove();
}

function getData() { return JSON.parse(localStorage.getItem(DATA_KEY)); }
function setData(data) { localStorage.setItem(DATA_KEY, JSON.stringify(data)); }
function resetData() { localStorage.setItem(DATA_KEY, JSON.stringify(DEFAULT_DATA)); }

initData();
const DEPARTMENTS = getData().departments;

// Keep polling while the backend is down so the app adopts it the moment it
// comes up, without requiring a page refresh.
startBackendPoller();

// Show the offline banner as soon as the page body is available.
if (typeof document !== "undefined" && document.addEventListener) {
  document.addEventListener("DOMContentLoaded", showOfflineNotice);
}