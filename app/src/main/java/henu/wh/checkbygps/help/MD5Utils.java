package henu.wh.checkbygps.help;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * MD5加密算法
     * @param str 需要进行MD5加密的数据
     * @return 返回MD5加密后的密码
     */
    public static String Encrypt(String str){
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[] s=md.digest(str.getBytes());
            String ss="";
            String result="";
            for(int i=0;i<s.length;i++){
                ss=Integer.toHexString(s[i] & 0xff);
                if(ss.length()==1){
                    result+="0"+ss;
                }else{
                    result+=ss;
                }
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5解密算法
     * @param inStr
     * @return 返回MD5加密后的密码
     */
    public static String convertMD5(String inStr){
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }


}
