package henu.wh.checkbygps.role;

public class MemSignInfo {

    private String signid;
    private String uid;
    private String gid;
    private String jingdu;
    private String weidu;
    private String signdate;

    public MemSignInfo(String signid, String uid, String gid, String jingdu, String weidu, String signdate) {
        this.signid = signid;
        this.uid = uid;
        this.gid = gid;
        this.jingdu = jingdu;
        this.weidu = weidu;
        this.signdate = signdate;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getJingdu() {
        return jingdu;
    }

    public void setJingdu(String jingdu) {
        this.jingdu = jingdu;
    }

    public String getWeidu() {
        return weidu;
    }

    public void setWeidu(String weidu) {
        this.weidu = weidu;
    }

    public String getSigndate() {
        return signdate;
    }

    public void setSigndate(String signdate) {
        this.signdate = signdate;
    }

    @Override
    public String toString() {
        return "MessegeInfo{" +
                "signid='" + signid + '\'' +
                ", uid='" + uid + '\'' +
                ", gid='" + gid + '\'' +
                ", jingdu='" + jingdu + '\'' +
                ", weidu='" + weidu + '\'' +
                ", signdate='" + signdate + '\'' +
                '}';
    }
}
