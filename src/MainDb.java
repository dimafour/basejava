import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainDb {
    public static void main(String[] args) {
        MainDb m = new MainDb();
        m.testDatabase();
    }

    private void testDatabase() {
        String url = "jdbc:postgresql://localhost:5432/contact_db";
        String login = "postgres";
        String password = "postgres";
        try (Connection con = DriverManager.getConnection(url, login, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM JC_CONTACT")) {
            while (rs.next()) {
                String str = rs.getString("contact_id") + ":" + rs.getString(2);
                System.out.println("Contact:" + str);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
