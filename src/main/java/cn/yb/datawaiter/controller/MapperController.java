package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.*;
import cn.yb.datawaiter.service.MapperService;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.service.impl.IMapperService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/mapper")
public class MapperController extends BasicController {
    @Autowired
    private IMapperService mapperService;
    @Autowired
    private  IDatawaiterService datawaiterService;

    @RequestMapping(value = "/findAll")
    public Respon findAll() {
        return responOk(Select.findDataAllByPoName(SysConn, Mapper.class));
    }

    @RequestMapping(value = "/findMappersByDatabaseIdAndTableName")
    public Respon findMappersByDatabaseIdAndTableName(String databaseId,String tableName) {
        List<Mapper> mappers = mapperService.findMappersByDatabaseIdAndTableName(databaseId,tableName);
        return responOk(mappers);
    }
    @RequestMapping(value = "/findMappersByDatabaseId")
    public Respon findMappersByDatabaseId(String databaseId) {
        List<Mapper> mappers = mapperService.findMappersByDatabaseId(databaseId);
        return responOk(mappers);
    }

    @RequestMapping(value = "/findMapperById")
    public Respon findMapperById(String id) {
       Mapper mapper = mapperService.findMapperById(id);
       return responOk(mapper);
    }

    @PostMapping("/autoCreateMapper")
    @ResponseBody
    public Respon autoCreateMapper(String databaseId, String tableName, String autoCreateMapperArray) {
        Connection coon = Connect.getSQLConnection(databaseId);
        Table table = Connect.getTable(coon, tableName);
        List<AutoCreateMapper> autos = JSONObject.parseArray(autoCreateMapperArray, AutoCreateMapper.class);
        List<Mapper> mappers = mapperService.createMapper(databaseId, table, autos);
        if (!Tool.isEmpty(mappers)) {
            return responOk(mappers);
        }
        return responError("创建失败");
    }
    @PostMapping("/saveMappers")
    public Respon saveMappers(@RequestBody List<Mapper> mappers) {
        if (!Tool.isEmpty(mappers)) {
            int count = mapperService.saveMappers(mappers);
            return responOk(count);
        }
        return responError("保存失败");
    }
    @PostMapping("/editMapper")
    public Respon editMapper(@RequestBody Mapper mapper) {
        if (mapper != null) {
            int count = mapperService.editMapper(mapper);
            return responOk(count);
        }
        return responError("保存失败");
    }

    @PostMapping("/mapperTest")
    public Respon mapperTest(@RequestBody Mapper mapper) {
        if (mapper != null) {
            if(mapper.getCrud() == CRUDEnum.SELECT){
                return responOk(datawaiterService.findDataByMapper(mapper));
            }else{
                int count = datawaiterService.mapperTest(mapper);
                return responOk(count);
            }
        }
        return responError("测试失败");
    }

    @PostMapping("/deleteMapper")
    public Respon deleteMapper(@RequestBody Mapper mapper) {
        if (mapper != null) {
            int count = mapperService.deleteMapper(mapper);
            return responOk(count);
        }
        return responError("删除失败");
    }

}
