package cn.yb.sys.controller;

import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.Field;
import cn.yb.datawaiter.mapper.ApiMapper;
import cn.yb.datawaiter.model.entity.Respon;
import cn.yb.sys.model.Dic;
import cn.yb.sys.model.ProjectDic;
import cn.yb.sys.service.impl.IDicService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.sql.Connection;
import java.util.*;

import static cn.yb.datawaiter.jdbc.Select.getSQL;

@Controller
@RestController
@RequestMapping(value = "/projectdic")
public class DicController extends BasicController {
    @Autowired
    private IDicService dicService;




    @PostMapping("/saveprojectdic")
    public Respon saveprojectdic(@RequestBody ProjectDic projectdic) {
        Respon respon = startRespon();
        int count = Insert.insertManyPosInService(getSystemConnect(),projectdic);

        return respon.ok(count);
    }

    @PostMapping("/updateprojectdic")
    public Respon updateprojectdic(@RequestBody ProjectDic projectdic) {
        Respon respon = startRespon();
        int count = Update.updateManyDataPosInService(getSystemConnect(),projectdic);

        return respon.ok(count);
    }

    @PostMapping("/deleteProjectDic")
    public Respon deleteProjectDic(@RequestBody ProjectDic projectdic) {
        Respon respon = startRespon();
        int count = Delete.deleteDataInServerice(getSystemConnect(),projectdic);

        return respon.ok(count);
    }

    @PostMapping("/saveDic")
    public Respon saveDic(String projectdicid, @RequestBody Dic dic) {
        Respon respon = startRespon();
        ProjectDic projectDic = Select.findDataById2Po(getSystemConnect(),ProjectDic.class,projectdicid);
        Connection connect = Connect.getSQLConnection(projectDic.getDatabaseconnectid());
        int count  = Insert.insertJSONInService(connect,projectDic.getDictablename(),dic);
        return respon.ok(count);
    }
    @PostMapping("/updateDic")
    public Respon updateDic(String projectdicid, @RequestBody Dic dic) {
        Respon respon = startRespon();
        ProjectDic projectDic = Select.findDataById2Po(getSystemConnect(),ProjectDic.class,projectdicid);
        Connection connect = Connect.getSQLConnection(projectDic.getDatabaseconnectid());
        int count  = Update.updateJSONInService(connect,projectDic.getDictablename(),dic);
        return respon.ok(count);
    }

    @PostMapping("/deleteDic")
    public Respon deleteDic(String projectdicid, @RequestBody Dic dic) {
        Respon respon = startRespon();
        ProjectDic projectDic = Select.findDataById2Po(getSystemConnect(),ProjectDic.class,projectdicid);
        Connection connect = Connect.getSQLConnection(projectDic.getDatabaseconnectid());
        int count = Delete.deleteDataByPriInServerice(connect,projectDic.getDictablename(),dic);

        return respon.ok(count);
    }

    @RequestMapping(value = "/findbyprojectid")
    public Respon findByProjectid(String projectid) {
        Respon respon = startRespon();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(" projectid = ", projectid);
        List<ProjectDic> dic =  Select.findDataByMap(getSystemConnect(),ProjectDic.class,map);
        return respon.ok(dic);
    }
    @RequestMapping(value = "/findAllByProjectid")
    public Respon findAllByProjectid(String projectid) {
        Respon respon = startRespon();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(" projectid = ", projectid);
        List<ProjectDic> projectDics =  Select.findDataByMap(getSystemConnect(),ProjectDic.class,map);
        for(ProjectDic projectDic :projectDics){
            projectDic.setDicArray(dicService.findAllDic(projectDic));
        }
        return respon.ok(projectDics);
    }

    @RequestMapping(value = "/finddicgroup")
    public Respon findDicGroupAll(String databaseconnectid,String dictablename) {
        Respon respon = startRespon();
        String sql ="SELECT * FROM (\n" +
                "SELECT dicgroup,dicgrouplabel,count(*) as childrencount FROM "+dictablename+" GROUP BY dicgroup,dicgrouplabel) dicgroup\n" +
                "\n" +
                "LEFT JOIN (SELECT\t id, seq,dicgroup dicgroup_ FROM "+dictablename+" WHERE  `key` IS NULL OR `key` = '' ) as dic\n" +
                "\n" +
                "ON dicgroup.dicgroup = dic.dicgroup_ ORDER BY seq";
        List<JSONObject> dic =  Select.findDataBySQL(Connect.getSQLConnection(databaseconnectid),sql);
        return respon.ok(dic);
    }

    @RequestMapping(value = "/findDicGroupInAll")
    public Respon findDicGroupInAll(String databaseconnectid,String dictablename,String dicgroup) {
        Respon respon = startRespon();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(" dicgroup = ", dicgroup);
        map.put(" ORDER BY  seq ", null);
        List<JSONObject> dic =  Select.findDataByMap(Connect.getSQLConnection(databaseconnectid),dictablename,map);
        return respon.ok(dic);
    }

    @PostMapping("/setFiledDic")
    public Respon setFiledDic(@RequestBody Field Field) {
        Respon respon = startRespon();
       /* ProjectDic projectDic = Select.findDataById2Po(getSystemConnect(),ProjectDic.class,projectdicid);
        Connection connect = Connect.getSQLConnection(projectDic.getDatabaseconnectid());
        int count  = Update.updateJSONInService(connect,projectDic.getDictablename(),dic);*/
        return respon.ok(0);
    }


}
