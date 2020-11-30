package henu.wh.checkbygps.role;

public class Group {

    private String gid;
    private String gname;
    private String groupowner;

    public Group() {
    }

    public Group(String gid, String gname, String groupowner) {
        this.gid = gid;
        this.gname = gname;
        this.groupowner = groupowner;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGroupowner() {
        return groupowner;
    }

    public void setGroupowner(String groupowner) {
        this.groupowner = groupowner;
    }

    @Override
    public String toString() {
        return "Group{" + "gid='" + gid + '\'' + ", gname='" + gname + '\'' + ", groupowner='" + groupowner + '\'' + '}';
    }
}
