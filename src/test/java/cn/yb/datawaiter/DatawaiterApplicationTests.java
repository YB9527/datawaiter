package cn.yb.datawaiter;

import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.AutoCreateMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;


class DatawaiterApplicationTests {



    @Test
    void contextLoads() {
        Class aClass = null;
        try {
           String json = "'DeleteById'";
            AutoCreateMapper jsonTest = JSONObject.parseObject(json,AutoCreateMapper.class);
            String s= "123";
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
