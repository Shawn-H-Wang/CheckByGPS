package henu.wh.checkbygps.dbHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    /**
     * <p>正则表达式验证phone是否正确</p>
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile("^[1][3,5,6,7,8,9][1-9]{9}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }



}
