package cn.yb.datawaiter.tools;



import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTool {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static String dataFormat(Date date){
        return  simpleDateFormat.format(date);
    }

    /**
     * 日期格式化
     * @param date
     * @param format
     * @return
     */
    private static String dataFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
       return simpleDateFormat.format(date);
    }
}
