package henu.wh.checkbygps.student;

/**
 * @Author:wanghe
 * @Date:2020/10/09
 * The table information of the Student.
 */
public class StudentTable {

    public static final String TAB_NAME = "tab_student";
    public static final String SID = "SID";
    public static final String SNAME = "Sname";
    public static final String SSEX = "Ssex";

    public static final String CREATE_TAB_STUDENT_SQL = "create table if not exists "
            + TAB_NAME + " ("
            + SID + "CHAR(10) PRIMARY KEY, "
            + SNAME + "VARCHAR(255),"
            + SSEX + "CHAR(2));";


}
