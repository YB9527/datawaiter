package cn.yb.datawaiter;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


class DatawaiterApplicationTests {


    static Connection conn;

    static {
        try {
            conn = Connect.getSQLConnection(new DatabaseConnect("127.0.0.1",DatabaseEnum.mysql,"datawaiter","root","1234"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static Connection connsys;

    static {
        try {
            connsys = Connect.getSQLConnection(new DatabaseConnect("127.0.0.1",DatabaseEnum.mysql,"datawaitersystem","root","1234"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //static Connection postgressconno = Connect.getSQLConnection(new DatabaseConnect("192.168.30.12:5432",DatabaseEnum.postgress,"lqdb","postgres","1"));

    @Test
    void contextLoads() {
        DatabaseConnect datawaiter = new DatabaseConnect("127.0.0.1",DatabaseEnum.mysql,"datawaiter","root","1234");
        datawaiter.setId(UUID.randomUUID().toString());
        long start = System.currentTimeMillis();
        /*List<JSONObject> datas = new ArrayList<>();

        for (int i = 0; i < 60000; i++) {
            JSONObject jsonObject = new JSONObject();
            if(i  == 0){
                jsonObject.put("t",77);
                jsonObject.put("name","77");
            }else{
                jsonObject.put("name","name"+i);
            }
            jsonObject.put("id",UUID.randomUUID().toString());
            jsonObject.put("password","password"+i);
            datas.add(jsonObject);
        }
        List<DatabaseConnect> connects = new ArrayList<>();
        connects.add(datawaiter);

        int insertcount = Insert.insertManyPos(connsys,connects);*/
        //加载MySql驱动
        /*String sql = "select * from user";
        List<JSONObject> datas = Select.findDataBySQL(conn, sql);
         int count =Delete.deleteDataByPri(conn,"user",datas);*/
        //String sql = "select * from nav_xian";
        //int count = Update.updateManyDatas(conn, "user", datas);
        //int count = Insert.insertManyDatas(conn,"user",datas);
        //Delete.deleteByColoumnAndValues(conn,"user","id",values);
      /*  List<JSONObject> nav_xian = Select.findDataBySQL(postgressconno, sql);

        String sys_cfg_app_domain = "select * from dk LIMIT 100000 OFFSET 0";
        List<JSONObject> domain = Select.findDataBySQL(postgressconno, sys_cfg_app_domain);*/
       // List<Table> allTableName = Connect.getAllTableName(postgressconno);
        //删除数据
        /*Object[] values = new Object[]{"2","yb"};
        //int count = Delete.deleteByColoumnAndValues(conn,"user","name",values);
        //插入行数据
       // int insertcount = Insert.insertManyDatas(conn,"user",jsonObjects);
        //修改数据
        for (JSONObject jsonObject :jsonObjects ){
            jsonObject.replace("name","9527");
            jsonObject.replace("password","9999");
        }
        int count = Update.updateManyDatas(conn, "user", jsonObjects);*/

        long total = System.currentTimeMillis()- start;
    }
}
