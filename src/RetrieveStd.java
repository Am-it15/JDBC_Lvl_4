import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RetrieveStd {


    public static void rtvMenu(Connection con, Scanner sc) throws SQLException {
        while (true) {
            System.out.println("1 :: Individual Student ");
            System.out.println("2 :: all Student ");
            System.out.println("3 :: Return to main menu ");
            System.out.print("Enter you retrieve choice :: ");
            if(!sc.hasNext()) {
                System.out.println("Enter Numbe" +
                        "r only");
                sc.next();
                continue;
            }
            int rtvChoice= sc.nextInt();

            switch (rtvChoice) {
                case 1 -> indStd(con, sc);
                case 2 -> allStd(con);
                case 3 -> {
                    System.out.println("Returning to Main Menu...");
                    return;                  // exit to main menu
                }
                default -> System.out.println("Enter valid retrieve choice");
            }
        }

    }

    private static void allStd(Connection con) {
        String procedure = "{CALL get_all_students()}"; // Stored procedure
        try (CallableStatement cs = con.prepareCall(procedure);
             ResultSet rs = cs.executeQuery()) {

            // Table header
            System.out.println("\nID       | Name                | Gender | DOB         | Phone          | Email                     | Address        | Admission   | Department      | Sem | Div | Parent Name         | Parent Phone");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-8s | %-20s | %-6s | %-10s | %-14s | %-25s | %-14s | %-11s | %-15s | %-3d | %-3s | %-20s | %-12s\n",
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("dob"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getDate("admission_date"),
                        rs.getString("department"),
                        rs.getInt("semester"),
                        rs.getString("division"),
                        rs.getString("parent_name"),
                        rs.getString("parent_phone"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all students: " + e.getMessage());
        }

    }

    private static void indStd(Connection con, Scanner sc) throws SQLException {
        try {
        sc.nextLine();
        System.out.print("Enter Student ID :: ");
        String id = sc.nextLine();

        CallableStatement cs = con.prepareCall("{CALL get_student_by_id(?)}");
        cs.setString(1, id);

        ResultSet rs = cs.executeQuery();

        if (rs.next()) {

            System.out.println("\n=========== Student Details ===========");

            System.out.println(String.format("ID            : %s", rs.getString("student_id")));
            System.out.println(String.format("Name          : %s", rs.getString("name")));
            System.out.println(String.format("Gender        : %s", rs.getString("gender")));
            System.out.println(String.format("DOB           : %s", rs.getDate("dob")));
            System.out.println(String.format("Phone         : %s", rs.getString("phone")));
            System.out.println(String.format("Email         : %s", rs.getString("email")));
            System.out.println(String.format("Address       : %s", rs.getString("address")));
            System.out.println(String.format("Admission Date: %s", rs.getDate("admission_date")));
            System.out.println(String.format("Department    : %s", rs.getString("department")));
            System.out.println(String.format("Semester      : %d", rs.getInt("semester")));
            System.out.println(String.format("Division      : %s", rs.getString("division")));
            System.out.println(String.format("Parent Name   : %s", rs.getString("parent_name")));
            System.out.println(String.format("Parent Phone  : %s", rs.getString("parent_phone")));

            System.out.println("=========================================");

        } else {
            System.out.println("❌ Student not found!");
        }

    } catch (Exception e) {
        System.out.println("Error processing student: " + e.getMessage());
    }

    }
}
