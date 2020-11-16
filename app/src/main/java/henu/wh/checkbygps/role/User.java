package henu.wh.checkbygps.role;

public class User {

    private String phone;
    private String name;
    private String password;
    private boolean sex;
    private boolean identify;

    public User() {
    }

    public void set(String phone, String name, String password, boolean sex, boolean identify) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.identify = identify;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isIdentify() {
        return identify;
    }

    public void setIdentify(boolean identify) {
        this.identify = identify;
    }
}
