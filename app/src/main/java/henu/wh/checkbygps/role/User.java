package henu.wh.checkbygps.role;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String phone;
    private String name;
    private String password;
    private boolean sex;
    private boolean identify;
    private List<String> managegroup = new ArrayList<String>();
    private List<String> ingroup = new ArrayList<String>();

    public User() {
    }

    public void set(String phone, String name, String password, boolean sex, boolean identify) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.identify = identify;
    }

    public void set(List<String> managegroup, List<String> ingroup) {
        this.managegroup = managegroup;
        this.ingroup = ingroup;
    }

    public List<String> getManagegroup() {
        return managegroup;
    }

    public void setManagegroup(List<String> managegroup) {
        this.managegroup = managegroup;
    }

    public List<String> getIngroup() {
        return ingroup;
    }

    public void setIngroup(List<String> ingroup) {
        this.ingroup = ingroup;
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
