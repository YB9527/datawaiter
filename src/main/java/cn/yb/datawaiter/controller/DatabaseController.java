package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.model.Level;
import cn.yb.datawaiter.model.Respon;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/database")
public class DatabaseController extends  BasicController {

    @RequestMapping(value = "/finddatabaseenum")
    public Respon findDatabaseEnum() {
        return responOk(DatabaseEnum.values());
    }

    @PostMapping("/addConnection")
    public Respon addConnection(@RequestBody DatabaseConnect connect) {
        int insertCount = 0;
        if(connect.getId() != null){
            //检查是否能连接
            try {
                Connection sqlConnection = Connect.getSQLConnection(connect);
                if(sqlConnection != null){
                    insertCount = Insert.insertPo(SysConn,connect);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return  insertCount == 0 ? responError("连接数据库失败"):responOk("");
    }

    @PostMapping("/editConnection")
    public Respon editConnection(@RequestBody DatabaseConnect connect) {
        int count = 0;
        if(connect.getId() != null){
            //检查是否能连接
            try {
                Connection sqlConnection = Connect.getSQLConnection(connect);
                if(sqlConnection != null){
                    count = Update.updateDataPo(SysConn,connect);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  count == 0 ? responError("删除失败"):responOk("");
    }

    @RequestMapping(value = "/findAll")
    public Respon findAll() {
        return responOk(Select.findDataBySQL(SysConn,"SELECT * FROM databaseconnect\n" +
                "left JOIN " +
                "(SELECT databaseConnectId,count(*) as apiCount from api GROUP BY databaseConnectId) as api\n" +
                "on databaseconnect.id = api.databaseConnectId  "));
    }

}
