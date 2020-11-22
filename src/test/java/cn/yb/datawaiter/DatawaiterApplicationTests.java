package cn.yb.datawaiter;


import cn.yb.datawaiter.crawler.OkHttpClientUtils;
import cn.yb.datawaiter.crawler.RequestCallback;
import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.Insert;
import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.tools.FileTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


class DatawaiterApplicationTests {


    @Test
    void contextLoads() throws FileNotFoundException {
        File text = new File("d:\\123 (2).txt");

        try {
            FileReader fr = new FileReader(text);
            BufferedReader br = new BufferedReader(fr);
            String tempString = null;
            int line = 1;
            List<String> sqls = new ArrayList<>();
            List<JSONObject> jsons = new ArrayList<>();
            while ((tempString = br.readLine()) != null) {//BufferedReader有readLine()，可以实现按行读取
                JSONObject json = JSONObject.parseObject(tempString);
                sqls.add("insert into m_dict_data(id,pid,dictid,code,label) values ('" + json.getString("id") + "' ," +
                        "'" + json.getString("pid") +
                        "' ,'" + json.getString("dictid") + "' ,'" +
                        json.getString("code") + "' ,'" +
                        json.getString("label") + "' );");

                jsons.add(json);
            }
            int count = jsons.size();
            FileTool.writeStrs(sqls, "d:/444.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void test2() throws IOException {

        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/51/20/512021.html";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("application/x-www-form-urlencoded", "gbk")
                .build();

        Call call = OkHttpClientUtils.client.newCall(request);
        okhttp3.Response res = call.execute();
        byte[] b = res.body().bytes(); //获取数据的bytess
        String info = new String(b, "GB2312"); //然后将其转为gb2312
        for (int i = 0; i < 10; i++) {
            System.out.println("我是主线程,线程Id为:" + Thread.currentThread().getId());
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
