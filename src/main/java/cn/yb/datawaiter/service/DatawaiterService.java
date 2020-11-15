package cn.yb.datawaiter.service;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.model.*;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.service.impl.IMapperService;
import cn.yb.datawaiter.service.impl.ISysService;
import cn.yb.datawaiter.tools.ReflectTool;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatawaiterService implements IDatawaiterService {

    @Autowired
    private ISysService sysService;
    @Autowired
    private IMapperService mapperService;
    private static String ARRAY = "Array";

    @Override
    public List<JSONObject> findDataByApi(Api api) {
        if (api != null) {
            String connId = api.getDatabaseConnectId();
            Connection conn = Connect.getSQLConnection(connId);
            String sql = api.getSql_();
            List<Param> params = api.getParams();
            if (params != null) {
                for (Param param : params) {
                    String paramName = param.getParamName();
                    if (paramName != null) {
                        String value = param.getTestValue();
                        value = value == null ? "" : value;
                        sql = sql.replace(paramName, value);
                    }
                }
            }
            return Select.findDataBySQL(conn, sql);
        }
        return null;
    }

    @Override
    public int handleData(Api api) {
        if (Tool.isEmpty(api.getParams())) {
            return 0;
        }
        int count = 0;
        Connection conn = Connect.getSQLConnection(api.getDatabaseConnectId());
        Map<String, List<JSONObject>> tableMap = getTableJSON(api.getParams());
        switch (api.getCrud()) {
            case INSERT:
                count = insertData(conn, tableMap);
                break;
            case DELETE:
                count = deleteData(conn, tableMap);
                break;
            case UPDATE:
                count = updateData(conn, tableMap);
                break;
        }
        return count;
    }

    private int updateData(Connection conn, Map<String, List<JSONObject>> tableMap) {
        int count = 0;
        for (String tableName : tableMap.keySet()) {
            count += Update.updateManyDatas(conn, tableName, tableMap.get(tableName));
        }
        return count;
    }

    private int deleteData(Connection conn, Map<String, List<JSONObject>> tableMap) {
        int count = 0;
        for (String tableName : tableMap.keySet()) {
            count += Delete.deleteDataByPri(conn, tableName, tableMap.get(tableName));
        }
        return count;
    }

    private int insertData(Connection conn, Map<String, List<JSONObject>> tableMap) {
        int count = 0;
        for (String tableName : tableMap.keySet()) {
            count += Insert.insertManyJSONs(conn, tableName, tableMap.get(tableName));
        }
        return count;
    }

    private Map<String, List<JSONObject>> getTableJSON(List<Param> params) {
        Map<String, List<JSONObject>> jsonMap = new HashMap<>();
        Map<String, List<Param>> paramMap = ReflectTool.getListIDMap("getParamName", params);
        for (String tableName : paramMap.keySet()) {
            List<JSONObject> jsons = new ArrayList<>();
            for (Param param : paramMap.get(tableName)) {
                tableName = tableName.replace("[", "").replace("]", "");
                if (tableName.endsWith(ARRAY)) {
                    tableName = tableName.replace("Array", "");
                    tableName = tableName.replace(ARRAY, "");
                    JSONArray jsonArray = JSONArray.parseArray(param.getTestValue());
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            jsons.add((JSONObject) jsonArray.get(i));
                        }
                    }
                } else {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(param.getTestValue());
                        if (jsonObject != null) {
                            jsons.add(jsonObject);
                        }
                    } catch (Exception e) {
                        throw new GlobRuntimeException("上传的不是 JSON 对象：" + param.getTestValue());
                    }

                }
                List<JSONObject> jj = jsonMap.get(tableName);
                if (jj == null) {
                    jj = new ArrayList<>();
                    jsonMap.put(tableName, jj);
                }
                jj.addAll(jsons);

            }
        }
        return jsonMap;
    }

    @Override
    public List<JSONObject> findDataByMapper(Mapper mapper) {


        if (mapper != null) {
            String connId = mapper.getDatabaseId();
            Connection conn = Connect.getSQLConnection(connId);
            String sql = mapper.getSql_();
            List<ResultColumn> resultColumns = mapper.getResultColumns();
            if (resultColumns != null) {
                for (ResultColumn resultColumn : resultColumns) {
                    if(resultColumn.getPoRelation() == PoRelation.no){
                        String property = resultColumn.getProperty();
                        if (property != null) {
                            String value = resultColumn.getTestValue();
                            value = "".equals(value)  ? "''" : value;
                            sql = sql.replace(property, value);
                        }
                    }

                }
            }
            List<JSONObject> jsons = Select.findDataBySQL(conn, sql);
            setDataByPoReation(jsons, resultColumns);
            return  jsons;
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
                for (ResultColumn childRc : childMapper.getResultColumns()){
                    String childProperty = childRc.getProperty();
                    if(rc.getColumn_().equals(childProperty)){
                        String testValue = json.getString(rc.getProperty());
                        childRc.setTestValue(testValue);
                        List<JSONObject> childDatas = findDataByMapper(childMapper);
                        switch (rc.getPoRelation()){
                            case association:
                                if(!Tool.isEmpty(childDatas)){
                                    json.replace(rc.getProperty(),childDatas.get(0));
                                }else{
                                    json.replace(rc.getProperty(),null);
                                }
                                break;
                            case collection:
                                json.replace(rc.getProperty(),childDatas);
                                break;
                        }
                    }
                }

            }

        }
    }

    @Override
    public int mapperTest(Mapper mapper) {
        return 0;
    }

}
