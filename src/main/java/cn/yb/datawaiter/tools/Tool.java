package cn.yb.datawaiter.tools;

import java.io.File;
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
        return str == null ? true : str.trim().isEmpty();
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


}
