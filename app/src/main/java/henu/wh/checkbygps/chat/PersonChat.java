package henu.wh.checkbygps.chat;

public class PersonChat {

    private String phone;
    private String name;
    private String gid;
    private String chatMessage;
    private boolean isMeSend;

    public PersonChat() {
        super();
    }

    public PersonChat(String gid, String phone, String chatMessage, boolean isMeSend) {
        this.gid = gid;
        this.phone = phone;
        this.chatMessage = chatMessage;
        this.isMeSend = isMeSend;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public boolean isMeSend() {
        return isMeSend;
    }

    public void setMeSend(boolean meSend) {
        isMeSend = meSend;
    }
}
