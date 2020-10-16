package henu.wh.checkbygps.dbHelper;

import android.util.Log;

import java.sql.*;

/**
 * 由于Android Studio访问mysql数据库不能在主进程中访问，
 * 所以访问mysql的方法要写在新的线程中，
 * 这里新建了一个类，将访问mysql的方法封装在类中（以下称数据库工具类），
 * 再在主进程中调用数据库工具类中的具体方法来访问数据库
 */
public class JdbcUtil {

    private static final String TAG = "Connect-MySQL";

    public static void mysql() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.toString());
                    }

                    // 1. JDBC驱动程序加载
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Log.v(TAG, "数据库驱动加载成功");
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "数据库驱动加载失败");
                        return;
                    }

                    // 2. 通过-IP/数据库/用户/密码-访问数据库
                    String ip = "10.0.2.2";
                    int port = 3306;
                    String dbname = "AUDB";
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbname;
                    String user = "root";
                    String passwd = "12345678";

                    // 3. 连接JDBC
                    try {
                        Connection conn = DriverManager.getConnection(url, user, passwd);
                        Log.d(TAG, "数据库连接成功");
                        conn.close();
                        return;
                    } catch (SQLException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
        thread.start();
    }

}
