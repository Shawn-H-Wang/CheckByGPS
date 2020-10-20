package henu.wh.checkbygps.dbHelper;

import android.util.Log;

import java.sql.*;

/**
 * 这里新建了一个类，将访问mysql的方法封装在类中（以下称数据库工具类）
 * 再在主进程中调用数据库工具类中的具体方法来访问数据库
 */
public class JdbcUtil {

    private static final String TAG = "Connect-MySQL";
    private static final String IP = "10.0.2.2";
    private static final String DBNAME = "AUDB";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";
    private static final int PORT = 3306;

    public static Connection conn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v(TAG, "数据库驱动加载成功");
            // 这里url一定要加上最后面的字符编码方式"?useUnicode=true&characterEncoding=UTF-8"
            // 不加上的话，MySQL默认接收的是Latin1字符编码格式，中文无法显示
            String url = "jdbc:mysql://" + IP + ":" + PORT + "/" + DBNAME + "?useUnicode=true&characterEncoding=UTF-8";
            conn = DriverManager.getConnection(url, USER, PASSWORD);
            Log.d(TAG, "数据库连接成功");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "数据库驱动加载失败");
        } catch (SQLException e) {
            Log.e(TAG, "数据库连接失败");
        }
        return conn;
    }

    public static boolean select(Connection conn, String userphone) {
        boolean flag = true;
        String sql = "SELECT phone FROM USER WHERE phone='" + userphone + "'";
        try {
            if (conn != null) { // conn不为null表明数据库建立成功
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    Log.e(TAG, "该号码已被使用，需要更换号码！");
                    flag = false;
                }
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void insert(Connection conn, String userphone, String username, String password, boolean sex, boolean identify) {
        String sql = "INSERT INTO USER VALUES(?,?,?,?,?)";
        try {
            if (conn != null) { // conn不为null表明数据库建立成功
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, userphone);
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setBoolean(4, sex);
                ps.setBoolean(5, identify);
                ps.executeUpdate();
                Log.d(TAG, "数据插入成功|"+"插入数据为{" + userphone + "," + username + "," + password + "," + sex + "," + identify+ "}");
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
