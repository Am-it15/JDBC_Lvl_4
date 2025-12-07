# 🎓 Student Management System

A **console-based Java application** for managing student records using **MySQL stored procedures**.  
This system provides complete CRUD operations with **auto-generated student IDs** and a clean **menu-driven interface**.

---

## 🌟 Features

| Feature | Description |
|---------|-------------|
| ➕ **Add Student** | Register new student with 12 fields including personal and parent information |
| 🔍 **View Students** | Retrieve individual student by ID or view all students in formatted table |
| ✏️ **Update Student** | Update any of 12 student fields through interactive menu |
| 🗑️ **Delete Student** | Remove student record with confirmation prompt |
| 🆔 **Auto-Generated IDs** | Student IDs generated automatically (Format: `YYDepartmentNNN`) |
| 🎯 **User-Friendly Console** | Menu-driven interface with input validation |
| 🔒 **MySQL Procedures** | All operations use stored procedures for security |
| ⚡ **Confirmation Prompts** | Delete operations require user confirmation |

---

## 🛠 Technologies Used

- ☕ **Java (JDK 8+)**
- 🐬 **MySQL Database**
- 🔌 **JDBC (Java Database Connectivity)**
- 📞 **CallableStatement (JDBC)** - To execute stored procedures
- 📦 **MySQL Stored Procedures** - All CRUD operations
- 🔄 **MySQL Triggers** - Auto ID generation
- 💻 **Console-based UI**

---

## 🗄 Database Setup

### 1️⃣ Create Database

```sql
CREATE DATABASE test;
USE test;
```

### 2️⃣ Create Students Table

```sql
CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    dob DATE,
    phone VARCHAR(15),
    email VARCHAR(100),
    address VARCHAR(200),
    admission_date DATE NOT NULL,
    department VARCHAR(50) NOT NULL,
    semester INT DEFAULT 1,
    division VARCHAR(5),
    parent_name VARCHAR(100),
    parent_phone VARCHAR(15)
);
```

**📊 Table Structure:**

| # | Column | Type | Attributes | Null | Default | Description |
|---|--------|------|------------|------|---------|-------------|
| 1 | `student_id` | VARCHAR(20) | PRIMARY KEY | No | None | Auto-generated unique ID (e.g., 25ComputerScience001) |
| 2 | `name` | VARCHAR(100) | NOT NULL | No | None | Student's full name |
| 3 | `gender` | VARCHAR(10) | - | Yes | NULL | Male/Female |
| 4 | `dob` | DATE | - | Yes | NULL | Date of birth |
| 5 | `phone` | VARCHAR(15) | - | Yes | NULL | Student contact number |
| 6 | `email` | VARCHAR(100) | - | Yes | NULL | Email address |
| 7 | `address` | VARCHAR(200) | - | Yes | NULL | Residential address |
| 8 | `admission_date` | DATE | NOT NULL | No | None | Date of admission |
| 9 | `department` | VARCHAR(50) | NOT NULL | No | None | Department name |
| 10 | `semester` | INT(11) | - | Yes | 1 | Current semester (1-8) |
| 11 | `division` | VARCHAR(5) | - | Yes | NULL | Division/Section (A, B, C) |
| 12 | `parent_name` | VARCHAR(100) | - | Yes | NULL | Parent/Guardian name |
| 13 | `parent_phone` | VARCHAR(15) | - | Yes | NULL | Parent contact number |

---

### 3️⃣ Create Trigger for Auto ID Generation

```sql
DELIMITER $$

CREATE TRIGGER generate_student_id
BEFORE INSERT ON students
FOR EACH ROW
BEGIN
    DECLARE yr VARCHAR(2);
    DECLARE deptName VARCHAR(50);
    DECLARE lastNo INT DEFAULT 0;
    DECLARE newNo INT;

    -- Last 2 digits of year (2025 → 25)
    SET yr = DATE_FORMAT(NEW.admission_date, '%y');

    -- Use department name directly
    SET deptName = NEW.department;

    -- Find last number used for same year + same department
    SELECT 
        IFNULL(MAX(CAST(SUBSTRING(student_id, LENGTH(yr) + LENGTH(deptName) + 1) AS UNSIGNED)), 0)
    INTO lastNo
    FROM students
    WHERE student_id LIKE CONCAT(yr, deptName, '%');

    -- Increase running number
    SET newNo = lastNo + 1;

    -- Final ID format → 25ComputerScience001
    SET NEW.student_id = CONCAT(yr, deptName, LPAD(newNo, 3, '0'));

END$$

DELIMITER ;
```

**🔍 How Trigger Works:**
- Extracts year from admission date (2025 → "25")
- Uses department name as prefix
- Finds highest existing number for that year+department
- Generates new ID: `YearDepartmentNumber`
- Example: `25ComputerScience001`, `25Physics002`

---

### 4️⃣ Create Stored Procedures

#### 📌 **1. Insert Student**

```sql
DELIMITER $$

CREATE PROCEDURE insert_student(
    IN p_name VARCHAR(100),
    IN p_gender VARCHAR(10),
    IN p_dob DATE,
    IN p_phone VARCHAR(15),
    IN p_email VARCHAR(100),
    IN p_address VARCHAR(200),
    IN p_admission_date DATE,
    IN p_department VARCHAR(50),
    IN p_semester INT,
    IN p_division VARCHAR(5),
    IN p_parent_name VARCHAR(100),
    IN p_parent_phone VARCHAR(15)
)
BEGIN
    -- Insert student (student_id is auto-created by trigger)
    INSERT INTO students(
        name, gender, dob, phone, email, address,
        admission_date, department, semester, division,
        parent_name, parent_phone
    ) VALUES (
        p_name, p_gender, p_dob, p_phone, p_email, p_address,
        p_admission_date, p_department, p_semester, p_division,
        p_parent_name, p_parent_phone
    );

    -- Return newly generated student_id (trigger generates it)
    SELECT student_id 
    FROM students 
    WHERE phone = p_phone 
    ORDER BY student_id DESC 
    LIMIT 1;
END$$

DELIMITER ;
```

---

#### 📌 **2. Get Student by ID**

```sql
DELIMITER $$

CREATE PROCEDURE get_student_by_id(IN p_student_id VARCHAR(20))
BEGIN
    SELECT * FROM students WHERE student_id = p_student_id;
END$$

DELIMITER ;
```

---

#### 📌 **3. Get All Students**

```sql
DELIMITER $$

CREATE PROCEDURE get_all_students()
BEGIN
    SELECT * FROM students;
END$$

DELIMITER ;
```

---

#### 📌 **4. Update Student Field**

```sql
DELIMITER $$

CREATE PROCEDURE update_student_field(
    IN p_student_id VARCHAR(20),
    IN p_column_name VARCHAR(50),
    IN p_new_value VARCHAR(255)
)
BEGIN
    SET @s = CONCAT('UPDATE students SET ', p_column_name, ' = ? WHERE student_id = ?');
    PREPARE stmt FROM @s;
    SET @val = p_new_value;
    SET @id = p_student_id;
    EXECUTE stmt USING @val, @id;
    DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;
```

---

#### 📌 **5. Delete Student by ID**

```sql
DELIMITER $$

CREATE PROCEDURE delete_student_by_id(IN p_student_id VARCHAR(20))
BEGIN
    DELETE FROM students WHERE student_id = p_student_id;
END$$

DELIMITER ;
```

---

## 📁 Project Structure

```
📦 Student-Management-System
├── 📄 Main.java              # Entry point with main menu
├── 📄 DbCon.java             # Database connection handler
├── 📄 NewStud.java           # Add new student
├── 📄 RetrieveStd.java       # View student records
├── 📄 UpdateStud.java        # Update student information
└── 📄 RemoveStud.java        # Delete student records
```

---

## 💻 How The System Works

### 🏠 **Main.java** - Application Entry Point

Creates database connection once and passes it to all operations. Displays main menu with input validation.

**Main Menu:**
```
============================= Student Manager =============================
1: New Student
2: View Student
3: Update Student
4: Remove Student
5: Exit

Enter you choice :: 
```

**🔍 Key Features:**
- ✅ **Input Validation**: Checks if user enters number using `scan.hasNextInt()`
- 🔗 **Single Connection**: Creates connection once and reuses it
- ♾️ **Continuous Loop**: Runs until user exits
- 🛡️ **Error Handling**: Try-catch block wraps entire application

**Input Validation Example:**
```java
if(!scan.hasNextInt()) {
    System.out.println("Invalid input! Please enter a NUMBER only.");
    scan.next();
    continue;
}
```

---

### 🔌 **DbCon.java** - Database Connection

Establishes MySQL connection with success message confirmation.

**Connection Configuration:**
- 🌐 URL: `jdbc:mysql://localhost:3306/test`
- 👤 Username: `root`
- 🔑 Password: `` (empty)

**🔍 Key Feature:**
```java
System.out.println("Database Connected Successfully!");
```
Displays confirmation message when connection is established.

---

### ➕ **NewStud.java** - Add New Student

**How it works:**
1. Takes 12 input fields from user
2. Calls `insert_student` stored procedure using `CallableStatement`
3. Procedure triggers auto ID generation
4. Returns and displays generated Student ID

**Input Fields (12):**
- 👤 Name
- ⚧ Gender (Male/Female)
- 📅 DOB (YYYY-MM-DD)
- 📱 Phone
- 📧 Email
- 🏠 Address
- 📆 Admission Date (YYYY-MM-DD)
- 🏢 Department
- 📚 Semester (Integer)
- 📑 Division
- 👨‍👩‍👦 Parent Name
- ☎️ Parent Phone

**Stored Procedure Call:**
```java
CallableStatement cstmt = con.prepareCall("{CALL insert_student(?,?,?,?,?,?,?,?,?,?,?,?)}");
cstmt.setString(1, name);
cstmt.setString(2, gender);
// ... sets all 12 parameters
boolean hasResult = cstmt.execute();
```

**Sample Output:**
```
Enter Name: John Smith
Enter Gender (Male/Female): Male
Enter DOB (YYYY-MM-DD): 2005-05-15
Enter Phone: 9876543210
Enter Email: john.smith@example.com
Enter Address: 123 Main Street
Enter Admission Date (YYYY-MM-DD): 2025-01-10
Enter Department: ComputerScience
Enter Semester: 1
Enter Division: A
Enter Parent Name: Robert Smith
Enter Parent Phone: 9123456789

🎉 Student Added Successfully!
Generated Student ID: 25ComputerScience001
```

---

### 🔍 **RetrieveStd.java** - View Students

**How it works:**
Shows a sub-menu with three options and loops until user returns to main menu.

**Retrieve Menu:**
```
1 :: Individual Student 
2 :: all Student 
3 :: Return to main menu 
Enter you retrieve choice :: 
```

---

#### 👤 **Individual Student (`indStd` method)**

- Takes Student ID as input
- Calls `get_student_by_id` procedure
- Displays formatted student details

**Procedure Call:**
```java
CallableStatement cs = con.prepareCall("{CALL get_student_by_id(?)}");
cs.setString(1, id);
ResultSet rs = cs.executeQuery();
```

**Sample Output:**
```
Enter Student ID :: 25ComputerScience001

=========== Student Details ===========
ID            : 25ComputerScience001
Name          : John Smith
Gender        : Male
DOB           : 2005-05-15
Phone         : 9876543210
Email         : john.smith@example.com
Address       : 123 Main Street
Admission Date: 2025-01-10
Department    : ComputerScience
Semester      : 1
Division      : A
Parent Name   : Robert Smith
Parent Phone  : 9123456789
=========================================
```

---

#### 📊 **All Students (`allStd` method)**

- Calls `get_all_students` procedure
- Displays all records in table format using `printf`

**Procedure Call:**
```java
CallableStatement cs = con.prepareCall("{CALL get_all_students()}");
ResultSet rs = cs.executeQuery();
```

**Sample Output:**
```
ID       | Name                | Gender | DOB         | Phone          | Email                     | Address        | Admission   | Department      | Sem | Div | Parent Name         | Parent Phone
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
25CS001  | John Smith          | Male   | 2005-05-15  | 9876543210     | john.smith@example.com    | 123 Main St    | 2025-01-10  | ComputerScience | 1   | A   | Robert Smith        | 9123456789
25Phy001 | Jane Doe            | Female | 2006-03-20  | 9988776655     | jane.doe@example.com      | 456 Park Ave   | 2025-01-10  | Physics         | 1   | B   | Mary Doe            | 9988776644
```

---

### ✏️ **UpdateStud.java** - Update Student Information

**How it works:**
1. Takes Student ID as input
2. Shows menu of 12 updatable fields
3. User selects field and enters new value
4. Calls `update_student_field` procedure with dynamic field name
5. Loops until user exits (choice 0)

**Update Menu:**
```
=== Update Menu ===
1. Name
2. Gender
3. DOB
4. Phone
5. Email
6. Address
7. Admission Date
8. Department
9. Semester
10. Division
11. Parent Name
12. Parent Phone
0. Exit
Choose field to update: 
```

**Dynamic Field Selection:**
```java
String column = "";
switch (choice) {
    case 1: column = "name"; break;
    case 2: column = "gender"; break;
    // ... all 12 fields
}
```

**Procedure Call:**
```java
CallableStatement cs = con.prepareCall("{CALL update_student_field(?, ?, ?)}");
cs.setString(1, id);           // Student ID
cs.setString(2, column);       // Field name
cs.setString(3, newValue);     // New value
cs.execute();
```

**Sample Output:**
```
Enter Student ID to update: 25ComputerScience001

=== Update Menu ===
1. Name
2. Gender
...
Choose field to update: 5
Enter new value for email: john.new@example.com

✅ email updated successfully!
```

---

### 🗑️ **RemoveStud.java** - Delete Student

**How it works:**
1. Takes Student ID as input
2. Asks for confirmation (Y/N)
3. If confirmed, calls `delete_student_by_id` procedure
4. Shows success/failure message

**Confirmation Prompt:**
```java
System.out.print("Are you sure you want to delete this student? (Y/N): ");
String confirm = scan.next();

if (!confirm.equalsIgnoreCase("Y")) {
    System.out.println("❌ Deletion cancelled.");
    return;
}
```

**Procedure Call:**
```java
CallableStatement cs = con.prepareCall("{CALL delete_student_by_id(?)}");
cs.setString(1, id);
int affectedRows = cs.executeUpdate();
```

**Sample Output:**
```
Enter Student ID to delete: 25ComputerScience001
Are you sure you want to delete this student? (Y/N): Y

✅ Student with ID 25ComputerScience001 deleted successfully!
```

**If ID doesn't exist:**
```
❌ No student found with ID: 25XYZ999
```

---

## 🎯 Key Technical Features

### 📞 **CallableStatement (JDBC) - Executing Stored Procedures**

This project uses **CallableStatement**, a JDBC interface specifically designed to call stored procedures in the database. Unlike PreparedStatement (used for regular SQL queries), CallableStatement allows calling pre-compiled database procedures.

**Why CallableStatement?**
- 🎯 **Executes Stored Procedures** - Designed specifically for calling database procedures
- 🛡️ **SQL Injection Prevention** - Parameters are safely bound
- ⚡ **Better Performance** - Procedures are pre-compiled in database
- 🔄 **Centralized Logic** - Database logic stays in database
- 📤 **Can Return Results** - Supports OUT parameters and ResultSets

**Basic Syntax:**
```java
// Prepare the procedure call
CallableStatement cs = connection.prepareCall("{CALL procedure_name(?, ?)}");

// Set input parameters
cs.setString(1, "value1");
cs.setInt(2, 123);

// Execute
cs.execute();              // For procedures that don't return data
ResultSet rs = cs.executeQuery();  // For procedures that return data
```

---

### 🔐 **How CallableStatement Works in This Project**

All 5 database operations use CallableStatement:

All 5 database operations use CallableStatement:

#### 1️⃣ **INSERT - Adding Student**
```java
// Prepare call with 12 parameters
CallableStatement cstmt = con.prepareCall("{CALL insert_student(?,?,?,?,?,?,?,?,?,?,?,?)}");

// Set all 12 student fields
cstmt.setString(1, name);
cstmt.setString(8, department);
cstmt.setInt(9, semester);
// ... (all parameters)

// Execute and get generated ID
boolean hasResult = cstmt.execute();
ResultSet rs = cstmt.getResultSet();
String studentId = rs.getString("student_id");
```

#### 2️⃣ **SELECT - Retrieving Single Student**
```java
CallableStatement cs = con.prepareCall("{CALL get_student_by_id(?)}");
cs.setString(1, id);
ResultSet rs = cs.executeQuery();
```

#### 3️⃣ **SELECT - Retrieving All Students**
```java
CallableStatement cs = con.prepareCall("{CALL get_all_students()}");
ResultSet rs = cs.executeQuery();
```

#### 4️⃣ **UPDATE - Updating Student Field**
```java
CallableStatement cs = con.prepareCall("{CALL update_student_field(?, ?, ?)}");
cs.setString(1, id);           // Student ID
cs.setString(2, column);       // Field name
cs.setString(3, newValue);     // New value
cs.execute();
```

#### 5️⃣ **DELETE - Removing Student**
```java
CallableStatement cs = con.prepareCall("{CALL delete_student_by_id(?)}");
cs.setString(1, id);
int affectedRows = cs.executeUpdate();
```

---

### 🔄 **CallableStatement Methods Used**

| Method | Usage | Returns |
|--------|-------|---------|
| `execute()` | General execution | boolean (true if ResultSet available) |
| `executeQuery()` | SELECT operations | ResultSet |
| `executeUpdate()` | INSERT/UPDATE/DELETE | int (affected rows count) |
| `getResultSet()` | Get results after execute() | ResultSet |

**Benefits:**
- 🛡️ SQL injection prevention
- ⚡ Better performance (pre-compiled)
- 🔄 Centralized database logic
- 🎯 Easy maintenance

---

### 🆔 **Auto-Generated Student IDs**

Student IDs are automatically generated by database trigger:

**ID Format:** `YearDepartmentNumber`

**Examples:**
- `25ComputerScience001` - First CS student in 2025
- `25Physics002` - Second Physics student in 2025
- `24Mathematics015` - 15th Math student in 2024

**How it works:**
1. Trigger fires BEFORE INSERT
2. Extracts year from admission_date
3. Uses department name as prefix
4. Finds last number for that year+dept
5. Generates new ID with zero-padded number

---

### ✅ **Input Validation**

**Main Menu Validation:**
```java
if(!scan.hasNextInt()) {
    System.out.println("Invalid input! Please enter a NUMBER only.");
    scan.next();
    continue;
}
```

**Delete Confirmation:**
```java
System.out.print("Are you sure? (Y/N): ");
String confirm = scan.next();
if (!confirm.equalsIgnoreCase("Y")) {
    System.out.println("❌ Deletion cancelled.");
    return;
}
```

---

### 🔄 **Buffer Management**

Proper Scanner buffer clearing:

```java
scan.nextLine(); // Clear buffer before reading strings
int semester = scan.nextInt();
scan.nextLine(); // Clear buffer after reading int
```

---

### 📊 **Formatted Output**

Uses `printf` and `String.format` for aligned display:

```java
System.out.printf("%-8s | %-20s | %-6s | %-10s\n",
    rs.getString("student_id"),
    rs.getString("name"),
    rs.getString("gender"),
    rs.getDate("dob"));
```

---

## 🚀 How to Run

### 📥 Prerequisites

1. ✅ Java JDK 8+
2. ✅ MySQL Server
3. ✅ MySQL Connector/J JAR

---

### ⚙️ Setup Steps

**1. Configure Database:**
```sql
-- Run all SQL commands from Database Setup section
-- Create database, table, trigger, and 5 procedures
```

**2. Update Connection in `DbCon.java`:**
```java
static String user = "your_username";
static String pass = "your_password";
```

**3. Compile:**
```bash
javac -cp .:mysql-connector-java-x.x.x.jar *.java
```

**4. Run:**
```bash
java -cp .:mysql-connector-java-x.x.x.jar Main
```

*Note: On Windows, replace `:` with `;` in classpath*

---

## 📸 Complete Workflow Example

```
Database Connected Successfully!

============================= Student Manager =============================
1: New Student
2: View Student
3: Update Student
4: Remove Student
5: Exit

Enter you choice :: 1

Enter Name: Alice Johnson
Enter Gender (Male/Female): Female
Enter DOB (YYYY-MM-DD): 2006-08-25
Enter Phone: 8765432109
Enter Email: alice.j@university.edu
Enter Address: 789 College Road
Enter Admission Date (YYYY-MM-DD): 2025-01-15
Enter Department: Physics
Enter Semester: 1
Enter Division: B
Enter Parent Name: Michael Johnson
Enter Parent Phone: 8765432100

🎉 Student Added Successfully!
Generated Student ID: 25Physics001

============================= Student Manager =============================
1: New Student
2: View Student
3: Update Student
4: Remove Student
5: Exit

Enter you choice :: 2

1 :: Individual Student 
2 :: all Student 
3 :: Return to main menu 
Enter you retrieve choice :: 1

Enter Student ID :: 25Physics001

=========== Student Details ===========
ID            : 25Physics001
Name          : Alice Johnson
Gender        : Female
DOB           : 2006-08-25
Phone         : 8765432109
Email         : alice.j@university.edu
Address       : 789 College Road
Admission Date: 2025-01-15
Department    : Physics
Semester      : 1
Division      : B
Parent Name   : Michael Johnson
Parent Phone  : 8765432100
=========================================

============================= Student Manager =============================
1: New Student
2: View Student
3: Update Student
4: Remove Student
5: Exit

Enter you choice :: 5

Bye Bye 👋👋....
```

---

## 🎓 Learning Outcomes

This project demonstrates:

- ☕ **Java Programming** - Scanner, Switch statements, Static methods
- 🗄️ **MySQL Database** - Tables, Triggers, Stored Procedures
- 🔌 **JDBC** - CallableStatement for stored procedures
- 🔐 **Database Security** - Parameterized procedures
- 🆔 **Auto ID Generation** - Custom ID format with triggers
- 💻 **Console UI** - Menu-driven interface design
- ✅ **Input Validation** - Type checking and confirmations
- 🔄 **Exception Handling** - Try-catch blocks
- 📊 **Formatted Output** - Table display with printf

---

## 📋 Database Procedures Summary

| # | Procedure Name | Purpose | Parameters |
|---|----------------|---------|------------|
| 1 | `insert_student` | Add new student | 12 IN parameters (all student fields) |
| 2 | `get_student_by_id` | Get single student | 1 IN (student_id) |
| 3 | `get_all_students` | Get all students | None |
| 4 | `update_student_field` | Update any field | 3 IN (id, column_name, new_value) |
| 5 | `delete_student_by_id` | Delete student | 1 IN (student_id) |

**Plus 1 Trigger:**
- `generate_student_id` - Automatically creates unique student IDs

---
