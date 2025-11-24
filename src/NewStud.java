import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

public class NewStud {
    public static void addStd(Connection con, Scanner scan) {
        try {
            scan.nextLine(); // clear buffer

            System.out.print("Enter Name: ");
            String name = scan.nextLine();

            System.out.print("Enter Gender (Male/Female): ");
            String gender = scan.nextLine();

            System.out.print("Enter DOB (YYYY-MM-DD): ");
            String dob = scan.nextLine();

            System.out.print("Enter Phone: ");
            String phone = scan.nextLine();

            System.out.print("Enter Email: ");
            String email = scan.nextLine();

            System.out.print("Enter Address: ");
            String address = scan.nextLine();

            System.out.print("Enter Admission Date (YYYY-MM-DD): ");
            String admissionDate = scan.nextLine();

            System.out.print("Enter Department: ");
            String department = scan.nextLine();

            System.out.print("Enter Semester: ");
            int semester = scan.nextInt();
            scan.nextLine();

            System.out.print("Enter Division: ");
            String division = scan.nextLine();

            System.out.print("Enter Parent Name: ");
            String parentName = scan.nextLine();

            System.out.print("Enter Parent Phone: ");
            String parentPhone = scan.nextLine();


            // CALL PROCEDURE
            CallableStatement cstmt = con.prepareCall("{CALL insert_student(?,?,?,?,?,?,?,?,?,?,?,?)}");

            cstmt.setString(1, name);
            cstmt.setString(2, gender);
            cstmt.setString(3, dob);
            cstmt.setString(4, phone);
            cstmt.setString(5, email);
            cstmt.setString(6, address);
            cstmt.setString(7, admissionDate);
            cstmt.setString(8, department);
            cstmt.setInt(9, semester);
            cstmt.setString(10, division);
            cstmt.setString(11, parentName);
            cstmt.setString(12, parentPhone);

            boolean hasResult = cstmt.execute();

            if (hasResult) {
                ResultSet rs = cstmt.getResultSet();
                if (rs.next()) {
                    String studentId = rs.getString("student_id");
                    System.out.println("\n🎉 Student Added Successfully!");
                    System.out.println("Generated Student ID: " + studentId);
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
