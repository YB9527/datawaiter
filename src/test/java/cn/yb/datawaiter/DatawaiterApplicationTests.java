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
