# PayrollSystem
MotorPH Payroll System Phase 1 for Computer Programming 1

# MotorPH Payroll System

A Java-based payroll processing system that allows employees to view employee details and payroll staff to compute and process payroll for one or all employees. The program handles attendance, computes hours worked, and calculates deductions and taxes.

---

## System Overview

The application reads employee and attendance data from CSV files and performs automated payroll computation. Depending on the user role, the system provides access either to employee information viewing or payroll processing functions.

Users are required to log in before accessing the system:

- Employee login (`employee`) allows viewing of employee details.
- Payroll staff login (`payroll_staff`) enables payroll computation and processing.

Default password for both accounts: 12345

---

## Employee Functions

After successful login, employees may enter their employee number to view stored personal information retrieved from the employee database. The system displays:

- Employee Number  
- Employee Name  
- Birthday  

---

## Payroll Processing

Payroll staff are provided with options to process payroll for a single employee or for all employees in the system. Payroll computation covers the period from **June to December 2024**.

The payroll module automatically computes:

- Total hours worked
- Gross salary per cut-off
- Government deductions
- Net salary with detailed breakdown

---

## Attendance Processing

Attendance information is obtained from: attendance_record.csv

Employee working hours are evaluated using the following rules:

- Standard working hours: **8 hours per day**
- Login before **8:10 AM** counts as a full work shift
- One-hour lunch break deduction when applicable
- Hours rounded to the nearest whole number
- Maximum credited hours per day: **8 hours**

---

## Payroll Computation Method

Employee salary is calculated using the formula: Hourly Rate = Monthly Salary ÷ 21 Working Days ÷ 8 Hours

Payroll is divided into two cut-off periods:

- **First Cut-Off:** Day 1–15  
- **Second Cut-Off:** Day 16–End of Month  

Mandatory deductions automatically applied include:

- SSS Contribution
- PhilHealth Contribution (Employee Share)
- Pag-IBIG Contribution
- Withholding Tax

---

## CSV File Handling

The system relies on the following files:

- `employee_details.csv` – employee information  
- `attendance_record.csv` – daily attendance records  

CSV handling includes:
- Support for quoted fields
- Skipping invalid or empty rows
- Prevention of processing errors caused by incomplete data

---

## Project Structure

PayrollSystem
│
├── src/main/java/com/mycompany/calculatehoursworked
│ ├── PayrollSystem.java
│ ├── CalculateHoursWorked.java
│ ├── CalculateDeductions.java
│
├── employee_details.csv
├── attendance_record.csv
├── pom.xml
└── README.md

---

## System Requirements

To run the program, the following are required:

- Java 8 or higher
- Maven
- Apache NetBeans IDE

---

## Running the Program

### Using NetBeans
1. Open Apache NetBeans IDE
2. Open the PayrollSystem project
3. Right-click the project
4. Select **Run Project**

### Using Terminal
mvn clean compile
mvn exec:java

---

## Error Handling

The system includes validation mechanisms to handle:

- Invalid employee numbers
- Missing CSV entries
- Incorrect login credentials
- Empty or malformed attendance records

---

## Future Improvements

Planned enhancements for future versions include:

- Graphical User Interface (GUI)
- Database integration
- Automated payslip generation
- Payroll reporting system
- Improved authentication and access control

---

## Developers

Synen Dolor  
MotorPH Payroll System Development Team

---

## License

This project was developed for academic purposes under Computer Programming 1 coursework requirements.
