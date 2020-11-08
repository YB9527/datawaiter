package cn.yb.datawaiter;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.tools.ReflectTool;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


class DatawaiterApplicationTests {



    @Test
    void contextLoads() {
        Class aClass = null;
        try {
            JSONObject json = new JSONObject();
            aClass = Class.forName("cn.yb.datawaiter.model.QuestMethod");
            Map<String,Method> methodMap =   ReflectTool.getMethod(ReflectTool.MethodNameEnum.get,aClass);
            Enum[] enums =  (Enum[])(aClass.getMethod("values")).invoke(null);
            for (Enum en : enums){
                JSONObject enJson = new JSONObject();
                json.put(en.toString(),enJson);
                for (Method m :methodMap.values()){
                    Object obj =  m.invoke(en);
                    String str = obj.toString();
                    enJson.put(m.getName().replace("get","").toLowerCase(),str);
                }
            }
            String s= "123";
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
