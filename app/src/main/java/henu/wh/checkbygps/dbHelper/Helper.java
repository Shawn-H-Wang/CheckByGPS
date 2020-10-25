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
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }



}
