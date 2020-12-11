package henu.wh.checkbygps.message;

import java.sql.Date;

public class Message {

    private int mid;
    private String memid;
    private String message;
    private Date mdate;
    private String mtype;
    private boolean isGet;
    private boolean isRead;

    public Message() {
    }

    public Message(int mid, String memid, String message, Date mdate, String mtype, boolean isGet, boolean isRead) {
        this.memid = memid;
        this.message = message;
        this.mdate = mdate;
        this.mtype = mtype;
        this.isGet = isGet;
        this.isRead = isRead;
    }

    public String getMemid() {
        return memid;
    }

    public void setMemid(String memid) {
        this.memid = memid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
