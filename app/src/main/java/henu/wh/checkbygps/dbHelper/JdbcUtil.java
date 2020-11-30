package henu.wh.checkbygps.dbHelper;

import android.util.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.chat.PersonChat;
import henu.wh.checkbygps.help.MD5Utils;
import henu.wh.checkbygps.role.Group;
import henu.wh.checkbygps.role.User;

/**
 * 这里新建了一个类，将访问mysql的方法封装在类中（以下称数据库工具类）
 * 再在主进程中调用数据库工具类中的具体方法来访问数据库
 */
public class JdbcUtil {

    private static final String TAG = "Connect-MySQL";
    private static final String IP = "47.100.139.81";
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

    public static boolean selectPhone(Connection conn, String userphone) {
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

    public static boolean selectPassword(Connection conn, User user, String userphone, String pd) {
        boolean flag = false;
        String sql = "SELECT * FROM USER WHERE phone='" + userphone + "'";
        try {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    Log.d(TAG, "用户存在，查找成功！");
                    String phone = rs.getString("phone");
                    String username = rs.getString("user");
                    String password = rs.getString("passwd");
                    boolean sex = rs.getBoolean("sex");
                    boolean identify = rs.getBoolean("identify");
                    user.set(phone, username, password, sex, identify);
                    if (password.equals(pd)) {
                        flag = true;
                        Log.d(TAG, "密码输入正确！");
                    } else {
                        Log.e(TAG, "密码输入错误！");
                    }
                }
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean isExistUSER(Connection conn, String userphone) {
        boolean flag = false;
        String sql = "SELECT phone FROM USER WHERE phone='" + userphone + "'";
        try {
            if (conn != null) { // conn不为null表明数据库建立成功
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {    // 如果查询有结果，返回true，说明存在！
                    flag = true;
                } else {
                    Log.e(TAG, "用户不存在！");
                }
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void insertUSER(Connection conn, String userphone, String username, String password, boolean sex, boolean identify) {
        String sql = "INSERT INTO `USER` VALUES(?,?,?,?,?)";
        try {
            if (conn != null) { // conn不为null表明数据库建立成功
                password = MD5Utils.Encrypt(password);
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, userphone);
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setBoolean(4, sex);
                ps.setBoolean(5, identify);
                ps.executeUpdate();
                Log.d(TAG, "数据插入成功|" + "插入数据为{" + userphone + "," + username + "," + password + "," + sex + "," + identify + "}");
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertGroup(Connection conn, String groupid, String groupname, String groupowner) {
        String sql = "INSERT INTO `group` VALUES(?,?,?)";
        try {
            if (conn != null) { // conn不为null表明数据库建立成功
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, groupid);
                ps.setString(2, groupname);
                ps.setString(3, groupowner);
                ps.executeUpdate();
                Log.d(TAG, "数据插入成功|插入数据为{" + groupid + "," + groupname + "," + groupowner + "}");
                ps.close();
                addIntoGroup(conn, groupid, groupowner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addIntoGroup(Connection conn, String groupid, String groupowner) {
        String sql = "INSERT INTO groupmember VALUES(?,?)";
        try {
            if (conn != null) { // conn不为null表明数据库建立成功
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, groupid);
                ps.setString(2, groupowner);
                ps.executeUpdate();
                Log.d(TAG, "数据插入成功|插入数据为{" + groupid + "," + groupowner + "}");
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void selectManageGroup(Connection conn, User user) {
        List<String> list = new ArrayList<String>();
        String userphone = user.getPhone();
        String sql = "SELECT * FROM `group` WHERE groupowner='" + userphone + "'";
        try {
            if (conn != null) { // conn不为null表明数据库建立成功
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String s1 = rs.getString("groupname");
//                    System.out.println(s1);
                    list.add(s1);
                }
//                System.out.println(list);
                stmt.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "查询无果");
            return;
        }
        if (list.size() <= 0) {
            return;
        }
        user.setManagegroup(list);
    }

    public static void selectInGroup(Connection conn, User user, List<Group> glist) {
        List<String> list = new ArrayList<String>();
        glist.clear();
        String userphone = user.getPhone();
        String sql = "select * from `group` where groupid in (select groupid from groupmember where groupmember.memberid = '" + userphone + "');";
        try {
            if (conn != null) { // conn不为null表明数据库建立成功
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String s1 = rs.getString("groupname");
                    glist.add(new Group(rs.getString("groupid"), rs.getString("groupname"), rs.getString("groupowner")));
//                    System.out.println(s1);
                    list.add(s1);
                }
//                System.out.println(list);
                stmt.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "查询无果");
            return;
        }
        if (list.size() <= 0) {
            return;
        }
        user.setIngroup(list);
    }

    public static void selectGroup(Connection conn, String message, List<Group> Glist) {
        String sql = "select * from `group` where groupid = '" + message + "' or groupname like '%" + message + "%'";
        Glist.clear();
        try {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Glist.add(new Group(rs.getString("groupid"), rs.getString("groupname"), rs.getString("groupowner")));
                }
                Log.d(TAG, "Group message: " + Glist.toString());
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void selectMessage(Connection conn, User user, String gid, List<PersonChat> plist) {
        String sql = "select * from messge where groupid='"+gid+"' order by mdate";
        plist.clear();
        try {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String mid = rs.getString("memberid");
                    plist.add(new PersonChat(
                            rs.getString("groupid"),
                            rs.getString("memberid"),
                            rs.getString("message"),
                            user.getPhone().equals(mid)));
                }
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过限定手机号进行密码修改
     *
     * @param conn
     * @param phone
     * @param password
     */
    public static void updatepassword(Connection conn, String phone, String password) {
        String sql = "update USER set passwd = ? where phone = ?";
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, password);
                ps.setString(2, phone);
                ps.executeUpdate();
                Log.d(TAG, "密码修改成功");
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatename(Connection conn, String phone, String name) {
        String sql = "update USER set user = ? where phone = ?";
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, phone);
                ps.executeUpdate();
                Log.d(TAG, "密码修改成功");
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
