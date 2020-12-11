package henu.wh.checkbygps.role;

public class SignInfo {

    private String signID;
    private String gid;
    private String locatiojn1;  //定位经度
    private String locatiojn2;  //定位纬度
    private String date;    // 签到发起时间
    private int memnum; // 应签到人数
    private int num;    // 签到人数

    public SignInfo() {
    }

    public SignInfo(String signID, String gid, String locatiojn1, String locatiojn2, String date, int memnum, int num) {
        this.signID = signID;
        this.gid = gid;
        this.locatiojn1 = locatiojn1;
        this.locatiojn2 = locatiojn2;
        this.date = date;
        this.memnum = memnum;
        this.num = num;
    }

    public String getSignID() {
        return signID;
    }

    public void setSignID(String signID) {
        this.signID = signID;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getLocatiojn1() {
        return locatiojn1;
    }

    public void setLocatiojn1(String locatiojn1) {
        this.locatiojn1 = locatiojn1;
    }

    public String getLocatiojn2() {
        return locatiojn2;
    }

    public void setLocatiojn2(String locatiojn2) {
        this.locatiojn2 = locatiojn2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMemnum() {
        return memnum;
    }

    public void setMemnum(int memnum) {
        this.memnum = memnum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
