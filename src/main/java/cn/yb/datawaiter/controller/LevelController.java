package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.model.Level;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.service.impl.ILevelService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/level")
public class LevelController extends  BasicController {

    @Autowired
    private ILevelService levelService;


    @RequestMapping(value = "/findApiLevelByParentId")
    public Respon findApiLevelByParentId(String parentId) {
        Respon respon = startRespon();

        String sql = "SELECT * FROM \n" +
                "\t\t(select * from level where typename = '"+ Level.LEVEL_TYPENAME_API+"' AND parentid='"+parentId+"') as level\n" +
                "LEFT JOIN (select levelid,count(*) as apicount from  api GROUP BY levelId ) AS api\n" +
                "ON level.id = api.levelid";
        List<JSONObject> domain = Select.findDataBySQL(SysConn, sql);
        return respon.ok(domain);
    }

    @RequestMapping(value = "/findapilevel")
    public Respon findApiLevel() {
        Respon respon = startRespon();

        String sql = "SELECT * FROM \n" +
                "\t\t(select * from level where typename = '"+ Level.LEVEL_TYPENAME_API+"'"+") as level\n" +
                "LEFT JOIN (select levelid,count(*) as apicount from  api GROUP BY levelId ) AS api\n" +
                "ON level.id = api.levelid";
        List<JSONObject> domain = Select.findDataBySQL(SysConn, sql);

        return respon.ok(domain);
    }
    @RequestMapping(value = "/findBeanLevel")
    public Respon findBeanLevel() {
        Respon respon = startRespon();
        String sql = "select * from level where typename = '"+ Level.LEVEL_TYPENAME_BEAN+"'";
        List<JSONObject> domain = Select.findDataBySQL(SysConn, sql);
        return respon.ok(domain);
    }

    @PostMapping("/addBeanLevel")
    public Respon addBeanLevel(@RequestBody Level level) throws SQLException {
        int insertCount = 0;
        Respon respon = startRespon();
        if(level.getId() != null){
            SysConn.setAutoCommit(false);
            level.setTypeName(Level.LEVEL_TYPENAME_BEAN);
            insertCount = Insert.insertPo(SysConn,level);
            SysConn.commit();
        }
        return  insertCount == 0 ? respon.responBasicError():respon.ok(insertCount);
    }

    @PostMapping("/addapilevel")
    public Respon saveAppversion(@RequestBody Level level) {
        int insertCount = 0;
        Respon respon = startRespon();
        if(level.getId() != null){
            level.setTypeName(Level.LEVEL_TYPENAME_API);
            insertCount = Insert.insertPo(SysConn,level);
        }
        return  insertCount == 0 ? respon.responBasicError():respon.ok(insertCount);
    }
    @PostMapping("/editApiLevel")
    public Respon editApiLevel(@RequestBody Level level) {
        int count = 0;
        Respon respon = startRespon();
        if(level.getId() != null){
            level.setTypeName(Level.LEVEL_TYPENAME_API);
            count = Update.updateManyDataPosInService(SysConn,level);
        }
        return  count == 0 ? respon.responBasicError():respon.ok(count);
    }

    @RequestMapping(value = "/deletelevelbyid")
    public Respon deleteLevelById(String id) throws SQLException {
        Respon respon = startRespon();
        SysConn.setAutoCommit(false);
        int count = Delete.deleteDataByPri(SysConn,Level.class.getSimpleName(), id);
        SysConn.commit();
        return  count == 0 ? respon.responBasicError():respon.ok(count);
    }

    @RequestMapping(value = "/findLevelAndParentId")
    public Respon findLevelAndParentId(String id) {
        Respon respon = startRespon();
        JSONObject json = levelService.findDataById(id);
        levelService.findParent(json);
        return respon.ok(json);
    }



}
