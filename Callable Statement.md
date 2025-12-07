# 📞 CallableStatement in JDBC - Complete Guide

A comprehensive guide to understanding **CallableStatement**, MySQL **Stored Procedures**, and **Triggers** with practical examples from the Student Management System.

---

## 🤔 What is CallableStatement?

**CallableStatement** is a JDBC interface that extends `PreparedStatement` and is specifically designed to **execute stored procedures** in a database.

### 🎯 Key Characteristics

- 📞 **Calls Database Procedures** - Executes pre-compiled procedures stored in the database
- 🔒 **Secure** - Prevents SQL injection through parameterized calls
- ⚡ **Efficient** - Procedures are pre-compiled in the database
- 📤 **Supports IN/OUT Parameters** - Can send and receive data
- 🔄 **Returns Multiple Results** - Can return ResultSets, output parameters, and update counts

### 📦 Interface Hierarchy

```
java.sql.Statement
    ↓
java.sql.PreparedStatement
    ↓
java.sql.CallableStatement
```

---

## 💡 Why Use CallableStatement?

### ✅ Benefits

| Benefit | Description |
|---------|-------------|
| 🛡️ **Security** | Prevents SQL injection attacks |
| ⚡ **Performance** | Pre-compiled procedures execute faster |
| 🔄 **Reusability** | Same procedure can be called from multiple applications |
| 🎯 **Maintainability** | Business logic centralized in database |
| 📊 **Complex Operations** | Can handle complex multi-step operations |
| 🔐 **Access Control** | Database-level permissions can be enforced |
| 🔀 **Transaction Management** | Procedures can manage transactions internally |
| 📈 **Network Efficiency** | Reduces client-server communication |

### 🎪 Real-World Use Cases

- ✅ Banking transactions (transfer money between accounts)
- ✅ Inventory management (update stock across multiple tables)
- ✅ Report generation (complex data aggregation)
- ✅ Data validation and cleanup
- ✅ Automated ID generation
- ✅ Batch operations

---

## 🔄 CallableStatement vs Other JDBC Statements

### 📊 Comparison Table

| Feature | Statement | PreparedStatement | CallableStatement |
|---------|-----------|-------------------|-------------------|
| **Purpose** | Execute simple SQL | Execute parameterized SQL | Execute stored procedures |
| **SQL Injection Protection** | ❌ No | ✅ Yes | ✅ Yes |
| **Pre-compilation** | ❌ No | ✅ Yes | ✅ Yes (in DB) |
| **Parameters** | ❌ No | ✅ IN only | ✅ IN, OUT, INOUT |
| **Performance** | 🐌 Slowest | ⚡ Fast | ⚡⚡ Fastest |
| **Code Reusability** | ❌ Low | ✅ Medium | ✅✅ High |
| **Use Case** | One-time queries | Repeated queries | Database procedures |

### 💻 Code Examples

#### ❌ **Statement (Not Recommended)**
```java
Statement stmt = con.createStatement();
String sql = "INSERT INTO students VALUES ('" + name + "')"; // SQL Injection risk!
stmt.executeUpdate(sql);
```

#### ✅ **PreparedStatement (Good for regular SQL)**
```java
PreparedStatement ps = con.prepareStatement("INSERT INTO students VALUES (?, ?, ?)");
ps.setString(1, name);
ps.setString(2, email);
ps.setInt(3, age);
ps.executeUpdate();
```

#### ⭐ **CallableStatement (Best for procedures)**
```java
CallableStatement cs = con.prepareCall("{CALL insert_student(?, ?, ?)}");
cs.setString(1, name);
cs.setString(2, email);
cs.setInt(3, age);
cs.execute();
```

---

## 🗄️ MySQL Stored Procedures

### 📖 What is a Stored Procedure?

A **stored procedure** is a pre-compiled collection of SQL statements stored in the database that can be executed as a single unit.

### 🎯 Key Features

- 📦 **Encapsulation** - Groups related SQL statements together
- 🔄 **Reusable** - Call from multiple applications
- ⚡ **Pre-compiled** - Executed faster than dynamic SQL
- 🎛️ **Parameters** - Accepts IN, OUT, and INOUT parameters
- 🔀 **Control Flow** - Supports IF, WHILE, LOOP statements
- 📊 **Returns Data** - Can return ResultSets and output parameters

### 📝 Basic Syntax

```sql
DELIMITER $$

CREATE PROCEDURE procedure_name(
    IN param1 datatype,
    OUT param2 datatype,
    INOUT param3 datatype
)
BEGIN
    -- SQL statements
    SELECT * FROM table WHERE column = param1;
END$$

DELIMITER ;
```

---

## 🔔 MySQL Triggers

### 📖 What is a Trigger?

A **trigger** is a database object that **automatically executes** when a specific event occurs on a table (INSERT, UPDATE, DELETE).

### 🎯 Key Features

- ⚡ **Automatic Execution** - Fires without explicit call
- 🕐 **Timing** - Can execute BEFORE or AFTER the event
- 🎪 **Event-Driven** - Responds to INSERT, UPDATE, or DELETE
- 🔄 **Data Modification** - Can modify data before insertion (BEFORE trigger)
- ✅ **Validation** - Can enforce business rules
- 📊 **Audit Trail** - Can log changes to tables

### 📝 Trigger Types

| Timing | Event | When It Fires |
|--------|-------|---------------|
| **BEFORE INSERT** | Before row insertion | Can modify NEW values |
| **AFTER INSERT** | After row insertion | Can't modify NEW values |
| **BEFORE UPDATE** | Before row update | Can modify NEW values |
| **AFTER UPDATE** | After row update | Can access OLD and NEW values |
| **BEFORE DELETE** | Before row deletion | Can access OLD values |
| **AFTER DELETE** | After row deletion | Can access OLD values |

### 📝 Basic Syntax

```sql
DELIMITER $$

CREATE TRIGGER trigger_name
BEFORE INSERT ON table_name
FOR EACH ROW
BEGIN
    -- Can access NEW.column_name
    -- Can modify NEW.column_name
    SET NEW.column = value;
END$$

DELIMITER ;
```

---

## 🛠️ Creating Stored Procedures

### 📌 Example 1: Simple INSERT Procedure

```sql
DELIMITER $$

CREATE PROCEDURE insert_student(
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(15)
)
BEGIN
    INSERT INTO students(name, email, phone) 
    VALUES (p_name, p_email, p_phone);
END$$

DELIMITER ;
```

**🔍 Explanation:**
- `DELIMITER $$` - Changes delimiter from `;` to `$$` (needed for procedure body)
- `IN p_name` - Input parameter
- `BEGIN...END` - Procedure body
- `DELIMITER ;` - Restores default delimiter

---

### 📌 Example 2: SELECT with Single Result

```sql
DELIMITER $$

CREATE PROCEDURE get_student_by_id(
    IN p_student_id VARCHAR(20)
)
BEGIN
    SELECT * FROM students 
    WHERE student_id = p_student_id;
END$$

DELIMITER ;
```

**🔍 Explanation:**
- Takes student ID as input
- Returns entire row as ResultSet
- Can be called from Java using `executeQuery()`

---

### 📌 Example 3: SELECT All Records

```sql
DELIMITER $$

CREATE PROCEDURE get_all_students()
BEGIN
    SELECT * FROM students;
END$$

DELIMITER ;
```

**🔍 Explanation:**
- No parameters needed
- Returns all student records
- Simple procedure without input

---

### 📌 Example 4: Dynamic UPDATE Procedure

```sql
DELIMITER $$

CREATE PROCEDURE update_student_field(
    IN p_student_id VARCHAR(20),
    IN p_column_name VARCHAR(50),
    IN p_new_value VARCHAR(255)
)
BEGIN
    -- Dynamic SQL using PREPARE
    SET @s = CONCAT('UPDATE students SET ', p_column_name, ' = ? WHERE student_id = ?');
    PREPARE stmt FROM @s;
    SET @val = p_new_value;
    SET @id = p_student_id;
    EXECUTE stmt USING @val, @id;
    DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;
```

**🔍 Explanation:**
- Uses **dynamic SQL** to build UPDATE query at runtime
- `CONCAT` - Builds SQL string with column name
- `PREPARE` - Prepares the statement
- `EXECUTE USING` - Executes with parameters
- `DEALLOCATE` - Frees the prepared statement

---

### 📌 Example 5: DELETE Procedure

```sql
DELIMITER $$

CREATE PROCEDURE delete_student_by_id(
    IN p_student_id VARCHAR(20)
)
BEGIN
    DELETE FROM students 
    WHERE student_id = p_student_id;
END$$

DELIMITER ;
```

**🔍 Explanation:**
- Simple DELETE operation
- Takes student ID as input
- Returns number of affected rows

---

### 📌 Example 6: INSERT with Return Value

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

    -- Return newly generated student_id
    SELECT student_id 
    FROM students 
    WHERE phone = p_phone 
    ORDER BY student_id DESC 
    LIMIT 1;
END$$

DELIMITER ;
```

**🔍 Explanation:**
- Takes 12 input parameters
- Inserts student record
- Trigger generates student_id automatically
- Returns the generated ID using SELECT

---

## 🔔 Creating Triggers

### 📌 Auto-Generate Student ID Trigger

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

**🔍 Step-by-Step Explanation:**

1. **DECLARE Variables**
   ```sql
   DECLARE yr VARCHAR(2);
   DECLARE deptName VARCHAR(50);
   DECLARE lastNo INT DEFAULT 0;
   DECLARE newNo INT;
   ```
    - Creates temporary variables for processing

2. **Extract Year (2025 → "25")**
   ```sql
   SET yr = DATE_FORMAT(NEW.admission_date, '%y');
   ```
    - `NEW.admission_date` - The admission date being inserted
    - `DATE_FORMAT(..., '%y')` - Gets last 2 digits of year

3. **Get Department Name**
   ```sql
   SET deptName = NEW.department;
   ```
    - `NEW.department` - The department being inserted

4. **Find Last Number Used**
   ```sql
   SELECT 
       IFNULL(MAX(CAST(SUBSTRING(student_id, ...) AS UNSIGNED)), 0)
   INTO lastNo
   FROM students
   WHERE student_id LIKE CONCAT(yr, deptName, '%');
   ```
    - Finds highest number for same year+department combination
    - `SUBSTRING` - Extracts the number part from ID
    - `CAST(...AS UNSIGNED)` - Converts to integer
    - `MAX` - Gets highest number
    - `IFNULL(..., 0)` - Returns 0 if no records exist

5. **Increment Number**
   ```sql
   SET newNo = lastNo + 1;
   ```

6. **Generate Final ID**
   ```sql
   SET NEW.student_id = CONCAT(yr, deptName, LPAD(newNo, 3, '0'));
   ```
    - `CONCAT` - Joins year + department + number
    - `LPAD(newNo, 3, '0')` - Pads number to 3 digits with zeros
    - Example: `25` + `ComputerScience` + `001` = `25ComputerScience001`

**📊 ID Format Examples:**

| Admission Year | Department | Sequence | Generated ID |
|----------------|------------|----------|--------------|
| 2025 | ComputerScience | 1 | 25ComputerScience001 |
| 2025 | ComputerScience | 2 | 25ComputerScience002 |
| 2025 | Physics | 1 | 25Physics001 |
| 2024 | Mathematics | 15 | 24Mathematics015 |

---

## 💻 Using CallableStatement in Java

### 🔌 Step 1: Establish Connection

```java
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/test",
    "root",
    "password"
);
```

---

### 📞 Step 2: Prepare CallableStatement

**Syntax:**
```java
CallableStatement cs = connection.prepareCall("{CALL procedure_name(?, ?)}");
```

**🔍 Components:**
- `{CALL procedure_name(...)}` - Standard JDBC escape syntax
- `?` - Parameter placeholders
- Number of `?` must match procedure parameters

---

### 🎯 Step 3: Set Parameters

```java
cs.setString(1, "John Doe");      // First parameter
cs.setInt(2, 25);                 // Second parameter
cs.setDate(3, new java.sql.Date(System.currentTimeMillis()));
```

**📋 Common Setter Methods:**

| Method | SQL Type | Java Type |
|--------|----------|-----------|
| `setString(index, value)` | VARCHAR, TEXT | String |
| `setInt(index, value)` | INT | int |
| `setLong(index, value)` | BIGINT | long |
| `setDouble(index, value)` | DOUBLE | double |
| `setDate(index, value)` | DATE | java.sql.Date |
| `setBoolean(index, value)` | BOOLEAN | boolean |

---

### ⚡ Step 4: Execute

**Three Execution Methods:**

#### 1️⃣ **execute()** - General purpose
```java
boolean hasResults = cs.execute();
if (hasResults) {
    ResultSet rs = cs.getResultSet();
}
```
- Returns `true` if first result is a ResultSet
- Returns `false` if it's an update count
- Use `getResultSet()` to get the ResultSet

#### 2️⃣ **executeQuery()** - For SELECT
```java
ResultSet rs = cs.executeQuery();
while (rs.next()) {
    String name = rs.getString("name");
}
```
- Returns ResultSet directly
- Use for procedures that return data

#### 3️⃣ **executeUpdate()** - For INSERT/UPDATE/DELETE
```java
int rowsAffected = cs.executeUpdate();
System.out.println(rowsAffected + " rows affected");
```
- Returns number of rows affected
- Use for procedures that modify data

---

## 🎪 Real-World Examples from Student Management System

### 📌 Example 1: Adding New Student

**Java Code (NewStud.java):**
```java
// Prepare call with 12 parameters
CallableStatement cstmt = con.prepareCall(
    "{CALL insert_student(?,?,?,?,?,?,?,?,?,?,?,?)}"
);

// Set all parameters
cstmt.setString(1, "John Smith");           // name
cstmt.setString(2, "Male");                 // gender
cstmt.setString(3, "2005-05-15");          // dob
cstmt.setString(4, "9876543210");          // phone
cstmt.setString(5, "john@example.com");    // email
cstmt.setString(6, "123 Main St");         // address
cstmt.setString(7, "2025-01-10");          // admission_date
cstmt.setString(8, "ComputerScience");     // department
cstmt.setInt(9, 1);                        // semester
cstmt.setString(10, "A");                  // division
cstmt.setString(11, "Robert Smith");       // parent_name
cstmt.setString(12, "9123456789");         // parent_phone

// Execute and get generated ID
boolean hasResult = cstmt.execute();
if (hasResult) {
    ResultSet rs = cstmt.getResultSet();
    if (rs.next()) {
        String studentId = rs.getString("student_id");
        System.out.println("Generated Student ID: " + studentId);
    }
}
```

**📊 What Happens:**
1. Java calls `insert_student` procedure
2. Procedure inserts data into students table
3. **Trigger fires** automatically and generates student_id
4. Procedure returns the generated ID
5. Java displays: `Generated Student ID: 25ComputerScience001`

---

### 📌 Example 2: Retrieving Student by ID

**Java Code (RetrieveStd.java):**
```java
CallableStatement cs = con.prepareCall("{CALL get_student_by_id(?)}");
cs.setString(1, "25ComputerScience001");

ResultSet rs = cs.executeQuery();

if (rs.next()) {
    System.out.println("ID: " + rs.getString("student_id"));
    System.out.println("Name: " + rs.getString("name"));
    System.out.println("Email: " + rs.getString("email"));
    System.out.println("Phone: " + rs.getString("phone"));
    // ... all other fields
}
```

**MySQL Procedure:**
```sql
CREATE PROCEDURE get_student_by_id(IN p_student_id VARCHAR(20))
BEGIN
    SELECT * FROM students WHERE student_id = p_student_id;
END
```

---

### 📌 Example 3: Retrieving All Students

**Java Code (RetrieveStd.java):**
```java
CallableStatement cs = con.prepareCall("{CALL get_all_students()}");
ResultSet rs = cs.executeQuery();

while (rs.next()) {
    System.out.printf("%-10s | %-20s | %-25s\n",
        rs.getString("student_id"),
        rs.getString("name"),
        rs.getString("email"));
}
```

**MySQL Procedure:**
```sql
CREATE PROCEDURE get_all_students()
BEGIN
    SELECT * FROM students;
END
```

---

### 📌 Example 4: Updating Student Field

**Java Code (UpdateStud.java):**
```java
CallableStatement cs = con.prepareCall("{CALL update_student_field(?, ?, ?)}");
cs.setString(1, "25ComputerScience001");   // student_id
cs.setString(2, "email");                  // column_name
cs.setString(3, "newemail@example.com");   // new_value

cs.execute();
System.out.println("Email updated successfully!");
```

**MySQL Procedure:**
```sql
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
END
```

---

### 📌 Example 5: Deleting Student

**Java Code (RemoveStud.java):**
```java
CallableStatement cs = con.prepareCall("{CALL delete_student_by_id(?)}");
cs.setString(1, "25ComputerScience001");

int affectedRows = cs.executeUpdate();
if (affectedRows > 0) {
    System.out.println("Student deleted successfully!");
} else {
    System.out.println("Student not found!");
}
```

**MySQL Procedure:**
```sql
CREATE PROCEDURE delete_student_by_id(IN p_student_id VARCHAR(20))
BEGIN
    DELETE FROM students WHERE student_id = p_student_id;
END
```

---

## 🎯 Best Practices

### ✅ Do's

| Practice | Reason |
|----------|--------|
| ✅ **Use try-with-resources** | Automatically closes resources |
| ✅ **Validate input data** | Prevent invalid data in database |
| ✅ **Use descriptive procedure names** | Easy to understand purpose |
| ✅ **Handle exceptions properly** | Graceful error management |
| ✅ **Close resources** | Prevent memory leaks |
| ✅ **Use transactions** | Ensure data consistency |
| ✅ **Document procedures** | Add comments explaining logic |
| ✅ **Test procedures independently** | Debug easily in database first |

---

### ❌ Don'ts

| Practice | Reason |
|----------|--------|
| ❌ **Don't concatenate SQL in Java** | SQL injection risk |
| ❌ **Don't ignore exceptions** | Silent failures are dangerous |
| ❌ **Don't hardcode credentials** | Security risk |
| ❌ **Don't forget to close resources** | Memory leaks |
| ❌ **Don't use SELECT * in production** | Performance impact |
| ❌ **Don't create procedures without error handling** | Difficult to debug |

---

### 💡 Code Example: Best Practices

```java
// ✅ Good: try-with-resources automatically closes
try (Connection con = DbConnection.getConnection();
     CallableStatement cs = con.prepareCall("{CALL insert_student(?,?,?)}")) {
    
    // Validate input
    if (name == null || name.trim().isEmpty()) {
        System.out.println("Name cannot be empty");
        return;
    }
    
    cs.setString(1, name);
    cs.setString(2, email);
    cs.setString(3, phone);
    
    cs.execute();
    System.out.println("Student added successfully!");
    
} catch (SQLException e) {
    System.err.println("Database error: " + e.getMessage());
    e.printStackTrace();
}
```

---

## 🎓 Summary

### 🔑 Key Takeaways

| Concept | Summary |
|---------|---------|
| **CallableStatement** | JDBC interface to execute stored procedures |
| **Stored Procedures** | Pre-compiled SQL code stored in database |
| **Triggers** | Automatic execution on INSERT/UPDATE/DELETE |
| **Benefits** | Security, Performance, Reusability, Maintainability |
| **Use Cases** | Complex operations, transaction management, auto ID generation |

---

## 📚 CallableStatement Methods Reference

### 🎯 Core Execution Methods

#### 1️⃣ **execute()** Method

```java
boolean hasResults = callableStatement.execute();
```

**📖 Description:**
- General-purpose execution method
- Returns `true` if first result is a ResultSet
- Returns `false` if first result is an update count or no results

**🔍 Return Value:**
- `true` → Use `getResultSet()` to retrieve data
- `false` → Use `getUpdateCount()` to get affected rows

**💡 When to Use:**
- When you're not sure what the procedure returns
- When procedure might return multiple results
- When procedure returns ResultSet after performing updates

**Example:**
```java
CallableStatement cs = con.prepareCall("{CALL insert_student(?,?,?)}");
cs.setString(1, "John");
cs.setString(2, "john@email.com");
cs.setString(3, "1234567890");

boolean hasResults = cs.execute();

if (hasResults) {
    ResultSet rs = cs.getResultSet();
    if (rs.next()) {
        String id = rs.getString("student_id");
        System.out.println("Generated ID: " + id);
    }
} else {
    int count = cs.getUpdateCount();
    System.out.println("Rows affected: " + count);
}
```

---

#### 2️⃣ **executeQuery()** Method

```java
ResultSet rs = callableStatement.executeQuery();
```

**📖 Description:**
- Executes procedures that **return data** (SELECT operations)
- Returns ResultSet directly
- Throws exception if procedure doesn't return ResultSet

**🔍 Return Value:**
- `ResultSet` object containing the query results

**💡 When to Use:**
- When procedure only performs SELECT operations
- When you know procedure returns data
- For retrieving records from database

**Example:**
```java
// Get student by ID
CallableStatement cs = con.prepareCall("{CALL get_student_by_id(?)}");
cs.setString(1, "25ComputerScience001");

ResultSet rs = cs.executeQuery();

if (rs.next()) {
    System.out.println("Name: " + rs.getString("name"));
    System.out.println("Email: " + rs.getString("email"));
    System.out.println("Phone: " + rs.getString("phone"));
}
```

```java
// Get all students
CallableStatement cs = con.prepareCall("{CALL get_all_students()}");
ResultSet rs = cs.executeQuery();

while (rs.next()) {
    System.out.printf("%-10s | %-20s | %-25s\n",
        rs.getString("student_id"),
        rs.getString("name"),
        rs.getString("email"));
}
```

---

#### 3️⃣ **executeUpdate()** Method

```java
int rowsAffected = callableStatement.executeUpdate();
```

**📖 Description:**
- Executes procedures that **modify data** (INSERT/UPDATE/DELETE)
- Returns number of rows affected
- Does not return ResultSet

**🔍 Return Value:**
- `int` - Number of rows inserted, updated, or deleted
- `0` if no rows affected

**💡 When to Use:**
- INSERT operations (adding records)
- UPDATE operations (modifying records)
- DELETE operations (removing records)
- When you need to know how many rows were affected

**Example:**
```java
// Delete student
CallableStatement cs = con.prepareCall("{CALL delete_student_by_id(?)}");
cs.setString(1, "25ComputerScience001");

int affectedRows = cs.executeUpdate();

if (affectedRows > 0) {
    System.out.println("✅ Student deleted successfully!");
    System.out.println("Rows deleted: " + affectedRows);
} else {
    System.out.println("❌ Student not found!");
}
```

```java
// Update student field
CallableStatement cs = con.prepareCall("{CALL update_student_field(?, ?, ?)}");
cs.setString(1, "25ComputerScience001");
cs.setString(2, "email");
cs.setString(3, "newemail@example.com");

int rowsUpdated = cs.executeUpdate();
System.out.println("Rows updated: " + rowsUpdated);
```

---

### 📊 Method Comparison Table

| Method | Returns | Use For | Example Scenario |
|--------|---------|---------|------------------|
| `execute()` | `boolean` | Any procedure, uncertain output | Insert + return generated ID |
| `executeQuery()` | `ResultSet` | SELECT operations | Retrieve student records |
| `executeUpdate()` | `int` | INSERT/UPDATE/DELETE | Delete student, update email |

---

## 🔧 Parameter Setting Methods

### 📝 String Parameters

```java
cs.setString(int parameterIndex, String value);
```

**Example:**
```java
cs.setString(1, "John Doe");          // name
cs.setString(2, "john@example.com");  // email
```

---

### 🔢 Numeric Parameters

```java
cs.setInt(int parameterIndex, int value);
cs.setLong(int parameterIndex, long value);
cs.setDouble(int parameterIndex, double value);
```

**Example:**
```java
cs.setInt(1, 25);              // age
cs.setLong(2, 9876543210L);    // phone number
cs.setDouble(3, 50000.50);     // salary
```

---

### 📅 Date/Time Parameters

```java
cs.setDate(int parameterIndex, java.sql.Date value);
cs.setTime(int parameterIndex, java.sql.Time value);
cs.setTimestamp(int parameterIndex, java.sql.Timestamp value);
```

**Example:**
```java
// Using SQL Date
java.sql.Date sqlDate = java.sql.Date.valueOf("2005-05-15");
cs.setDate(1, sqlDate);

// Using current date
java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
cs.setDate(2, today);
```

---

### ✅ Boolean Parameters

```java
cs.setBoolean(int parameterIndex, boolean value);
```

**Example:**
```java
cs.setBoolean(1, true);   // is_active
```

---

### 🔤 Other Parameter Methods

| Method | SQL Type | Description |
|--------|----------|-------------|
| `setBytes()` | BLOB, BINARY | Binary data |
| `setObject()` | ANY | Generic object |
| `setNull()` | NULL | Set parameter to NULL |
| `setBigDecimal()` | DECIMAL | Precise decimal numbers |

---

## 🔄 ResultSet Retrieval Methods

### 📖 Getting Data from ResultSet

After executing `executeQuery()` or `execute()`, use these methods to retrieve data:

```java
ResultSet rs = cs.executeQuery();

while (rs.next()) {
    // String data
    String name = rs.getString("name");
    String email = rs.getString("email");
    
    // Numeric data
    int semester = rs.getInt("semester");
    long phone = rs.getLong("phone");
    
    // Date data
    java.sql.Date dob = rs.getDate("dob");
    
    // By column index (starts at 1)
    String id = rs.getString(1);
    String name = rs.getString(2);
}
```

### 📊 Common ResultSet Methods

| Method | Returns | Description |
|--------|---------|-------------|
| `next()` | `boolean` | Move to next row, returns false if no more rows |
| `getString(column)` | `String` | Get string value |
| `getInt(column)` | `int` | Get integer value |
| `getLong(column)` | `long` | Get long value |
| `getDate(column)` | `java.sql.Date` | Get date value |
| `getDouble(column)` | `double` | Get double value |
| `getBoolean(column)` | `boolean` | Get boolean value |

---

## 🔄 Additional CallableStatement Methods

### 📌 **registerOutParameter()** - For OUT Parameters

```java
CallableStatement cs = con.prepareCall("{CALL procedure_with_out(?)}");
cs.registerOutParameter(1, Types.INTEGER);
cs.execute();
int result = cs.getInt(1);
```

**📖 Description:**
- Used when procedure has OUT or INOUT parameters
- Must be called before executing the procedure
- Specifies the SQL type of the OUT parameter

---

### 📌 **setObject()** - Generic Parameter Setting

```java
cs.setObject(1, value);
cs.setObject(2, value, SQLType);
```

**📖 Description:**
- Can set any type of parameter
- Java automatically converts to appropriate SQL type
- Useful when type is not known at compile time

---

### 📌 **getResultSet()** - Get Current ResultSet

```java
ResultSet rs = cs.getResultSet();
```

**📖 Description:**
- Retrieves current ResultSet after `execute()`
- Used when procedure returns multiple results
- Call after checking if `execute()` returned true

---

### 📌 **getUpdateCount()** - Get Affected Rows

```java
int count = cs.getUpdateCount();
```

**📖 Description:**
- Returns number of rows affected by update/insert/delete
- Returns -1 if there are no more results
- Used with `execute()` when result is not a ResultSet

---

### 📌 **getMoreResults()** - Multiple Results

```java
boolean hasMore = cs.getMoreResults();
```

**📖 Description:**
- Moves to next result (if procedure returns multiple results)
- Returns true if next result is a ResultSet
- Returns false if next result is an update count or no more results

---

### 📌 **close()** - Close Statement

```java
cs.close();
```

**📖 Description:**
- Releases database and JDBC resources
- Should be called in finally block or use try-with-resources
- Automatically closes associated ResultSet

---

## 🎯 Complete Method Usage Examples

### 📌 Example 1: Using execute() with Multiple Operations

```java
CallableStatement cs = con.prepareCall("{CALL complex_procedure(?)}");
cs.setString(1, "value");

boolean isResultSet = cs.execute();

// Process first result
if (isResultSet) {
    ResultSet rs = cs.getResultSet();
    while (rs.next()) {
        System.out.println(rs.getString(1));
    }
} else {
    int count = cs.getUpdateCount();
    System.out.println("Updated: " + count);
}

// Check for more results
while (cs.getMoreResults() || cs.getUpdateCount() != -1) {
    if (cs.getResultSet() != null) {
        ResultSet rs = cs.getResultSet();
        // Process next ResultSet
    } else {
        int count = cs.getUpdateCount();
        System.out.println("More updates: " + count);
    }
}
```

---

### 📌 Example 2: Using executeQuery() for Data Retrieval

```java
CallableStatement cs = con.prepareCall("{CALL get_all_students()}");
ResultSet rs = cs.executeQuery();

System.out.println("All Students:");
System.out.println("------------------------------------");

while (rs.next()) {
    String id = rs.getString("student_id");
    String name = rs.getString("name");
    String email = rs.getString("email");
    int semester = rs.getInt("semester");
    
    System.out.printf("%-15s | %-20s | %-25s | %d\n", 
        id, name, email, semester);
}

rs.close();
cs.close();
```

---

### 📌 Example 3: Using executeUpdate() for Modifications

```java
CallableStatement cs = con.prepareCall("{CALL delete_old_records(?)}");
cs.setDate(1, java.sql.Date.valueOf("2020-01-01"));

int deletedRows = cs.executeUpdate();

System.out.println("Deleted " + deletedRows + " old records");

if (deletedRows > 0) {
    System.out.println("✅ Cleanup successful!");
} else {
    System.out.println("ℹ️ No old records found");
}

cs.close();
```

---

### 📌 Example 4: Try-With-Resources (Best Practice)

```java
// Automatically closes resources
try (Connection con = DbConnection.getConnection();
     CallableStatement cs = con.prepareCall("{CALL get_student_by_id(?)}")) {
    
    cs.setString(1, "25ComputerScience001");
    
    try (ResultSet rs = cs.executeQuery()) {
        if (rs.next()) {
            System.out.println("Student Found!");
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Email: " + rs.getString("email"));
        } else {
            System.out.println("Student not found");
        }
    }
    
} catch (SQLException e) {
    System.err.println("Error: " + e.getMessage());
    e.printStackTrace();
}
// Resources automatically closed here
```

---

### 📌 Example 5: Batch Execution

```java
CallableStatement cs = con.prepareCall("{CALL insert_student(?,?,?)}");

// Add multiple students in batch
String[][] students = {
    {"John Doe", "john@email.com", "1111111111"},
    {"Jane Smith", "jane@email.com", "2222222222"},
    {"Bob Johnson", "bob@email.com", "3333333333"}
};

for (String[] student : students) {
    cs.setString(1, student[0]);
    cs.setString(2, student[1]);
    cs.setString(3, student[2]);
    cs.addBatch();
}

int[] results = cs.executeBatch();
System.out.println("Inserted " + results.length + " students");
```

---

## 📚 Method Hierarchy

```
CallableStatement (Interface)
    ↓ extends
PreparedStatement (Interface)
    ↓ extends
Statement (Interface)
```

### 📋 Inherited from Statement

- `execute(String sql)` - Execute any SQL
- `executeQuery(String sql)` - Execute SELECT
- `executeUpdate(String sql)` - Execute INSERT/UPDATE/DELETE
- `close()` - Close statement
- `isClosed()` - Check if closed
- `getResultSet()` - Get current ResultSet
- `getUpdateCount()` - Get affected rows
- `getMoreResults()` - Move to next result

### 📋 Inherited from PreparedStatement

- `setString(int, String)` - Set string parameter
- `setInt(int, int)` - Set integer parameter
- `setLong(int, long)` - Set long parameter
- `setDate(int, Date)` - Set date parameter
- `setBoolean(int, boolean)` - Set boolean parameter
- `setObject(int, Object)` - Set any object
- `setNull(int, int)` - Set NULL value
- `execute()` - Execute prepared statement
- `executeQuery()` - Execute SELECT
- `executeUpdate()` - Execute UPDATE/INSERT/DELETE
- `addBatch()` - Add to batch
- `executeBatch()` - Execute batch

### 📋 Specific to CallableStatement

- `registerOutParameter(int, int)` - Register OUT parameter
- `registerOutParameter(int, SQLType)` - Register OUT with SQL type
- `wasNull()` - Check if last read was NULL
- `getString(int)` - Get OUT parameter as string
- `getInt(int)` - Get OUT parameter as int
- `getDate(int)` - Get OUT parameter as date

---

## 🎓 Quick Reference Card

### 🔥 Most Common Methods

| Task | Method | Example |
|------|--------|---------|
| **Call procedure** | `prepareCall()` | `con.prepareCall("{CALL proc(?)}")` |
| **Set string param** | `setString()` | `cs.setString(1, "value")` |
| **Set int param** | `setInt()` | `cs.setInt(1, 100)` |
| **Execute & get data** | `executeQuery()` | `ResultSet rs = cs.executeQuery()` |
| **Execute & get count** | `executeUpdate()` | `int n = cs.executeUpdate()` |
| **Execute general** | `execute()` | `boolean b = cs.execute()` |
| **Read string data** | `rs.getString()` | `String s = rs.getString("name")` |
| **Read int data** | `rs.getInt()` | `int i = rs.getInt("age")` |
| **Move to next row** | `rs.next()` | `while (rs.next()) {...}` |
| **Close resources** | `close()` | `cs.close()` or use try-with-resources |

---

## 🎯 Summary - All CallableStatement Methods

### ✅ Essential Methods You Must Know

1. **prepareCall()** - Create CallableStatement
2. **setString(), setInt(), setLong()** - Set parameters
3. **execute()** - Execute procedure (general)
4. **executeQuery()** - Execute SELECT procedure
5. **executeUpdate()** - Execute INSERT/UPDATE/DELETE
6. **getResultSet()** - Get returned data
7. **close()** - Release resources

### 📊 Complete Method List

| Category | Methods |
|----------|---------|
| **Creation** | `prepareCall()` |
| **Parameter Setting** | `setString()`, `setInt()`, `setLong()`, `setDouble()`, `setBoolean()`, `setDate()`, `setObject()`, `setNull()` |
| **Execution** | `execute()`, `executeQuery()`, `executeUpdate()`, `executeBatch()` |
| **Getting Results** | `getResultSet()`, `getUpdateCount()`, `getMoreResults()` |
| **OUT Parameters** | `registerOutParameter()`, `getString()`, `getInt()`, `wasNull()` |
| **Batch Operations** | `addBatch()`, `executeBatch()`, `clearBatch()` |
| **Resource Management** | `close()`, `isClosed()` |

---

