package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.model.QuestMethod;
import cn.yb.datawaiter.model.Respon;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/sys")
public class SysController extends  BasicController {


    @RequestMapping(value = "/deleteByTableNameAndId")
    public Respon deleteByTableNameAndId(String tableName,String id) {
        if(tableName == null || id == null){
            return  responBasicError();
        }
        int count = Delete.deleteDataByPri(SysConn,tableName,id);
        return responOk(count);
    }

    @RequestMapping(value = "/findAll")
    public Respon findAll(String tableName) {
        if(tableName == null ){
            return  responBasicError();
        }
        return responOk(Select.findDataAllByTableName(SysConn,tableName));
    }
    @RequestMapping(value = "/findEnums")
    public Respon findEnums(String enumClassName) {
        try {
            Class aClass = Class.forName(enumClassName);
            Method m = aClass.getMethod("values");
           Object obj =  m.invoke(null);
            return responOk(obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  responBasicError();
    }
}
