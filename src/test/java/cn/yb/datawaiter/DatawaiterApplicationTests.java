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
import org.apache.commons.logging.Log;
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
        int[] arr = new int[]{9,5,4,6,8,7,3,1,2};

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i-1; j++) {
                if(arr[j] < arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }


}
