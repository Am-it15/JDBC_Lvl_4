import java.sql.Connection;
import java.util.Scanner;

public class Main {
    static void main(String[] args) {

        try {
            Connection con =DbCon.getConnection();
            Scanner scan=new Scanner(System.in);

            while (true) {
                System.out.println("\n============================= Student Manager =============================");
                stdMenu();

                System.out.print("Enter you choice :: ");
                if(!scan.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a NUMBER only.");
                    scan.next();
                    continue;
                }
                int choice= scan.nextInt();


                switch (choice) {
                    case 1 -> NewStud.addStd(con, scan);
                    default -> System.out.println("\n> Enter valid choice");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void stdMenu() {
        System.out.println("1: New Student");
        System.out.println("2: View Student");
        System.out.println("3: Update Student");
        System.out.println("4: Remove Student");
        System.out.println("4: Exit");
    }
}