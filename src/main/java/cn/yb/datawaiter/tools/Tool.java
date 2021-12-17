package cn.yb.datawaiter.tools;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 一般工具类
 */
public class Tool {
    /**
     * 检查集合是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null ? true: list.isEmpty();
    }
    /**
     *
     * @param flag null 返回false
     * @return
     */
    public static boolean isEmpty(Boolean flag) {
        return flag == null ? false:flag;
    }

    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getHostAddress() {
        String hostAddress = "http://192.168.2.47:8080/";
        return hostAddress;
    }


    /**
     * 检查文件夹是否存在
     *
     * @param dirPath   文件路径
     * @param isCreated 如果没有是否创建
     */
    public static boolean exitsDir(String dirPath, boolean isCreated) {
        File file = new File(dirPath);
        if (file.exists()) {
            return true;
        } else {
            if (isCreated) {
                boolean bl = file.mkdirs();
                return bl;
            }
        }
        return false;
    }

    public static boolean isEmpty(String ...array) {
        if(array == null){
            return  true;
        }
       for(String str : array){
           if(str == null || "".equals(str.trim()) || str.equals("undefined")){
               return  true;
           }
       }
       return  false;
    }

    public static boolean isEmpty(String str) {
        return str == null ? true : str.trim().isEmpty()||str.equals("undefined");
    }


    /**
     *
     * @param list
     * @return
     */
    public static String listToString(List<String> list,String connectsymbol) {
        if(Tool.isEmpty(list)){
            return  "";
        }
        StringBuilder sb = new StringBuilder();
        int size = list.size()-1;
        for (int i = 0; i < size; i++) {
            sb.append(list.get(i) + connectsymbol);
        }
        sb.append(list.get(size));
        return  sb.toString();
    }

    /**
     * 检查 map 集合是否为空
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return map == null ? true:map.isEmpty();
    }


    public static<T> List<JSONObject> list2JSON(List<T> list) {
        List<JSONObject> jsons = new ArrayList<>();
        for (T t : list){
            jsons.add((JSONObject) JSONObject.toJSON(t));
        }
        return  jsons;
    }

    /**
     * 首先字母大写
     * @param letter
     * @return
     */
    public static String upperFirstLatter(String letter){
        return letter.substring(0, 1).toUpperCase()+letter.substring(1);
    }

    /**
     * 右边补0
     * @param str
     * @param strLength
     * @return
     */
    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 除去科学计数法的E
     * @param value
     * @return
     */
    public static String doubleToString(Double value) {
        if(value != null){
            return  new BigDecimal(value).toString();
        }
        return  null;

    }

    public static File getFile(String xlsPath) throws FileNotFoundException {
        if(xlsPath.startsWith("src")){
            return  new File(xlsPath);
        }
        File  xlsFile =   ResourceUtils.getFile("classpath:"+xlsPath);

        return  xlsFile;
    }

    /**
     * 补齐符号
     * @param str
     * @param symbol
     * @param len
     * @param fangxiang
     * @return
     */
    public static String addSymbol(String str, String symbol, int len, String fangxiang) {
        if(str == null){
            str = "";
        }
        while (str.length() < len){
            switch (fangxiang){
                case  "LEFT":
                    str  =  symbol+ str ;
                    break;
                case  "RIGHT":
                    str  = str +symbol;
                    break;
            }
        }
        return  str;
    }

    /**
     * 这个类型 是不是 数字型的
     * @param simpleName 类型名字
     * @return
     */
    public static boolean typeIsNumber(String simpleName) {
        switch (simpleName){
            case "short":
            case "Short":
            case "int":
            case "Integer":
            case "long":
            case "Long":
            case "float":
            case "Float":
            case "double":
            case "Double":
            case "BigDecimal":
                return  true;
        }
        return  false;
    }

    public static BigDecimal add(BigDecimal ...decimals) {
        BigDecimal sum = new BigDecimal(0);
        if(decimals != null){
            for (int i = 0; i < decimals.length; i++) {
                BigDecimal bigDecimal = decimals[i];
                if(bigDecimal != null){
                    sum = sum.add(bigDecimal);
                }
            }
        }
        return sum;
    }

    /**
     * d1 比 d2 多多少
     * @param d1
     * @param d2
     * @return
     */
    public static int compareTo(BigDecimal d1, BigDecimal d2) {
        if(d1 == null ){
            d1 = new BigDecimal(0);
        }
        if( d2 == null){
            d2 = new BigDecimal(0);
        }
        return  d1.compareTo(d2);
    }

    public static int numberValue(Integer jjxwcrs) {
        return  jjxwcrs == null ? 0 : jjxwcrs.intValue();
    }

    public static BigDecimal newBigDecimal(Object val) {
        if(val == null){
            return  null;
        }
        return  new BigDecimal(val.toString());
    }
    public static String getInterIP() {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }


}
