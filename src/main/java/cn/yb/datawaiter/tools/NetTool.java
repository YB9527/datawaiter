package cn.yb.datawaiter.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetTool {
    /**
     * 读取网络文件
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(String path) throws IOException {
        File file = new File(path);
        if(file.exists()){
            InputStream iputstream = new  FileInputStream(path);
            return  iputstream;
        }

        URL url = new URL(path);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
        uc.connect();
        InputStream iputstream = uc.getInputStream();
        return  iputstream;
    }
}
