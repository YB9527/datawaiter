package cn.yb.sys.controller;

import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.tools.ReflectTool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RestController
@RequestMapping(value = "/sys")
public class SysController extends BasicController {

    @RequestMapping(value = "/breakconnect")
    public Respon breakConnect() {
        Respon respon = startRespon();
        Connect.breakConnect();
        return respon.ok("");
    }

    @RequestMapping(value = "/getdatetime")
    public Respon getdatetime() {
        Respon respon = startRespon();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        return respon.ok(df.format(new Date()));
    }

    @RequestMapping(value = "/deleteByTableNameAndId")
    public Respon deleteByTableNameAndId(String tableName, String id) {
        Respon respon = startRespon();
        if (tableName == null || id == null) {
            return respon.responBasicError();
        }
        int count = Delete.deleteDataByPri(SysConn, tableName, id);
        return respon.ok(count);
    }

    @RequestMapping(value = "/findByTableNameAndId")
    public Respon findByTableNameAndId(String tableName, String id) {
        Respon respon = startRespon();
        if (tableName == null || id == null) {
            return respon.responBasicError();
        }
        return respon.ok(Select.findDataById(SysConn,tableName,id));
    }

    @RequestMapping(value = "/findAll")
    public Respon findAll(String tableName) {
        Respon respon = startRespon();
        if (tableName == null) {
            return respon.responBasicError();
        }
        tableName = tableName.toLowerCase();
        return respon.ok(Select.findDataAllByTableName(SysConn, tableName));
    }

    @RequestMapping(value = "/findEnums")
    public Respon findEnums(String enumClassName) {
        Respon respon = startRespon();
        try {
            Class aClass = Class.forName(enumClassName);
            Method m = aClass.getMethod("values");
            Object obj = m.invoke(null);
            Map<String, Method> methodMap = ReflectTool.getMethod(ReflectTool.MethodNameEnum.get, aClass);
            if (methodMap != null && methodMap.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                Enum[] enums = (Enum[]) (aClass.getMethod("values")).invoke(null);
                for (Enum en : enums) {
                    JSONObject enJson = new JSONObject();
                    jsonArray.add(enJson);
                    for (Method mtem : methodMap.values()) {
                        obj = mtem.invoke(en);
                        String str = obj.toString();
                        enJson.put(mtem.getName().replace("get", "").toLowerCase(), str);
                    }
                }
                return respon.ok(jsonArray);
            } else {
                return respon.ok(obj);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return respon.responBasicError();
    }


}
