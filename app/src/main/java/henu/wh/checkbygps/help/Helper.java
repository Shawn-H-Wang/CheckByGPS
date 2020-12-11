package henu.wh.checkbygps.help;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    /**
     * <p>正则表达式验证phone是否正确</p>
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    @SuppressLint("DefaultLocale")
    public static String Entrycode(int count) {
        String gid = "g";
        gid += String.format("%9d", count);
        return gid;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String randomGID() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());
    }

    public static void exportExcel() {
        File file = new File(getSDPath() + "/Record");
        makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/成绩表.xls", null);
        String fileName = getSDPath() + "/Record/成绩表.xls";
    }

    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
}
