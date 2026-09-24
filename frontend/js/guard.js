// js/guard.js — loaded on every authenticated page (data.js is loaded
// before it). Requires a real session token: if nobody is logged in the
// user is sent back to the entry page. Wires up Logout (which also
// invalidates the token on the backend), applies the session profile
// name, and keeps the notification bell + profile dropdown working.
//
// Pages that restrict access to specific roles call requireRole(...).

const ROLE_KEY = "mq_role";

// Ends the session: tells the backend the token is dead, then clears the
// local session and returns to the entry page.
function mqLogout() {
  const token = typeof getToken === "function" ? getToken() : null;
  const done = () => {
    clearSession();
    window.location.href = "index.html";
  };
  if (token && typeof logoutAsync === "function") {
    logoutAsync().finally(done);
  } else {
    done();
  }
}

(function guard() {
  if (typeof getToken !== "function" || !getToken()) {
    window.location.href = "index.html";
    return;
  }
})();

// Restricts a page to one or more roles. Redirects to the role's home or
// the entry page when the visitor is not allowed. Returns true when the
// visitor may stay.
function requireRole(...allowed) {
  const role = localStorage.getItem(ROLE_KEY);
  if (!getToken()) {
    window.location.href = "index.html";
    return false;
  }
  if (allowed.length && !allowed.includes(role)) {
    window.location.href = homeForRole(role);
    return false;
  }
  return true;
}

document.addEventListener("DOMContentLoaded", () => {
  const logoutLink = document.getElementById("logoutLink");
  if (logoutLink) {
    logoutLink.addEventListener("click", (e) => {
      e.preventDefault();
      mqLogout();
    });
  }

  applyProfileName();
  setupNotifications();
  setupProfileMenu();

  document.addEventListener("click", () => {
    document.querySelectorAll(".dropdown-panel").forEach((d) => (d.style.display = "none"));
  });
});

function applyProfileName() {
  const name = localStorage.getItem("mq_name");
  const nameEl = document.querySelector(".user-name");
  if (name && nameEl) nameEl.textContent = name;
}

function closeOtherDropdowns(except) {
  document.querySelectorAll(".dropdown-panel").forEach((d) => {
    if (d !== except) d.style.display = "none";
  });
}

function setupNotifications() {
  const bellWrap = document.querySelector(".bell-wrap");
  if (!bellWrap) return;

  const data = typeof getData === "function" ? getData() : null;
  const waiting = data ? data.queue.filter((q) => q.status === "Waiting").length : 0;
  const latestPatient = data && data.patients.length ? data.patients[0].name : null;

  const items = [];
  if (waiting > 0) items.push(`${waiting} patient${waiting === 1 ? "" : "s"} currently waiting in queue`);
  if (latestPatient) items.push(`Most recently registered: ${latestPatient}`);
  if (items.length === 0) items.push("No new notifications");

  const dot = bellWrap.querySelector(".bell-dot");
  if (dot) dot.style.display = waiting > 0 ? "block" : "none";

  const dropdown = document.createElement("div");
  dropdown.className = "dropdown-panel";
  dropdown.style.display = "none";
  dropdown.innerHTML = `
    <div class="dropdown-title">Notifications</div>
    ${items.map((i) => `<div class="dropdown-item">${i}</div>`).join("")}
  `;
  bellWrap.appendChild(dropdown);

  bellWrap.addEventListener("click", (e) => {
    e.stopPropagation();
    closeOtherDropdowns(dropdown);
    dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
  });
}

function setupProfileMenu() {
  const userBlock = document.querySelector(".user-block");
  if (!userBlock) return;

  const roleLabel = userBlock.querySelector(".user-role")?.textContent || "";

  const dropdown = document.createElement("div");
  dropdown.className = "dropdown-panel";
  dropdown.style.display = "none";
  dropdown.innerHTML = `
    <div class="dropdown-title">${roleLabel}</div>
    <a class="dropdown-item dropdown-item-action" href="profile.html">Profile settings</a>
    <div class="dropdown-item dropdown-item-action" id="profileLogoutBtn">Logout</div>
  `;
  userBlock.appendChild(dropdown);

  userBlock.addEventListener("click", (e) => {
    e.stopPropagation();
    closeOtherDropdowns(dropdown);
    dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
  });

  dropdown.querySelector("#profileLogoutBtn").addEventListener("click", (e) => {
    e.stopPropagation();
    mqLogout();
  });
}