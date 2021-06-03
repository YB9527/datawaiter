package cn.yb.datawaiter.service;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.model.*;
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
    public List<JSONObject> findDataByMapper(Mapper mapper) {
        if (mapper != null) {
            String connId = mapper.getDatabaseId();
            Connection conn = Connect.getSQLConnection(connId);
            String sql = mapper.getSql_();
            List<ResultColumn> resultColumns = mapper.getResultColumns();
            if (resultColumns != null) {
                for (ResultColumn resultColumn : resultColumns) {
                    if (resultColumn.getPoRelation() == PoRelation.no) {
                        String property = resultColumn.getProperty();
                        if (property != null) {
                            String value = resultColumn.getTestValue();
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
                               }else{
                                   sql = sql.replace(property,value );
                               }

                           }
                        }
                    }

                }
            }
            List<JSONObject> jsons = Select.findDataBySQL(conn, sql);
            setDataByPoReation(jsons, resultColumns);
            return jsons;
        }
        return null;
    }

    private void setDataByPoReation(List<JSONObject> jsons, List<ResultColumn> resultColumns) {
        for (ResultColumn rc : resultColumns) {
            if (rc.getPoRelation() == PoRelation.no) {
                continue;
            }
            Mapper childMapper = mapperService.findMapperById(rc.getColumn_MapperId());
            if (childMapper == null) {
                return;
            }
            for (JSONObject json : jsons) {
                for (ResultColumn childRc : childMapper.getResultColumns()) {
                    String childProperty = childRc.getProperty();
                    if (rc.getColumn_().equals(childProperty)) {
                        String testValue = json.getString(rc.getProperty());
                        childRc.setTestValue(testValue);
                        List<JSONObject> childDatas = findDataByMapper(childMapper);
                        switch (rc.getPoRelation()) {
                            case association:
                                if (!Tool.isEmpty(childDatas)) {
                                    json.replace(rc.getProperty(), childDatas.get(0));
                                } else {
                                    json.replace(rc.getProperty(), null);
                                }
                                break;
                            case collection:
                                json.replace(rc.getProperty(), childDatas);
                                break;
                        }
                    }
                }

            }

        }
    }


    @Override
    public List<JSONObject> findDataByMapper(Api api, Map<String, String> paramMap) {
        Mapper mapper = mapperService.findMapperById(api.getMapperId());
        for (ResultColumn resultColumn : mapper.getResultColumns()){
            String key = resultColumn.getProperty().replace("[","").replace("]","");
            String value = paramMap.get(key);
            resultColumn.setTestValue(value);
            if(value == null){
                throw  new GlobRuntimeException("api "+api.getLabel()+",没有传递这个参数："+key);
            }
          /*  if(resultColumn.getProperty().equals(param.getParamName())){

            }*/

        }
        return findDataByMapper(mapper);
    }

    @Override
    public int handleData(Api api, Map<String, String> paramMap) {
        Mapper mapper = mapperService.findMapperById(api.getMapperId());
        if(mapper == null){
            throw  new GlobRuntimeException("mapper 查不到，id："+api.getMapperId());
        }
        return mapperService.handelData(api.getCrud(),mapper, paramMap);
    }

    @Override
    public int handleData(Api api, JSONObject jsonObject) {
        Mapper mapper = mapperService.findMapperById(api.getMapperId());
        if(mapper == null){
            throw  new GlobRuntimeException("mapper 查不到，id："+api.getMapperId());
        }
        return mapperService.handelData(api.getCrud(),mapper, jsonObject);
    }

}
