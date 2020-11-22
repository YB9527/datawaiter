package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.Level;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.tools.Tool;
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
        Respon respon = startRespon();
        return respon.ok(DatabaseEnum.values());
    }

    @PostMapping("/addConnection")
    public Respon addConnection(@RequestBody DatabaseConnect connect) {
        int insertCount = 0;
        Respon respon = startRespon();
        if(connect.getId() != null){
            //检查是否能连接
            try {
                Connection sqlConnection = Connect.getSQLConnection(connect);
                if(sqlConnection != null){
                    SysConn.setAutoCommit(false);
                    insertCount = Insert.insertPo(SysConn,connect);
                    SysConn.commit();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return  insertCount == 0 ? respon.responError("连接数据库失败"):respon.ok("");
    }

    @PostMapping("/editConnection")
    public Respon editConnection(@RequestBody DatabaseConnect connect) {
        Respon respon = startRespon();
        int count = 0;
        if(connect.getId() != null){
            //检查是否能连接
            try {
                Connection sqlConnection = Connect.getSQLConnection(connect);
                if(sqlConnection != null){
                    SysConn.setAutoCommit(false);
                    count = Update.updateDataPo(SysConn,connect);
                    Connect.removeSQLConnection(connect.getId());
                    SysConn.commit();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  count == 0 ? respon.responError("操作失败"):respon.ok("");
    }

    @RequestMapping(value = "/findAll")
    public Respon findAll() {
        Respon respon = startRespon();
        return respon.ok(Select.findDataBySQL(SysConn,"SELECT * FROM databaseconnect\n" +
                "left JOIN " +
                "(SELECT databaseId,count(*) as apiCount from api GROUP BY databaseId) as api\n" +
                "on databaseconnect.id = api.databaseId  "));
    }

    @RequestMapping(value = "/findTableAllByDatabaseId")
    public Respon findTableAllByDatabaseId(String id) {
        Respon respon = startRespon();
        if(Tool.isEmpty(id)){
            return  respon.responError("id无效");
        }
        Connection coon = Connect.getSQLConnection(id);
        List<Table> tables =  Connect.getAllTableName(coon);
        return respon.ok(tables);
    }

}
