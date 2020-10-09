package henu.wh.checkbygps.student;

/**
 * @Author:wanghe
 * @Date:2020/10/09 The information of the Student.
 */
public class StudentInfo {

    private String sid;
    private String name;
    private boolean sex;

    public StudentInfo() {
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }

    public StudentInfo(String sid, String name, boolean sex) {
        this.sid = sid;
        this.name = name;
        this.sex = sex;
    }

    public String getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
