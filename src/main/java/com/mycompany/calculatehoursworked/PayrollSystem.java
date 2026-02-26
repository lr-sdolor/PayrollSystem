/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calculatehoursworked;

/**
 *
 * @author synen
 */
// ===== IMPORTS =====
// BufferedReader allows rading text files line by line
import java.io.BufferedReader;
// FileReader allows opening and reading files from the file system
import java.io.FileReader;
// Duration allows calculation difference between two LocalTime objects
import java.time.Duration;
// LocalTime represents a specific time without date -- to be used for logging in and logging out
import java.time.LocalTime;
// YearMonth allows to get number of days in a specific month
import java.time.YearMonth;
// DateTimeFormatter allows parsing time strings like "15:00" into LocalTime objects
import java.time.format.DateTimeFormatter;
// Scanner allows user input from keyboard
import java.util.Scanner;

// ===== MAIN CLASS =====
public class PayrollSystem {
    
    // DEDUCTION METHODS
    // SSS Contribution
    public static double computeSSS(double grossSalary) {
        // sss method computes employee SSS contribution based on gross salary (reference: SSS Contribution.csv from MotorPH website)
        // check ranges and return corresponding contribution
        double baseContribution = 135.0; // min contribution
        double increment = 22.5; // increment per salary bracket
        double salaryStep = 500.0; // how much the salary must increase before moving to the next SSS contribution bracket; each bracket covers 500php of salary
        double startingSalary = 3250.0; // salary for base/min contribution 
        double maxContribution = 1125.0; // max ontribution
        
        // if gross salary is below the starting bracket, return minimum contribution
        if (grossSalary < startingSalary)
            return baseContribution; // min contribution
        
        // calculate how many brackets above starting salary
        int n = (int)((grossSalary - startingSalary)/salaryStep) + 1; // number of brackets
        
        // contribution is base + increment x number of brackets; capped at max contribution
        return Math.min(baseContribution + increment * n, maxContribution);          
    }

    // PhilHealth Contribution (50% employee share)
    // computes philhealth contribution (employee share 50%)
    // based on 3% of monthly basic, capped between 300 and 1800
    public static double computePhilHealth(double grossSalary) {
        double premium = grossSalary * 0.03; // 3% of monthly basic/gross
        if (premium <= 1000) premium = 300; // minimum contribution rule
        else if (premium >= 6000) premium = 1800; // maximum contribution rule
        return premium / 2; // employee pays half
    }

    // Pag-IBIG Contribution
    // computes employee pag-ibig contribution based on salary
    // 1% if salary <= 1500, otherwise 2%
    public static double computePagIbig(double grossSalary) {
        double contribution;
        if (grossSalary <= 1500) 
            contribution = grossSalary * 0.01;
        else 
            contribution = grossSalary * 0.02;
        return Math.min(contribution, 100);
    }
    
    // Withholding Tax
    // computes income tax based on taxable income
    // taxable income = gross - sss - philhealth - pag-ibig
    public static double computeWithholdingTax(double taxableIncome) {
        if (taxableIncome <= 20832) 
            return 0;
        else if (taxableIncome <= 33332) 
            return (taxableIncome - 20833) * 0.20;
        else if (taxableIncome <= 66666) 
            return 2500 + (taxableIncome - 33333) * 0.25;
        else if (taxableIncome <= 166666) 
            return 10833 + (taxableIncome - 66667) * 0.30;
        else if (taxableIncome <= 666666) 
            return 40833.33 + (taxableIncome - 166667) * 0.32;
        else return 200833.33 + (taxableIncome - 666667) * 0.35;
    }
    
    // Rounding money
    // ensures all monetary values are rounded to 2 decimal places
    public static double roundMoney(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    
    // HOURS COMPUTATION
    public static double computeHours(LocalTime login, LocalTime logout) {
        LocalTime graceTime = LocalTime.of(8, 10); // grace period allowed until 8:10
        LocalTime cutoffTime = LocalTime.of(17, 0); // official work end time
        
        // apply 17:00 cut off
        if (logout.isAfter(cutoffTime)) {
            logout = cutoffTime;
        }
        
        long minutesWorked = Duration.between(login, logout).toMinutes();
        
        // deduct lunch (if total worked is more than 1 hour
        if (minutesWorked > 60) {
            minutesWorked -= 60;
        } else {
            minutesWorked = 0;
        }
        
        double hours = minutesWorked / 60.0;
        
        // grace period rule
        if (!login.isAfter(graceTime)) {
            return 8.0;
        }
        hours = Math.round(hours);
        
        // return hours worked, capped at 8
        return Math.min(hours, 8.0);
    }   
    
    // helper method for displaying payroll
    public static void displayPayroll(String empNo, String lastName, String firstName, String birthday, double firstHalf, double secondHalf, double hourlyRate, String monthName, int monthDays) {
        double gross1 = roundMoney(firstHalf * hourlyRate);
        double gross2 = roundMoney(secondHalf * hourlyRate);
        double monthlyGross = roundMoney(gross1 + gross2);
        double sss = roundMoney(computeSSS(monthlyGross));
        double phil = roundMoney(computePhilHealth(monthlyGross));
        double pagibig = roundMoney(computePagIbig(monthlyGross));
        double tax = roundMoney(computeWithholdingTax(monthlyGross - (sss + phil + pagibig)));
        double totalDeductions = roundMoney(sss + phil + pagibig + tax);
        double net = roundMoney(monthlyGross - totalDeductions);
        
        System.out.println("\n================ "+monthName + " ================");
        System.out.println("Employee No.: " + empNo);
        System.out.println("Employee: " + lastName + ", " + firstName);
        System.out.println("Birthday: " + birthday);
        System.out.println("\n==== First Cut-Off ====");
        System.out.println("Total Hours Worked: " + firstHalf);
        System.out.println("Gross Salary: " + gross1);
        System.out.println("\n==== Second Cut-Off ====");
        System.out.println("Total Hours Worked: " + secondHalf);
        System.out.println("Gross Salary: " + gross2);
        System.out.println("\n==== Breakdown of Deductions ====");
        System.out.println("SSS: " + sss);
        System.out.println("PhilHealth: " + phil);
        System.out.println("Pag-IBIG: " + pagibig);
        System.out.println("Tax: " + tax);
        System.out.println("Total Deductions: " + totalDeductions);
        System.out.println("\nNet Salary: " + net);
    }
    
    // helper method for computing attendance
    public static double[] computeAttendance(String empNo, String attFile, int month) {
        double firstHalf = 0;
        double secondHalf = 0;
        
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
        
        try(BufferedReader br = new BufferedReader(new FileReader(attFile))) {
            br.readLine(); // skip header
            String line;
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if(!data[0].equals(empNo)) continue;
                
                String[] dateParts = data[3].split("/");
                int recordMonth = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if(year!=2024 || recordMonth != month) continue;
                
                LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
                
                double hours = computeHours(login, logout);
                if(day <= 15) {
                    firstHalf += hours;
                } else {
                    secondHalf += hours;
                }
            }
        } catch(Exception e) {
            System.out.println("Error reading attendance file for month " + month);
            e.printStackTrace();
        }
        
        return new double[] {
            firstHalf, secondHalf
        };
    }
    
    // helper method for processing payroll for employee
    public static void processPayrollForEmp(String empNo, String empFile, String attFile) {
        try(BufferedReader br = new BufferedReader(new FileReader(empFile))) {
            br.readLine(); //skip header
            String line;
            boolean found = false;
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if(data[0].equals(empNo)) {
                    String lastName = data[1];
                    String firstName = data[2];
                    String birthday = data[3];
                    double hourlyRate = Double.parseDouble(data[18].replace("\"", "").trim());
                    found = true;
                    
                    // Loop from June to Recember
                    for(int month = 6; month <= 12; month++) {
                        String monthName = switch(month) {
                            case 6 -> "June";
                            case 7 -> "July";
                            case 8 -> "August";
                            case 9 -> "September";
                            case 10 -> "October";
                            case 11 -> "November";
                            case 12 -> "December";
                            default -> "Month "+month;
                        };
                        int monthDays = YearMonth.of(2024, month).lengthOfMonth();
                        double[] hours = computeAttendance(empNo, attFile, month);
                        displayPayroll(empNo, lastName, firstName, birthday, hours[0], hours[1], hourlyRate, monthName, monthDays);
                    }
                    break;
                }
            }
            if(!found) {
                System.out.println("Employee number does not exist.");
            } 
        } catch(Exception e) {
                System.out.println("Error reading employee file.");
                e.printStackTrace();
        }
    }
    
    // main program
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==== MOTORPH LOGIN ====");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        if(username.equals("employee") && password.equals("12345")) {
            System.out.println("Login Successful!");
        }
        else if (username.equals("payroll_staff") && password.equals("12345")) {
            System.out.println("Login Sucessful!");
        }
        else {
            System.out.println("Incorrect username or password");
        }
        
        String empFile = "src\\main\\java\\com\\mycompany\\calculatehoursworked\\employee_details.csv";
        String attFile = "src\\main\\java\\com\\mycompany\\calculatehoursworked\\attendance_record.csv";
        
        // employee flow
        if(username.equals("employee")) {
            while(true) {
                System.out.println("\n1. Enter Employee Number");
                System.out.println("\n2. Exit Program");
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                if(choice == 2) {
                    break;
                }
                
                System.out.print("Enter Employee Number: ");
                String empNo = scanner.nextLine();
                boolean found = false;
                
                try(BufferedReader br = new BufferedReader(new FileReader(empFile))) {
                    br.readLine(); // skip header
                    String line;
                    while((line = br.readLine()) != null) {
                        if(line.trim().isEmpty()) continue;
                        String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                        if(data[0].equals(empNo)) {
                            found = true;
                            System.out.println("\n===================================");
                            System.out.println("Employee # : " + data[0]);
                            System.out.println("Employee Name : " + data[1] + ", " + data[2]);
                            System.out.println("Birthday : " + data[3]);
                            System.out.println("===================================");
                            break;
                        }
                    }
                    if(!found) {
                        System.out.println("Employee number does not exist.");
                    } 
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        if(username.equals("payroll_staff")) {
            while(true) {
                System.out.println("\n1. Process Payroll");
                System.out.println("\n2. Exit Program");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 2) {
                    break;
                }
                
                System.out.println("\n1. One Employee");
                System.out.println("\n2. All Employees");
                System.out.println("\n3. Exit Program");
                int subChoice = scanner.nextInt();
                scanner.nextLine();
                if (subChoice == 3) {
                    break;
                }
                
                if(subChoice == 1) {
                    System.out.print("Enter Employee Number: ");
                    String empNo  = scanner.nextLine();
                    processPayrollForEmp(empNo, empFile, attFile);
                }
                
                if(subChoice == 2) {
                    try(BufferedReader br = new BufferedReader(new FileReader(empFile))) {
                        br.readLine(); // skip header
                        String line;
                        while((line = br.readLine()) != null) {
                            if(line.trim().isEmpty()) {
                                continue;
                            }
                            String empNo = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0];
                            processPayrollForEmp(empNo, empFile, attFile);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}    