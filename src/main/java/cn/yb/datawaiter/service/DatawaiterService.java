package cn.yb.datawaiter.service;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.model.entity.ApiEntity;
import cn.yb.datawaiter.model.entity.MapperEntity;
import cn.yb.datawaiter.model.entity.PoRelation;
import cn.yb.datawaiter.model.entity.ResultColumnEntity;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.service.impl.IMapperService;
import cn.yb.datawaiter.service.impl.ISysService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Service
public class DatawaiterService implements IDatawaiterService {

    @Autowired
    private ISysService sysService;
    @Autowired
    private IMapperService mapperService;
    //private static String ARRAY = "Array";

    @Override
    public List<JSONObject> findDataByMapper(MapperEntity mapperEntity) {
        if (mapperEntity != null) {
            String connId = mapperEntity.getDatabaseId();
            Connection conn = Connect.getSQLConnection(connId);
            String sql = mapperEntity.getSql_();
            List<ResultColumnEntity> resultColumnEntities = mapperEntity.getResultColumnEntities();
            if (resultColumnEntities != null) {
                for (ResultColumnEntity resultColumnEntity : resultColumnEntities) {
                    if (resultColumnEntity.getPoRelation() == PoRelation.no) {
                        String property = resultColumnEntity.getProperty();
                        if (property != null) {
                            String value = resultColumnEntity.getTestValue();
                           if(value != null && value.matches("[0-9]+")){
                               sql = sql.replace(property, value);
                           }else{
                               /*//检查前面是否有in
                               int index =  sql.lastIndexOf("IN",sql.indexOf(property));
                               if(index != -1){
                                   //检查中间是否有其他字符串
                               }*/
                               //value = value == null || "".equals(value) ? "''" : "'" + value + "'";
                               if(value != null  && value instanceof  String){
                                   sql = sql.replace(property,"'" + value+"'" );

                               }else {
                                   sql = sql.replace(property,value );
                               }

                           }
                        }
                    }

                }
            }
            List<JSONObject> jsons = Select.findDataBySQL(conn, sql);
            setDataByPoReation(jsons, resultColumnEntities);
            return jsons;
        }
        return null;
    }

    private void setDataByPoReation(List<JSONObject> jsons, List<ResultColumnEntity> resultColumnEntities) {
        for (ResultColumnEntity rc : resultColumnEntities) {
            if (rc.getPoRelation() == PoRelation.no) {
                continue;
            }
            MapperEntity childMapperEntity = mapperService.findMapperById(rc.getColumn_MapperId());
            if (childMapperEntity == null) {
                return;
            }
            for (JSONObject json : jsons) {
                for (ResultColumnEntity childRc : childMapperEntity.getResultColumnEntities()) {
                    String childProperty = childRc.getProperty();
                    if (rc.getColumn_().equals(childProperty)) {
                        String property =  rc.getProperty().trim();
                        String testValue = json.getString(property);
                        childRc.setTestValue(testValue);
                        List<JSONObject> childDatas = findDataByMapper(childMapperEntity);
                        switch (rc.getPoRelation()) {
                            case association:
                                if (!Tool.isEmpty(childDatas)) {
                                    json.replace(rc.getProperty(), childDatas.get(0));
                                } else {
                                    json.replace(rc.getProperty(), null);
                                }
                                break;
                            case collection:
                                json.replace(property, childDatas);
                                break;
                        }
                    }
                }

            }

        }
    }


    @Override
    public List<JSONObject> findDataByMapper(ApiEntity apiEntity, Map<String, String> paramMap) {
        MapperEntity mapperEntity = mapperService.findMapperById(apiEntity.getMapperId());
        for (ResultColumnEntity resultColumnEntity : mapperEntity.getResultColumnEntities()){
            if(resultColumnEntity.getPoRelation() != PoRelation.no){
                continue;
            }
            String key = resultColumnEntity.getProperty().replace("[","").replace("]","");
            String value = paramMap.get(key.trim());
            resultColumnEntity.setTestValue(value);
            if(value == null){
                throw  new GlobRuntimeException("api "+ apiEntity.getLabel()+",没有传递这个参数："+key);
            }
          /*  if(resultColumn.getProperty().equals(param.getParamName())){

            }*/

        }
        return findDataByMapper(mapperEntity);
    }

    @Override
    public int handleData(ApiEntity apiEntity, Map<String, String> paramMap) {
        MapperEntity mapperEntity = mapperService.findMapperById(apiEntity.getMapperId());
        if(mapperEntity == null){
            throw  new GlobRuntimeException("mapper 查不到，id："+ apiEntity.getMapperId());
        }
        return mapperService.handelData(apiEntity.getCrud(), mapperEntity, paramMap);
    }

    @Override
    public int handleData(ApiEntity apiEntity, JSONObject jsonObject) {
        MapperEntity mapperEntity = mapperService.findMapperById(apiEntity.getMapperId());
        if(mapperEntity == null){
            throw  new GlobRuntimeException("mapper 查不到，id："+ apiEntity.getMapperId());
        }
        return mapperService.handelData(apiEntity.getCrud(), mapperEntity, jsonObject);
    }

}
