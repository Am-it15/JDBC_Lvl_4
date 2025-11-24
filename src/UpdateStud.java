import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Scanner;

public class UpdateStud {

    public static void updStd(Connection con, Scanner scan) {
        System.out.print("Enter Student ID to update: ");
        String id = scan.next();

        while (true) {
            System.out.println("\n=== Update Menu ===");
            System.out.println("1. Name");
            System.out.println("2. Gender");
            System.out.println("3. DOB");
            System.out.println("4. Phone");
            System.out.println("5. Email");
            System.out.println("6. Address");
            System.out.println("7. Admission Date");
            System.out.println("8. Department");
            System.out.println("9. Semester");
            System.out.println("10. Division");
            System.out.println("11. Parent Name");
            System.out.println("12. Parent Phone");
            System.out.println("0. Exit");
            System.out.print("Choose field to update: ");

            int choice = scan.nextInt();
            scan.nextLine(); // consume newline

            if (choice == 0) {
                System.out.println("Exiting update menu...");
                break;
            }

            String column = "";
            switch (choice) {
                case 1: column = "name"; break;
                case 2: column = "gender"; break;
                case 3: column = "dob"; break;
                case 4: column = "phone"; break;
                case 5: column = "email"; break;
                case 6: column = "address"; break;
                case 7: column = "admission_date"; break;
                case 8: column = "department"; break;
                case 9: column = "semester"; break;
                case 10: column = "division"; break;
                case 11: column = "parent_name"; break;
                case 12: column = "parent_phone"; break;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
                    continue;
            }

            System.out.print("Enter new value for " + column + ": ");
            String newValue = scan.nextLine();

            try (CallableStatement cs = con.prepareCall("{CALL update_student_field(?, ?, ?)}")) {
                cs.setString(1, id);
                cs.setString(2, column);
                cs.setString(3, newValue);

                cs.execute();
                System.out.println("✅ " + column + " updated successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
