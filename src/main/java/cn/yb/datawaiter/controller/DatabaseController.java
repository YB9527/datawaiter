package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.model.*;
import cn.yb.datawaiter.model.entity.Respon;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.service.impl.IFieldService;
import cn.yb.sys.service.impl.IProjectService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IProjectService projectService;

    @Autowired
    private IFieldService fieldService;

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
        return  insertCount == 0 ? respon.responError("连接数据库失败"):respon.ok("添加成功");
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
        return  count == 0 ? respon.responError("操作失败"):respon.ok("修改成功");
    }

    @RequestMapping(value = "/findAll")
    public Respon findAll() {
        Respon respon = startRespon();
        return respon.ok(Select.findDataBySQL(SysConn,"SELECT * FROM databaseconnect\n" +
                "left JOIN " +
                "(SELECT databaseConnectId,count(*) as apiCount from api GROUP BY databaseConnectId) as api\n" +
                "on databaseconnect.id = api.databaseConnectId  "));
    }

    /**
     * 查找项目所有的表格
     * @param projectid
     * @return
     */
    @RequestMapping(value = "/findByProjectId")
    public Respon findByProjectId(String projectid) {
        Respon respon = startRespon();
        return   respon.ok(projectService.findAllDatabaseByProjectid(projectid));

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

    @RequestMapping(value = "/findFiledAllByDatabaseidAndTableName")
    public Respon findFiledAllByDatabaseidAndTableName(String databaseId,String tableName) {
        Respon respon = startRespon();
        if(Tool.isEmpty(databaseId) || Tool.isEmpty(tableName)){
            return  respon.responError("id无效");
        }
        Connection coon = Connect.getSQLConnection(databaseId);
        List<TableColumn> tableColumns =  Connect.getColumnCommentByTableName(coon,tableName);
        return respon.ok(tableColumns);
    }

    @RequestMapping(value = "/findTableField")
    public Respon findTableField(String projectid, String databaseid,String tableName) {
        Respon respon = startRespon();
        if(Tool.isEmpty(projectid,databaseid,tableName)){
            return  respon.responError("id无效");
        }
        List<JSONObject> tableColumns =  fieldService.findTableField(projectid,databaseid,tableName);
        return respon.ok(tableColumns);
    }

    @PostMapping("/saveOrUpdateField")
    public Respon saveOrUpdateField(@RequestBody List<Field>  fieldarray) {
        Respon respon = startRespon();
        fieldService.saveOrUpdateField(fieldarray);
        return respon.ok("");
    }


}
