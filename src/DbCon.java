import java.sql.Connection;
import java.sql.DriverManager;

public class DbCon {
    static String url = "jdbc:mysql://localhost:3306/test";
    static String user = "root";
    static String pass = "";
    public static Connection getConnection() {

        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Database Connected Successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return con;
    }
}
