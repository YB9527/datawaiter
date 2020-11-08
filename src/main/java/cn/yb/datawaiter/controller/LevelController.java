package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.Insert;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.model.Level;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.service.impl.ILevelService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/level")
public class LevelController extends  BasicController {

    @Autowired
    private ILevelService levelService;
    @RequestMapping(value = "/findapilevel")
    public Respon findApiLevel() {
        String sql = "select * from level where typename = '"+ Level.API_LEVEL_TYPENAME+"'";
        List<JSONObject> domain = Select.findDataBySQL(SysConn, sql);
        return responOk(domain);
    }
    @PostMapping("/addapilevel")
    public Respon saveAppversion(@RequestBody Level level) {
        int insertCount = 0;
        if(level.getId() != null){
            level.setTypeName(Level.API_LEVEL_TYPENAME);
            insertCount = Insert.insertPo(SysConn,level);
        }
        return  insertCount == 0 ? responBasicError():responOk("");
    }
    @RequestMapping(value = "/deletelevelbyid")
    public Respon deleteLevelById(String id) {
        int count = Delete.deleteDataByPri(SysConn,"level", id);
        return responOk(count);
    }

    @RequestMapping(value = "/findLevelAndParentId")
    public Respon findLevelAndParentId(String id) {
        JSONObject json = levelService.findDataById(id);
        levelService.findParent(json);

        return responOk(json);
    }

}
