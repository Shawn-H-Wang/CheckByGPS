package henu.wh.checkbygps.role;

public class MemSignInfo {

    private String signid;
    private String name;
    private String uid;
    private String gid;
    private String jingdu;
    private String weidu;
    private String signdate;
    private Boolean sign;
    private String signType;

    public MemSignInfo(String signid, String uid, String gid, String jingdu, String weidu, String signdate, String signType) {
        this.signid = signid;
        this.uid = uid;
        this.gid = gid;
        this.jingdu = jingdu;
        this.weidu = weidu;
        this.signdate = signdate;
        this.signType = signType;
        this.sign = false;
    }

    public MemSignInfo() {
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

    public Boolean getSign() {
        return sign;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
