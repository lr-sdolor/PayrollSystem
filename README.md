# PayrollSystem
MotorPH Payroll System Phase 1 for Computer Programming 1

# MotorPH Payroll System

A Java-based payroll processing system that allows employees to view employee details and payroll staff to compute and process payroll for one or all employees. The program handles attendance, computes hours worked, and calculates deductions and taxes.

---

## Features

### Login System
- Employee login (`employee`) to view employee details.
- Payroll staff login (`payroll_staff`) to process payroll.
- Password for both: `12345`.

### Employee Flow
- View personal information by entering employee number.
- Displays:
  - Employee number
  - Name
  - Birthday

### Payroll Staff Flow
- Process payroll for one or all employees.
- Computes monthly payroll from June to December 2024.
- Attendance processing from CSV file (`attendance_record.csv`):
  - Grace period: Login before 8:10 AM counts as full 8 hours.
  - Lunch deduction: 1 hour deducted if total worked > 1 hour.
  - Hours rounded to nearest whole number and capped at 8 per day.
- Payroll computations:
  - Gross salary per cut-off (first and second half of the month)
  - Statutory deductions:
    - SSS
    - PhilHealth (50% employee share)
    - Pag-IBIG
    - Withholding tax
  - Net salary and detailed breakdown

### CSV File Handling
- `employee_details.csv` contains employee information.
- `attendance_record.csv` contains daily attendance records.
- Properly handles quoted fields and skips invalid/empty rows.

---

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven (for building and running the project)
- CSV files located in:
