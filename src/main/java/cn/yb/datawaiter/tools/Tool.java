package cn.yb.datawaiter.tools;

import cn.yb.datawaiter.model.Param;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

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
