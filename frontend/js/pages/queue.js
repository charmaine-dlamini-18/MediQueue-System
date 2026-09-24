// js/pages/queue.js
// Editing here means moving a patient through the real queue statuses,
// not a generic form — that's closer to how a receptionist would
// actually manage a live queue.

function renderQueue() {
  const data = getData();
  const body = document.getElementById("queueBody");
  document.getElementById("queueBodyCount").textContent = `${data.queue.length} records`;
  body.innerHTML = data.queue.map((q, i) => `
    <tr>
      <td>${q.no}</td><td>${esc(q.patient)}</td><td>${q.dept}</td>
      <td>
        <select class="input" style="padding:5px 8px;font-size:12.5px;width:auto;" onchange="updateQueueStatus(${i}, this.value)">
          <option value="Waiting" ${q.status === "Waiting" ? "selected" : ""}>Waiting</option>
          <option value="In Consultation" ${q.status === "In Consultation" ? "selected" : ""}>In Consultation</option>
          <option value="Completed" ${q.status === "Completed" ? "selected" : ""}>Completed</option>
        </select>
      </td>
      <td>${q.wait}</td>
      <td class="action-cell">
        <button class="btn btn-danger btn-sm" onclick="removeFromQueue(${i})">Remove</button>
      </td>
    </tr>
  `).join("");
}

async function updateQueueStatus(index, newStatus) {
  const data0 = getData();
  if (!data0.queue[index]) return;
  const entry = data0.queue[index];

  // Backend: persist on the real queue entry when it exists.
  if (window.__mqConnected && entry.entryId) {
    // Only one person can be "In Consultation" at a time.
    if (newStatus === "In Consultation") {
      for (const q of data0.queue) {
        if (q.entryId && q.entryId !== entry.entryId && q.status === "In Consultation") {
          await setQueueEntryStatusAsync(q.entryId, "Waiting");
        }
      }
    }
    const r = await setQueueEntryStatusAsync(entry.entryId, newStatus);
    if (r.ok) { renderQueue(); return; }
  }

  const data = getData();
  const i = Math.min(index, data.queue.length - 1);
  if (newStatus === "In Consultation") {
    data.queue = data.queue.map((q, j) => i === j ? { ...q, status: newStatus } : (q.status === "In Consultation" ? { ...q, status: "Waiting" } : q));
  } else {
    data.queue[i] = { ...data.queue[i], status: newStatus };
  }
  setData(data);
  renderQueue();
}

async function removeFromQueue(index) {
  const data0 = getData();
  const q = data0.queue[index];
  if (!q) return;
  if (!confirm(`Remove ${q.patient} (${q.no}) from the queue? This can't be undone.`)) return;

  if (window.__mqConnected && q.entryId) {
    const r = await deleteQueueEntryAsync(q.entryId);
    if (r.ok) { renderQueue(); return; }
    alert("Could not remove the patient from the server queue.");
  }

  const data = getData();
  data.queue.splice(Math.min(index, data.queue.length - 1), 1);
  setData(data);
  renderQueue();
}

document.addEventListener("DOMContentLoaded", renderQueue);