package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.AutoCreateMapper;
import cn.yb.datawaiter.model.Mapper;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.model.ResponTotal;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api/database")
public class SQLController extends  BasicController{

    @PostMapping("/sql")
    @ResponseBody
    public Respon sql(String sql) throws SQLException {

        Respon respon = startRespon();
        Connection coon = Connect.getSQLConnection("1");
        List<JSONObject> jsons =  Select.findDataBySQL(coon,sql);
      /*  Connection coon = Connect.getSQLConnection(databaseId);
        coon.setAutoCommit(false);
        Table table = Connect.getTable(coon, tableName);
        List<AutoCreateMapper> autos = JSONObject.parseArray(autoCreateMapperArray, AutoCreateMapper.class);
        List<Mapper> mappers = mapperService.createMapper(databaseId, table, autos);
        coon.commit();
        if (!Tool.isEmpty(mappers)) {
            return respon.ok(mappers);
        }*/
        return respon.ok(jsons);
    }
    @PostMapping("/sqlpage")
    @ResponseBody
    public Respon sql(String sql,Integer limit,Integer page) throws SQLException {

        ResponTotal respon = startResponTotal();
        Connection coon = Connect.getSQLConnection("1");
        int total =  Select.findDataBySQL(coon,sql).size();
        sql = sql +" LIMIT "+limit+" OFFSET "+((page -1) *limit);
        List<JSONObject> jsons =  Select.findDataBySQL(coon,sql);
        /*Connection coon = Connect.getSQLConnection(databaseId);
        coon.setAutoCommit(false);
        Table table = Connect.getTable(coon, tableName);
        List<AutoCreateMapper> autos = JSONObject.parseArray(autoCreateMapperArray, AutoCreateMapper.class);
        List<Mapper> mappers = mapperService.createMapper(databaseId, table, autos);
        coon.commit();
        if (!Tool.isEmpty(mappers)) {
            return respon.ok(mappers);
        }*/
        return respon.ok(total,jsons);
    }
}
