# Placement Management System — Complete & Smooth Workflow Guide

This guide details the complete, tested workflow for the **Placement Management System**, covering credentials, architecture, and step-by-step user journeys for both **Students** and **Administrators**.

---

## 🔑 Authentication Credentials

### 1. Administrator Account
| Field | Value |
|---|---|
| **Portal** | Admin Portal |
| **Username** | `admin` |
| **Password** | `admin123` |

### 2. Sample Student Accounts
| Student Name | Email | Password | Department | CGPA |
|---|---|---|---|---|
| **Ravi Kumar** | `ravi@gmail.com` | `ravi123` | CSE | 8.50 |
| **Priya Sharma** | `priya@gmail.com` | `priya123` | ECE | 9.00 |
| **Arun Raj** | `arun@gmail.com` | `arun123` | IT | 7.80 |
| **Sneha Reddy** | `sneha@gmail.com` | `sneha123` | CSE | 8.20 |
| **Karthik M** | `karthik@gmail.com` | `karthik123` | MECH | 7.50 |

*(New student accounts can also be created via **"Create an account"** on the Student Login page).*

---

## 🚀 Application Launch & Entry Point

- **Launch Command**:
  ```powershell
  java -cp "bin;lib/*" Main
  ```
- **Landing Screen (`HomePage`)**:
  - Displays dynamic ambient floating graphics, real-time counters, and animated stat bars.
  - Choose between two distinct entry portals:
    1. **Student Portal** (Graduation Cap Icon) -> navigates to `StudentLoginForm`.
    2. **Admin Portal** (Shield Icon) -> navigates to `AdminLoginForm`.

---

## 🎓 1. Student Portal Workflow (Step-by-Step)

```
[HomePage] 
   └── [Student Portal]
          ├── Login (or "Create an account" -> Registration -> Login)
          └── [Student Dashboard]
                 ├── 1. Dashboard Home (Personalized greeting, resume alert, KPI stats, quick tables)
                 ├── 2. Update Profile (Edit department, CGPA, phone number)
                 ├── 3. Upload Resume (Browse PDF/Word document -> local storage linked to profile)
                 ├── 4. Placement Drives
                 │      ├── Eligible Drives Tab (One-click "Apply Now" buttons)
                 │      └── All Active Drives Tab (Timeline of all company drives)
                 ├── 5. Apply For Drive (Direct application submission via Drive ID)
                 ├── 6. My Applications (Live application tracking: Pending / Approved / Rejected)
                 ├── 7. Notice Board (View sticky announcements from administration)
                 ├── 8. My Offers (View CTC packages, Accept or Decline pending offers)
                 ├── 9. Change Password (Update account security credentials)
                 └── Logout -> Returns to HomePage
```

### Step 1: Login or Register
1. From `HomePage`, click **Student Portal**.
2. If already registered, enter your email (e.g., `ravi@gmail.com`) and password (`ravi123`), then press **Enter** or click **LOG IN**.
3. *If new*: Click **"Create an account"**, fill in Full Name, Email, Password, Department, CGPA (0.0 to 10.0), and Phone Number. Click **Register Account** and log in.

### Step 2: Dashboard Home
- View your personalized greeting banner.
- Check the **Placement Eligibility Status Card**:
  - ⛔ *Action Required*: Alerts you if no resume is uploaded.
  - ✅ *Ready*: Confirms your resume is active and on file.
- Check real-time stats: **Eligible Drives**, **Applied Drives**, and **Placement Status** (`ACTIVE` or `PLACED ✓`).
- Quick-glance tables show your latest upcoming eligible drives and recent applications.

### Step 3: Update Profile
1. Select **Update Profile** from the sidebar.
2. View and edit your academic Department, Cumulative CGPA, and Phone Number.
3. Click **Save Changes** to update your database record.

### Step 4: Upload Resume (Prerequisite for Applying)
1. Select **Upload Resume** from the sidebar.
2. Click the interactive drop-zone box to open the file browser.
3. Select a `.pdf`, `.docx`, or `.doc` file.
4. Click **Upload Document**. The resume is saved to the local application store and linked to your student ID.

### Step 5: Explore Placement Drives & Apply
1. Select **Placement Drives** from the sidebar.
   - **Eligible Drives Tab**: Lists all drives where your CGPA meets the company's requirement. Click the **"Apply Now"** button directly on any drive card.
   - **All Active Drives Tab**: Chronological timeline of all upcoming drives across all companies.
2. *Alternative Direct Application*:
   - Select **Apply For Drive** from the sidebar.
   - Enter the target **Drive ID** and click **Submit Application**. The system validates your eligibility and prevents duplicates.

### Step 6: Track Applications
1. Select **My Applications** from the sidebar.
2. View status tracker cards showing your submitted applications with live badges: `Pending`, `Approved`, or `Rejected`.

### Step 7: College Notice Board
1. Select **Notice Board** from the sidebar.
2. View notes and updates posted by the placement department.

### Step 8: View & Respond to Job Offers
1. Select **My Offers** from the sidebar.
2. View cards showing company name, job role, compensation package (LPA), and offer date.
3. If an offer is **Pending**, click on the card to either **Accept Offer** or **Decline Offer**. The status updates in the database immediately.

### Step 9: Change Password & Logout
1. Select **Change Password** to update your login password.
2. Click **Logout** at the bottom of the sidebar to securely return to the portal selector.

---

## 🛡️ 2. Administrator Portal Workflow (Step-by-Step)

```
[HomePage] 
   └── [Admin Portal]
          ├── Login (Username: admin, Password: admin123)
          └── [DashboardFrame - Admin Console]
                 ├── 1. Dashboard (KPI statistics, quick action triggers, live active drives)
                 ├── 2. Students (Search students by name, view full details & CGPA badges)
                 ├── 3. Companies
                 │      ├── View Companies (Inspect cards, delete company)
                 │      └── Add Company (Register new recruiting company)
                 ├── 4. Drives
                 │      ├── View Placement Drives (Timeline of dates & required CGPA)
                 │      └── Create New Drive (Schedule new drive for company)
                 ├── 5. Applications (Approve/Reject applicants, view student resume directly)
                 ├── 6. Notice Board (Post new sticky notices, delete expired notes)
                 ├── 7. Offers (Kanban board: Pending/Accepted/Declined, issue new offers, click card to update status)
                 ├── 8. Interviews (Add assessment rounds to any drive)
                 ├── 9. Analytics (Interactive donut chart & progress bars of application distribution)
                 └── Logout -> Returns to HomePage
```

### Step 1: Admin Login
1. From `HomePage`, click **Admin Portal**.
2. Enter username `admin` and password `admin123`, then click **LOG IN**.
3. The comprehensive `DashboardFrame` management console opens.

### Step 2: Main Dashboard & Live Metrics
- **Top Metric Cards**: Real-time counts for Total Students, Registered Companies, Placement Drives, and Job Applications.
- **Quick Controls**: Instant jump buttons to Add Company, Create Drive, Search Students, and Manage Applications.
- **System Activity Log**: Live database connection and metric summary.
- **Live Placement Drives**: Animated overview cards of current company drives.

### Step 3: Student Directory (`Students`)
1. Click **Students** in the sidebar.
2. Instantly view all registered students with color-coded avatar initials and CGPA badges.
3. Use the search bar at the top to filter students by name in real-time.

### Step 4: Company Management (`Companies`)
1. Click **Companies** in the sidebar.
2. **View Companies Tab**: Displays company cards showing package, location, and role. Select a card and click **Delete Selected Company** if needed.
3. **Add Company Tab**: Register a new recruiting company by providing Company Name, Location, Package (LPA), and Role.

### Step 5: Placement Drives (`Drives`)
1. Click **Drives** in the sidebar.
2. **View Placement Drives Tab**: Visual timeline of all scheduled drives, including drive date, package, and minimum required CGPA.
3. **Create New Drive Tab**: Schedule a hiring drive by providing the Company ID, Drive Date (`YYYY-MM-DD`), and Minimum CGPA.

### Step 6: Application Review & Resume Verification (`Applications`)
1. Click **Applications** in the sidebar.
2. View the table of all applicant submissions (Student Name, Company, Status).
3. Select an application row:
   - Click **View Resume**: Automatically opens the student's uploaded PDF or document in the system viewer.
   - Click **Approve**: Marks the application as approved, advancing the student in the process.
   - Click **Reject**: Marks the application as rejected.

### Step 7: Notice Board (`Notice Board`)
1. Click **Notice Board** in the sidebar.
2. View active sticky notes on the corkboard.
3. Click **+ Post New Notice** to publish an announcement with title and content.
4. Click on an existing sticky note and select **🗑 Delete Selected** to remove expired notices.

### Step 8: Offer Board & Kanban Tracking (`Offers`)
1. Click **Offers** in the sidebar.
2. Kanban view organized into three columns: **Pending Offers**, **Accepted Offers**, and **Declined Offers**.
3. **Issue New Offer**: Click **+ Issue New Offer**, enter the Student ID, Company ID, Role, CTC, and initial status.
4. **Interactive Status Update**: Click any offer card directly to toggle its status (`Pending` / `Accepted` / `Declined`). The card instantly moves to the appropriate column.

### Step 9: Interview Rounds (`Interviews`)
1. Click **Interviews** in the sidebar.
2. Add assessment rounds (e.g., *Online Aptitude*, *Technical Interview 1*, *HR Discussion*) for any active Drive ID.

### Step 10: Placement Analytics (`Analytics`)
1. Click **Analytics** in the sidebar.
2. View real-time visual progress bars and a dynamic donut chart breaking down the percentages of **Approved**, **Pending**, and **Rejected** student applications.

### Step 11: Logout
- Click **Logout** at the bottom of the sidebar to return to `HomePage`.
