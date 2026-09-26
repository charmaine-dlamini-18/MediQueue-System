# MediQueue System

A clinic queue-management and patient-records system. Staff register walk-in or
booked patients, patients are queued and called through a clinic queue, and
every stage of the visit — vital signs, symptom analysis, consultation notes,
prescriptions and dispensing — is written to a MySQL database and read back by
the web front end through a REST API.

* **Backend:** Spring Boot 3.2.5, Java 21, Spring Data JPA / Hibernate, MySQL 8
* **Frontend:** vanilla HTML, CSS and JavaScript (no framework, no build step)
* **Tests:** JUnit 5, Mockito, MockMvc, H2 (55 test classes, 308 tests)

---

## Table of contents

1. [What the system does](#what-the-system-does)
2. [Tech stack](#tech-stack)
3. [Prerequisites](#prerequisites)
4. [Setup and running](#setup-and-running)
5. [Demo accounts](#demo-accounts)
6. [Project structure](#project-structure)
7. [REST API reference](#rest-api-reference)
8. [Database design](#database-design)
9. [Frontend-to-backend integration](#frontend-to-backend-integration)
10. [Design decisions](#design-decisions)
11. [Testing](#testing)
12. [Troubleshooting](#troubleshooting)

---

## What the system does

| Role | Responsibilities |
|------|------------------|
| **Receptionist** | Books appointments, registers walk-ins, opens the daily queue, calls the next patient, assigns the doctor |
| **Nurse** | Works the queue, records vital signs, runs symptom analysis |
| **Doctor** | Runs the consultation queue, writes medical records, issues prescriptions |
| **Pharmacist** | Views prescriptions, marks them dispensed |
| **Patient** | Books appointments, tracks their queue position, views records and prescriptions, receives notifications |
| **Administrator** | Manages staff accounts, clinics and departments |

### End-to-end flow

```
Walk-in / booking  ->  Appointment + QueueEntry (doctor may be NULL)
   ->  Nurse: VitalSigns + SymptomsAnalysis
   ->  Doctor: MedicalRecord
   ->  Doctor: Prescription
   ->  Pharmacist: prescription status = Dispensed
```

Every arrow above is an HTTP call that becomes a MySQL row.

---

## Tech stack

| Layer | Technology | Documentation |
|-------|-----------|---------------|
| Web framework | Spring Boot 3.2.5 | https://docs.spring.io/spring-boot/docs/3.2.5/reference/html/ |
| Language | Java 21 (LTS) | https://docs.oracle.com/en/java/javase/21/ |
| Persistence | Spring Data JPA | https://docs.spring.io/spring-data/jpa/reference/ |
| ORM | Hibernate 6 | https://docs.jboss.org/hibernate/orm/6.2/ |
| Database | MySQL 8 | https://dev.mysql.com/doc/refman/8.0/en/ |
| JDBC driver | mysql-connector-j | https://dev.mysql.com/doc/connector-j/en/ |
| JSON mapping | Jackson | https://github.com/FasterXML/jackson-docs |
| Validation | Apache Commons Validator 1.10.1 | https://commons.apache.org/proper/commons-validator/ |
| Auth | Custom `HandlerInterceptor` (JWT-style opaque token) | https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/handlermapping.html |
| Test DB | H2 (in-memory) | https://www.h2database.com/html/features.html |
| Unit tests | JUnit 5 + Mockito | https://junit.org/junit5/docs/current/user-guide/ , https://site.mockito.org/ |
| Integration tests | Spring MockMvc | https://docs.spring.io/spring-framework/reference/testing/webappctx/mvc-mockmvc.html |
| Build | Apache Maven (wrapper included) | https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html |
| Frontend | HTML5, CSS3, ES6+ JavaScript, Fetch API | https://developer.mozilla.org/en-US/docs/Web/JavaScript , https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API |

No Spring Security is used. Authentication is a small `HandlerInterceptor`
that validates a bearer token issued at login, which keeps the token format
inspectable for demonstration purposes.

---

## Prerequisites

* **JDK 21** — `java -version`
* **MySQL 8** running on `localhost:3306`, or set `MYSQL_HOST`
* **Maven** is not required; the repository includes `mvnw.cmd` (Windows) and
  `mvnw` (macOS/Linux)
* A modern browser. A static file server is only needed because the front end
  uses `fetch` (see below)

---

## Setup and running

### 1. Database

The application creates its own schema and tables on first start
(`createDatabaseIfNotExist=true`, `ddl-auto=update`). You only need a MySQL
server with a user that can create databases:

```sql
CREATE USER 'root'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';
```

Connection settings live in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/MediQueue?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
server.servlet.context-path=/MediQueue
server.port=8080
```

Override the host without editing the file:

```powershell
$env:MYSQL_HOST="localhost"
```

### 2. Backend

```powershell
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

Startup log ends with:

```
Tomcat started on port 8080 (http) with context path '/MediQueue'
DataSeeder: seeding complete (roles, clinics, departments, patients, staff, appointments, visits, records, prescriptions, queue).
```

`DataSeeder` runs **only when the `roles` table is empty**, so restarting never
duplicates data. It inserts ~50 interlinked, domain-realistic records: 6
patients, 8 staff members, 4 clinics, 4 departments, 5 appointments, 4 visits,
3 medical records, 5 prescriptions, 1 queue and 5 queue entries.

Verify the API is up:

```powershell
curl http://localhost:8080/MediQueue/clinic/getAll
```

### 3. Frontend

The pages are plain static files. Because they call the API with `fetch`, they
must be served over `http://` rather than opened with `file://`. Either use VS
Code **Live Server** (right-click `frontend/html/index.html` → *Open with Live
Server*), or:

```powershell
npx serve frontend
```

Then open the URL it prints, for example `http://localhost:3000/html/index.html`.

The API base URL is defined once, in `frontend/js/data.js`:

```js
const API_BASE = "http://localhost:8080/MediQueue";
```

Change it there if your backend runs on another host or port.

### 4. Tests

```powershell
.\mvnw.cmd test
```

Expected: `Tests run: 308, Failures: 0, Errors: 0, Skipped: 2` and
`BUILD SUCCESS`. Tests use an in-memory H2 database, so MySQL does not need to
be running.

### 5. Reset the database

```sql
DROP DATABASE MediQueue;
```

Restart the backend; the schema and starter data are recreated.

---

## Demo accounts

All seeded accounts use the password **`pass123`**.

| Role | Email | ID |
|------|-------|-----|
| Administrator | s.ndaba@mediqueue.co.za | U-107 |
| Receptionist | thandi.m@mediqueue.co.za | U-100 |
| Doctor | n.zulu@mediqueue.co.za | U-101 |
| Doctor (Pediatrics) | l.pillay@mediqueue.co.za | U-102 |
| Doctor (Antenatal) | t.mthembu@mediqueue.co.za | U-103 |
| Doctor (HIV Care) | r.jacobs@mediqueue.co.za | U-104 |
| Pharmacist | p.adams@mediqueue.co.za | U-105 |
| Nurse | b.mnyamana@mediqueue.co.za | U-106 |
| Patient | sipho.dlamini@mediqueue.co.za | P-0231 |
| Patient | nomsa.khumalo@mediqueue.co.za | P-0232 |

Staff sign in at `frontend/html/staff-login.html`, patients at
`frontend/html/patient-login.html`.

---

## Project structure

```
MediQueue-System/
├── mvnw / mvnw.cmd           Maven wrapper
├── pom.xml                   Dependencies, Java 21, surefire config
├── src/main/java/com/cput/mediqueuesystem/
│   ├── config/               WebConfig, AuthInterceptor, DataSeeder, SchemaAlignment
│   ├── controller/           16 REST controllers (HTTP layer only)
│   ├── domain/               14 JPA entities
│   ├── factory/              Entity factories (builder entry points)
│   ├── repository/           Spring Data JPA interfaces
│   ├── service/              Business logic, password hashing, referential cleanup
│   └── util/                 PasswordUtil (PBKDF2), ApiExceptionHandler
├── src/main/resources/application.properties
├── src/test/java/...         55 test classes (unit + integration)
└── frontend/
    ├── html/                 42 pages
    ├── css/                  Shared stylesheet(s)
    └── js/
        ├── data.js           The single API layer for the whole front end
        ├── guard.js          Session and role guards
        └── pages/            One script per page (rendering only)
```

**The layering rule:** a controller never touches a repository, and a page
script never calls `fetch` directly. Business rules live in services, HTTP lives
in controllers, rendering lives in page scripts.

---

## REST API reference

Base URL: `http://localhost:8080/MediQueue`

| Resource | Prefix | Endpoints |
|----------|--------|-----------|
| Auth | `/api/auth` | `POST /register`, `POST /login`, `POST /logout`, `GET /me` |
| Roles | `/api/roles` | `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| Patients | `/patient` | `GET /getAll`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Staff | `/staff` | `GET /getAll`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Users | `/user` | `GET /getAll`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Clinics | `/clinic` | `GET /getAll`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Departments | `/department` | `GET /getAll`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{departmentId}` |
| Appointments | `/appointment` | `GET /getAll`, `GET /read/{id}`, `GET /patient/{patientId}`, `GET /doctor/{doctorId}`, `POST /create`, `PUT /update`, `PUT /assign/{appointmentId}/{doctorId}`, `PUT /status/{appointmentId}/{status}`, `DELETE /delete/{id}` |
| Visits | `/mediqueue/visit` | `GET /all`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Vital signs | `/mediqueue/vital-signs` | `GET /all`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Symptom analysis | `/mediqueue/symptoms-analysis` | `GET /all`, `GET /read/{id}`, `GET /patient/{patientId}`, `POST /analyze`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Medical records | `/medical-record` | `GET /getAll`, `GET /read/{id}`, `GET /patient/{patientId}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Prescriptions | `/prescription` | `GET /getAll`, `GET /read/{id}`, `POST /create`, `PUT /update`, `PUT /status/{id}/{status}`, `DELETE /delete/{id}` |
| Queues | `/mediqueue/queue` | `GET /all`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |
| Queue entries | `/mediqueue/queue-entry` | `GET /all`, `GET /read/{id}`, `POST /create`, `PUT /update`, `DELETE /delete/{id}` |

### Example: log in and use the token

```powershell
$login = Invoke-RestMethod -Method Post `
  -Uri http://localhost:8080/MediQueue/api/auth/login `
  -ContentType "application/json" `
  -Body '{"email":"thandi.m@mediqueue.co.za","password":"pass123"}'

$headers = @{ Authorization = "Bearer $($login.token)" }

Invoke-RestMethod -Uri http://localhost:8080/MediQueue/patient/getAll -Headers $headers
```

---

## Database design

14 tables, InnoDB, foreign keys enforced.

```
roles ──┐
        ├──< user >────┬──── patient
        │              └──── staff ────> department ────> clinic
        └────────────── role_id
                        user_id (PK, shared)

patient ──┬──< appointment >──┬── staff (doctor, NULLABLE)
          │                   └── clinic, department, created_by (staff)
          ├──< visit >─────── appointment (nullable)
          │      └──< vital_signs
          ├──< medical_record >── staff (doctor)
          │      └──< prescription
          ├──< symptoms_analysis
          ├──< queue_entry >── queue, staff (doctor, NULLABLE)
          └──< prescription (via record)
```

Key constraints:

* `user.user_id` — primary key; `patient` and `staff` use it as a shared
  primary key (`InheritanceType.JOINED`)
* `user.email` and `patient.id_number` — `UNIQUE`
* `appointment.patient_id`, `visit.patient_id`, `medical_record.patient_id`,
  `queue_entry.patient_id` — `NOT NULL`
* `appointment.doctor_id`, `queue_entry.doctor_id` — **nullable**, because a
  patient can queue before a doctor is allocated
* `queue_entry.queue_number` — **nullable**, because a queue number is only
  issued once the booking is approved; an unapproved request holds no number
* `queue_entry.queue_id` — `NOT NULL`

### Indexes

Indexes are declared on the entity that owns the table, and Hibernate creates
them on startup. They cover the columns the application actually filters on:

| Index | Table | Column | Query it serves |
|-------|-------|--------|-----------------|
| `idx_user_role` | user | role_id | staff list filtered by role |
| `idx_user_last_name` | user | last_name | staff name search |
| `idx_staff_department` | staff | department_id | doctors-per-department dropdown |
| `idx_staff_position` | staff | position | staff list filtered by position |
| `idx_appointment_patient` | appointment | patient_id | "my appointments" |
| `idx_appointment_date` | appointment | scheduled_date | one day's diary |
| `idx_appointment_doctor` | appointment | doctor_id | one doctor's diary |
| `idx_appointment_status` | appointment | status | filter by state |
| `idx_visit_patient` | visit | patient_id | patient's history |
| `idx_visit_appointment` | visit | appointment_id | visit behind an appointment |
| `idx_visit_date` | visit | visit_date | "visits today" |
| `idx_medical_record_patient` | medical_record | patient_id | patient's records |
| `idx_medical_record_date` | medical_record | record_date | newest-first history |
| `idx_prescription_record` | prescription | record_id | record + its prescriptions |
| `idx_prescription_status` | prescription | status | pharmacy worklist |
| `idx_queue_entry_queue` | queue_entry | queue_id | the queue board |
| `idx_queue_entry_patient` | queue_entry | patient_id | patient's queue position |
| `idx_queue_entry_status` | queue_entry | status | "who is waiting" |
| `idx_queue_date` | queue | date | today's queues |

`user.email` and `patient.id_number` are `UNIQUE`, which MySQL backs with an
index automatically.

---

## Frontend-to-backend integration

`frontend/js/data.js` is the only module that knows the API exists. Page scripts
import from it and never call `fetch` themselves.

```js
// 1. What to load, and from where
const API_COLLECTIONS = [
  { key: "patients",   path: "/patient/getAll" },
  { key: "staff",      path: "/staff/getAll" },
  { key: "queue",      path: "/mediqueue/queue-entry/getAll" },
  // ...
];

// 2. Fetch everything in parallel
async function refreshFromApiAsync() { /* ... */ }

// 3. Normalise each row for the page layer
const data = buildDataFromApi(patients, staff, queues, ...);

// 4. Cache it
setData(data);

// 5. Render
renderPatientTable(getData().patients);
```

Other integration points worth demonstrating:

* **Optimistic-free writes.** Every form calls an `...Async()` helper, awaits the
  HTTP response, and only updates the UI after a 2xx. There is no `catch` that
  writes to `localStorage` — a failed save shows an error and changes nothing.
* **Error handling.** If the backend is unreachable, `data.js` shows a banner
  ("Cannot reach the MediQueue server") and polls every few seconds until the
  connection returns, then re-syncs automatically.
* **No hardcoded data.** If a table has no rows, the page renders an empty-state
  message. It never invents a placeholder patient, clinic or queue number.
* **Identity from the session.** The signed-in user's name and role in the
  sidebar and top bar come from `/api/auth/me` via `fillIdentityChrome()`, not
  from markup.
* **Notifications are derived.** `patient-notifications.js` builds its feed from
  the patient's real appointments, queue entry, records and prescriptions. Only
  the read/unread flags are stored locally, because the database has no
  notification table.

---

## Design decisions

| Decision | Why |
|----------|-----|
| **Layered architecture** (controller → service → repository) | Business rules are testable without HTTP; the repository layer is swappable |
| **Repository pattern** | Spring Data JPA generates the implementations, removing DAO boilerplate |
| **Builder pattern** on every entity | Validated, readable construction; `@JsonPOJOBuilder` gives Jackson a matching deserializer |
| **Reference stubs, not nested objects** | The browser sends `{"type":"staff","userId":"U-101"}`, so a client can never bind or overwrite an unrelated entity graph |
| **Merge-on-update** in `PatientService` / `StaffService` | A partial form payload must not null out a `NOT NULL` column |
| **Password is write-only JSON** | `User.getPassword()` is `@JsonProperty(WRITE_ONLY)`, so hashes are never sent to the browser |
| **PBKDF2 hashing** in the service | Plaintext passwords are never stored; legacy plaintext is migrated on the next successful update |
| **Referential-safe patient delete** | `PatientService.delete` removes vitals → visits → prescriptions → records → symptoms → queue entries → appointments → patient, in FK order |
| **Nullable doctor columns** | Real queues contain unallocated patients; enforced by `SchemaAlignment` at startup |
| **Numbering owned by the server** | `queue_number` is nullable and is only issued by `QueueNumberingService` once a doctor is assigned or the booking is approved, so an unapproved request never looks like it is already in line |
| **Hand-written auth interceptor** | Small, inspectable, and sufficient for a demonstration project |

### Edge cases handled

* Nullable `doctor_id` on appointments and queue entries
* Partial updates preserve unspecified fields
* `User.status` is a primitive `boolean`; the front end always sends the current
  value so an omitted field is never read as `false`
* Unknown IDs return `404`; validation failures return a readable message
  through `ApiExceptionHandler` instead of a blank `400`
* Deleting a patient with linked history succeeds instead of failing on a
  foreign-key constraint
* Clinic and department creation generate a client-side key when the backend
  does not supply one
* Backend offline: banner + automatic retry, never a silent local "success"
* Empty tables render an empty state instead of placeholder rows

---

## Testing

```powershell
.\mvnw.cmd test
```

| Level | How it is written | Example |
|-------|-------------------|---------|
| **Unit** | JUnit 5 + Mockito, no Spring context | `QueueEntryServiceTest`, `PatientServiceTest`, `StaffServiceTest` |
| **Integration** | `@WebMvcTest` + MockMvc against an H2 schema | `QueueEntryControllerTest`, `AppointmentControllerTest` |
| **Factories** | Entity builder validation | `QueueEntryFactoryTest` |

Coverage highlights:

* Queue promotion: only `Waiting` entries are promoted, `doctor_id` is set
* Queue numbering: no number while a booking is unapproved, issued automatically once a doctor is assigned
* Merge-on-update: omitted fields are preserved
* Password hashing: plaintext input is stored hashed
* Cascade delete: dependents are removed before the patient
* Controller contract: status codes and JSON shape for each endpoint

---

## Troubleshooting

| Symptom | Cause and fix |
|---------|---------------|
| Front end shows "Cannot reach the MediQueue server" | Backend is not running, or `API_BASE` in `frontend/js/data.js` points at the wrong port |
| Blank page when opening `index.html` directly | Pages need `http://`; use Live Server or `npx serve frontend` |
| `Access denied for user 'root'` | MySQL credentials differ — set them in `application.properties` or `MYSQL_HOST` |
| Port 8080 already in use | Change `server.port` in `application.properties` and `API_BASE` in `data.js` |
| Tables exist but are empty | `DataSeeder` skips when data is present; drop the database to reseed |
| Login returns 401 with correct credentials | Accounts are seeded with password `pass123` |
| Maven cannot resolve dependencies | Run once with network access: `.\mvnw.cmd -U test` |

---

## References

* Spring Boot reference — https://docs.spring.io/spring-boot/docs/3.2.5/reference/html/
* Spring Data JPA — https://docs.spring.io/spring-data/jpa/reference/
* Hibernate 6 User Guide — https://docs.jboss.org/hibernate/orm/6.2/
* MySQL 8 Reference Manual — https://dev.mysql.com/doc/refman/8.0/en/
* Jackson Annotations — https://github.com/FasterXML/jackson-annotations
* JUnit 5 User Guide — https://junit.org/junit5/docs/current/user-guide/
* Mockito — https://site.mockito.org/
* Spring MockMvc — https://docs.spring.io/spring-framework/reference/testing/webappctx/mvc-mockmvc.html
* H2 Database — https://www.h2database.com/html/features.html
* MDN Fetch API — https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API
