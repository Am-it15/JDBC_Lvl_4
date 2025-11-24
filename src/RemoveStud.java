import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Scanner;

public class RemoveStud {

    public static void rmStd(Connection con, Scanner scan) {
        System.out.print("Enter Student ID to delete: ");
        String id = scan.next();

        System.out.print("Are you sure you want to delete this student? (Y/N): ");
        String confirm = scan.next();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("❌ Deletion cancelled.");
            return;
        }

        try (CallableStatement cs = con.prepareCall("{CALL delete_student_by_id(?)}")) {
            cs.setString(1, id);

            int affectedRows = cs.executeUpdate(); // returns number of rows deleted
            if (affectedRows > 0) {
                System.out.println("✅ Student with ID " + id + " deleted successfully!");
            } else {
                System.out.println("❌ No student found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
