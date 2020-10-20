package henu.wh.checkbygps.dbHelper;

import cn.smssdk.SMSSDK;

public class SMSSDKUtil {

    public void play(String number) {
        SMSSDK.getVerificationCode("86", number);
    }

    public void tijiao(String number, String testcode) {
        SMSSDK.submitVerificationCode("86", number, testcode);
    }

}
