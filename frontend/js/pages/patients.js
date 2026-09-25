// js/pages/patients.js

function renderPatients() {
  const data = getData();
  const body = document.getElementById("patientsBody");
  document.getElementById("patientsBodyCount").textContent = `${data.patients.length} records`;
  body.innerHTML = data.patients.map(p => `
    <tr>
      <td>${p.id}</td><td>${esc(p.name)}</td><td>${p.gender}</td><td>${p.age}</td><td>${p.phone}</td><td>${p.dept}</td>
      <td class="action-cell">
        <button class="btn btn-ghost btn-sm" onclick="openEditPatient('${p.id}')">Edit</button>
        <button class="btn btn-danger btn-sm" onclick="deletePatient('${p.id}')">Delete</button>
      </td>
    </tr>
  `).join("");
}

function populateDeptDropdowns() {
  document.querySelectorAll("#npDept, #epDept").forEach(sel => {
    if (sel) sel.innerHTML = DEPARTMENTS.map(d => `<option>${d.name}</option>`).join("");
  });
}

function openEditPatient(id) {
  const data = getData();
  const p = data.patients.find(x => x.id === id);
  if (!p) return;
  document.getElementById("epId").value = p.id;
  document.getElementById("epName").value = p.name;
  document.getElementById("epGender").value = p.gender;
  document.getElementById("epAge").value = p.age;
  document.getElementById("epPhone").value = p.phone;
  document.getElementById("epDept").value = p.dept;
  openModal("editPatientModal");
}

async function deletePatient(id) {
  const data = getData();
  const p = data.patients.find(x => x.id === id);
  if (!p) return;
  if (!confirm(`Remove ${p.name} from patients? This can't be undone.`)) return;
  if (window.__mqConnected) {
    const r = await deletePatientAsync(id);
    if (!r.ok) {
      alert("Could not delete the patient from the server.");
      return;
    }
    renderPatients();
    return;
  }
  data.patients = data.patients.filter(x => x.id !== id);
  setData(data);
  renderPatients();
}

document.addEventListener("DOMContentLoaded", () => {
  renderPatients();
  populateDeptDropdowns();

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
        renderPatients();
        return;
      }
      alert("Could not save the patient to the server – saved locally only.");
    }
    data.patients.unshift(entry);
    setData(data);
    document.getElementById("addPatientForm").reset();
    closeModal("addPatientModal");
    renderPatients();
  });

  document.getElementById("editPatientForm").addEventListener("submit", (e) => {
    e.preventDefault();
    const data = getData();
    const id = document.getElementById("epId").value;
    const idx = data.patients.findIndex(x => x.id === id);
    if (idx === -1) return;
    data.patients[idx] = {
      ...data.patients[idx],
      name: document.getElementById("epName").value.trim(),
      gender: document.getElementById("epGender").value,
      age: Number(document.getElementById("epAge").value) || 0,
      phone: document.getElementById("epPhone").value,
      dept: document.getElementById("epDept").value,
    };
    setData(data);
    closeModal("editPatientModal");
    renderPatients();
  });
});