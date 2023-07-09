import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MysqlConn {

    private Connection conn = null;
    private String url = "jdbc:mysql://93.89.225.198:3306/pmitLn2oq_javadb";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String userName = "pmitLn2oq_javaus";
    private String password = "123456aA+-";
    private ResultSet res;
    private Statement st;

    public MysqlConn() {
        try {
            st = connect();
        } catch (Exception e) {
            System.out.println("Veritabanına bağlanamadı!");
        }
    }

    public Statement connect() throws Exception {
        Class.forName(driver);
        conn = DriverManager.getConnection(url, userName, password);
        return conn.createStatement();
    }

    public void disconnect() throws Exception {
        conn.close();
    }

    public ResultSet getData(String sql) throws Exception {
        res = st.executeQuery(sql);
        return res;
    }

}
