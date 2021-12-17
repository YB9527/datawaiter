package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.entity.*;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.service.impl.IMapperService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

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
        Respon respon = startRespon();
        return respon.ok(Select.findDataAllByPoName(SysConn, MapperEntity.class));
    }

    @RequestMapping(value = "/findMappersByDatabaseIdAndTableName")
    public Respon findMappersByDatabaseIdAndTableName(String databaseId,String tableName) {
        Respon respon = startRespon();
        List<JSONObject> mappers = mapperService.findMappersByDatabaseIdAndTableNameAndCount(databaseId,tableName);
        return  respon.ok(mappers);
    }
    @RequestMapping(value = "/findMappersByDatabaseId")
    public Respon findMappersByDatabaseId(String databaseId) {
        Respon respon = startRespon();
        List<MapperEntity> mapperEntities = mapperService.findMappersByDatabaseId(databaseId);
        return  respon.ok(mapperEntities);
    }


    @RequestMapping(value = "/findResultColumnCUDByMapperId")
    public Respon findResultColumnCUDByMapperId(String mapperId) {
        Respon respon = startRespon();
        List<ResultColumnCUD> mappers = mapperService.findResultColumnCUDByMapperId(mapperId);
        return respon.ok(mappers);
    }

    @RequestMapping(value = "/findMapperById")
    public Respon findMapperById(String id) {
        Respon respon = startRespon();
       MapperEntity mapperEntity = mapperService.findMapperById(id);
       return respon.ok(mapperEntity);
    }

    @PostMapping("/autoCreateMapper")
    @ResponseBody
    public Respon autoCreateMapper(String databaseId, String tableName, String autoCreateMapperArray) throws SQLException {
        Respon respon = startRespon();
        Connection coon = Connect.getSQLConnection(databaseId);
        coon.setAutoCommit(false);
        Table table = Connect.getTable(coon, tableName);
        List<AutoCreateMapper> autos = JSONObject.parseArray(autoCreateMapperArray, AutoCreateMapper.class);
        List<MapperEntity> mapperEntities = mapperService.createMapper(databaseId, table, autos);
        coon.commit();
        if (!Tool.isEmpty(mapperEntities)) {
            return respon.ok(mapperEntities);
        }
        return respon.responError("创建失败");
    }
    @PostMapping("/saveMappers")
    public Respon saveMappers(@RequestBody List<MapperEntity> mapperEntities) {
        Respon respon = startRespon();
        if (!Tool.isEmpty(mapperEntities)) {
            int count = mapperService.saveMappers(mapperEntities);
            return  respon.ok(count);
        }
        return respon.responError("保存失败");
    }
    @PostMapping("/editMapper")
    public Respon editMapper(@RequestBody MapperEntity mapperEntity) {
        Respon respon = startRespon();
        if (mapperEntity != null) {
            int count = mapperService.editMapper(mapperEntity);
            return respon.ok(count);
        }
        return respon.responError("保存失败");
    }

    @PostMapping("/editResultColumnCUD")
    public Respon editMapper(@RequestBody ResultColumnCUD resultColumnCUD) {
        Respon respon = startRespon();
        if (resultColumnCUD != null) {
            int count = mapperService.editResultColumnCUD(resultColumnCUD);
            return respon.ok(count);
        }
        return respon.responError("保存失败");
    }
    @PostMapping("/mapperTest")
    public Respon mapperTest(@RequestBody JSONObject josn) {
        Respon respon = startRespon();
        MapperEntity mapperEntity = josn.toJavaObject(MapperEntity.class);
        if (mapperEntity != null) {
            if(mapperEntity.getCrud() == MapperCreateEnum.SELECT){
                respon.ok(datawaiterService.findDataByMapper(mapperEntity));
            }else{
                // int count = datawaiterService.mapperTest(mapper);
                //responOk(respon,count);
            }
            return  respon;
        }
        return respon.responError("测试失败");
    }
    @PostMapping("/mapperTest1")
    public Respon mapperTest1(@RequestBody MapperEntity mapperEntity) {
        Respon respon = startRespon();
        if (mapperEntity != null) {
            if(mapperEntity.getCrud() == MapperCreateEnum.SELECT){
                respon.ok(datawaiterService.findDataByMapper(mapperEntity));
            }else{
               // int count = datawaiterService.mapperTest(mapper);
                 //responOk(respon,count);
            }
            return  respon;
        }
        return respon.responError("测试失败");
    }



    @PostMapping("/deleteMapper")
    public Respon deleteMapper(@RequestBody MapperEntity mapperEntity) {
        Respon respon = startRespon();
        if (mapperEntity != null) {
            Respon res =  startRespon();
            int count = mapperService.deleteMapper(mapperEntity);
            return res.ok(count);
        }
        return  respon.responError("删除失败");
    }

    @PostMapping("/mapperTesthandel")
    public Respon mapperTesthandel(HttpServletRequest request, HttpServletResponse response) {
        Respon respon = startRespon();
        try {
            Map<String, String> paramMap = getAllRequestParam(request);
            MapperEntity mapperEntity = mapperService.findMapperById(paramMap.get("mapperId"));
            paramMap.remove("mapperId");
            if(mapperEntity != null){
                if(mapperEntity.getCrud() != MapperCreateEnum.SELECT){
                    int count =  mapperService.handelData(CRUDEnum.SELECT, mapperEntity,paramMap);
                    return respon.ok(count);
                }
            }
            return respon.responError("URL地址有问题：");
        } catch (GlobRuntimeException e) {
            return respon.responError(e.getMessage());
        }
    }

    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }
        return res;
    }

}
