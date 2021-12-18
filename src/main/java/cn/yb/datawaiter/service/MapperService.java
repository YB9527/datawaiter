package cn.yb.datawaiter.service;

import cn.yb.datawaiter.dao.impl.IMapperDao;
import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.model.*;
import cn.yb.datawaiter.model.entity.*;
import cn.yb.datawaiter.service.impl.IDatabaseService;
import cn.yb.datawaiter.service.impl.IMapperService;
import cn.yb.datawaiter.tools.AnnotationTool;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
public class MapperService implements IMapperService {

    @Autowired
    private IMapperDao mapperDao;
    @Autowired
    private IDatabaseService databaseService;

    @Override
    public List<MapperEntity> createMapper(String databaseId, Table table, List<AutoCreateMapper> autos) {
        if (Tool.isEmpty(databaseId) || table == null || Tool.isEmpty(autos)) {
            return null;
        }
        List<MapperEntity> mapperEntities = new ArrayList<>();
        for (AutoCreateMapper auto : autos) {
            MapperEntity mapperEntity = createMapper(databaseId, table, auto);
            mapperEntities.add(mapperEntity);
        }
        return mapperEntities;
    }


    private MapperEntity createMapper(String databaseId, Table table, AutoCreateMapper auto) {
        DatabaseConnect databaseConnect = databaseService.findDatabaseConnect(databaseId);
        MapperEntity mapperEntity = new MapperEntity();
        String tableName = table.getName().toLowerCase();
        mapperEntity.setDatabaseId(databaseId);
        mapperEntity.setId(UUID.randomUUID().toString());
        mapperEntity.setTableName(tableName);
        List<ResultColumnEntity> resultColumnEntities = new ArrayList<>();
        String sql = "";
        String mapperId = mapperEntity.getId();
        TableColumn pri = table.getColumns().get(table.getPrimaryIndex());
        List<ResultColumnCUD> cuds = new ArrayList<>();
        cuds.add(new ResultColumnCUD(mapperEntity));
        mapperEntity.setResultColumnCUDs(cuds);
        switch (auto) {
            case SelectById:
                sql = "SELECT " + "*" + " FROM " + tableName + " WHERE " + pri.getColumnName() + " = [" + pri.getColumnName() + "]";
                mapperEntity.setCrud(MapperCreateEnum.SELECT);
                resultColumnEntities.add(new ResultColumnEntity(mapperId, pri));
                mapperEntity.setLabel("查找根据ID");
                break;
            case SelectAll:
                sql = "SELECT " + "*" + " FROM " + tableName;
                mapperEntity.setCrud(MapperCreateEnum.SELECT);
                mapperEntity.setLabel("查找所有");
                break;
            case SelectCount:
                sql = "SELECT COUNT(*) as count FROM " + tableName;
                mapperEntity.setCrud(MapperCreateEnum.SELECT);
                mapperEntity.setLabel("查找总条目数");
                break;
            case SelectPagination:

                sql = getSelectPaginationSQL(databaseConnect, tableName);
                mapperEntity.setCrud(MapperCreateEnum.SELECT);
                resultColumnEntities.add(new ResultColumnEntity(mapperId,"START", "0"));
                resultColumnEntities.add(new ResultColumnEntity(mapperId,"END", "10"));
                mapperEntity.setLabel("查找分页");
                break;
            case DeleteByPo:
                mapperEntity.setCrud(MapperCreateEnum.EDIT);

                mapperEntity.setLabel("删除根据对象");
                break;
            case SavePo:
                mapperEntity.setCrud(MapperCreateEnum.EDIT);
                mapperEntity.setLabel("添加对象");
                break;
            case UpdatePo:
                mapperEntity.setCrud(MapperCreateEnum.EDIT);
                mapperEntity.setLabel("修改对象");
                break;
        }
        mapperEntity.setResultColumnEntities(resultColumnEntities);
        mapperEntity.setSql_(sql);
        return mapperEntity;
    }
    public static String getSelectPaginationSQL(DatabaseConnect databaseConnect, String tableName) {
        DatabaseEnum databaseEnum = databaseConnect.getDatabaseEnum();
        switch (databaseEnum){
            case mysql:
                return  "SELECT * FROM " + tableName + " LIMIT [START] , [END]";
            case postgress:
                return  "SELECT  * FROM "+ tableName +" LIMIT [END] OFFSET [START]";
            default:
                throw  new GlobRuntimeException("创建分页，遇到不能识别的数据库类型："+databaseEnum.name());
        }
    }
    @Override
    public int saveMappers(List<MapperEntity> mapperEntities) {
        int count = 0;
        try {
            Connection conn = SystemConnect.getConn();
            conn.setAutoCommit(false);
            List<ResultColumnEntity> rcs = new ArrayList<>();
            List<ResultColumnCUD> cuds = new ArrayList<>();

            for (MapperEntity mapperEntity : mapperEntities) {
                if (!Tool.isEmpty(mapperEntity.getResultColumnEntities())) {
                    rcs.addAll(mapperEntity.getResultColumnEntities());
                }
                if (!Tool.isEmpty(mapperEntity.getResultColumnCUDs())) {
                    cuds.addAll(mapperEntity.getResultColumnCUDs());
                }
            }
            count = Insert.insertManyPos(conn, mapperEntities);
            Insert.insertManyPos(conn, rcs);
            Insert.insertManyPos(conn, cuds);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<JSONObject> findMappersByDatabaseIdAndTableNameAndCount(String databaseId, String tableName) {
        String sql = "SELECT * FROM " +
                "   (SELECT *  FROM mapper WHERE databaseId = " + JDBCUtils.sqlStr(databaseId) + " AND tableName=" + JDBCUtils.sqlStr(tableName)+" ) as mapper"+
                " LEFT JOIN (SELECT mapperId,count(*) as apiCount FROM api GROUP BY mapperId) as api " +
                " on mapper.id=api.mapperId";
        List<JSONObject> list = Select.findDataBySQL(SystemConnect.getConn(), sql);
        return list;
    }

    @Override
    public List<MapperEntity> findMappersByDatabaseIdAndTableName(String databaseId, String tableName) {
        String sql = "SELECT *  FROM mapper WHERE databaseConnectId = " + JDBCUtils.sqlStr(databaseId) +
                " AND tableName=" + JDBCUtils.sqlStr(tableName);
        List<MapperEntity> list = Select.findDataBySQL(SystemConnect.getConn(), sql, MapperEntity.class);
        return list;
    }

    @Override
    public MapperEntity findMapperById(String id) {
        MapperEntity mapperEntity = Select.findDataById2Po(SystemConnect.getConn(), MapperEntity.class, id);
        if (mapperEntity == null) {
            return null;
        }
        List<ResultColumnEntity> list = findResultColumnByMapperId(id);
        mapperEntity.setResultColumnEntities(list);
        return mapperEntity;
    }

    private List<ResultColumnEntity> findResultColumnByMapperId(String id) {
        String selectResultColumnSQL = "SELECT * FROM " + AnnotationTool.getTableName(ResultColumnEntity.class)  + " WHERE mapperId = " + JDBCUtils.sqlStr(id);
        List<ResultColumnEntity> list = Select.findDataBySQL(SystemConnect.getConn(), selectResultColumnSQL, ResultColumnEntity.class);
        return list;
    }

    @Override
    public int editMapper(MapperEntity mapperEntity) {
        Connection conn = SystemConnect.getConn();
        try {
            conn.setAutoCommit(false);//开启事务

            String tableName = AnnotationTool.getTableName(MapperEntity.class);
            String sql = mapperEntity.getSql_().replace("\"", "'");
            mapperEntity.setSql_(sql);
            JSONObject dbPo = Select.findDataById(conn, tableName, mapperEntity.getId());
            List<ResultColumnEntity> resultColumnEntities = mapperEntity.getResultColumnEntities();
            List<ResultColumnCUD> resultColumnCUDs = mapperEntity.getResultColumnCUDs();

            String mapperId = mapperEntity.getId();
            for (ResultColumnEntity rc : resultColumnEntities) {
                rc.setMapperId(mapperId);
            }
            clearNullData(resultColumnEntities);
            int count;
            if (dbPo == null) {
                //保存
                count = Insert.insertPo(conn, mapperEntity);
            } else {
                //编辑
                count = Update.updateDataPo(conn, mapperEntity);
                Delete.deleteByColoumnAndValues(conn,AnnotationTool.getTableName(ResultColumnEntity.class), "mapperId", new String[]{mapperEntity.getId()});
            }
            if (resultColumnCUDs != null) {
                Delete.deleteByColoumnAndValues(conn,AnnotationTool.getTableName(ResultColumnCUD.class)  , "mapperId", new String[]{mapperEntity.getId()});
                List<ResultColumnCUD> all = findResultColumnCUDAll(resultColumnCUDs);
                Insert.insertManyPos(conn, all);
            }
            int count2 = Insert.insertManyPos(conn, resultColumnEntities);
            conn.commit();
            return count;
        } catch (Exception e) {
            try {
                conn.rollback();//回滚事务
                throw new GlobRuntimeException(e.getMessage());
            } catch (SQLException ex) {
                throw new GlobRuntimeException(ex.getMessage());
            }
        }
    }

    private List<ResultColumnCUD> findResultColumnCUDAll(List<ResultColumnCUD> cuds) {
        List<ResultColumnCUD> list = new ArrayList<>();
        for (ResultColumnCUD cud : cuds) {
            findResultColumnCUDAll1(list, cud);
        }
        return list;
    }

    private void findResultColumnCUDAll1(List<ResultColumnCUD> list, ResultColumnCUD cud) {
        List<ResultColumnCUD> childs = cud.getResultColumnCUDs();
        list.add(cud);
        if (childs != null) {
            for (ResultColumnCUD child : childs) {
                findResultColumnCUDAll1(list, child);
            }
        }
    }

    private void clearNullData(List<ResultColumnEntity> resultColumnEntities) {
        for (int i = 0; i < resultColumnEntities.size(); i++) {
            ResultColumnEntity rc = resultColumnEntities.get(i);
            if (rc == null || rc.getProperty() == null || rc.getPoRelation() == null || rc.getMapperId() == null) {
                resultColumnEntities.remove(i);
                i--;
            }
        }
    }

    @Override
    public int deleteMapper(MapperEntity mapperEntity) {
        int count = 0;
        try {
            Connection conn = SystemConnect.getConn();
            conn.setAutoCommit(false);//开启事务
            Delete.deleteDataByPri(conn,AnnotationTool.getTableName(MapperEntity.class)  , mapperEntity.getId());
            List<ResultColumnEntity> resultColumnEntities = mapperEntity.getResultColumnEntities();
            count = Delete.deleteDataByPri(conn, resultColumnEntities);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<MapperEntity> findMappersByDatabaseId(String databaseId) {
        String sql = "SELECT *  FROM mapper WHERE databaseId = " + JDBCUtils.sqlStr(databaseId);
        List<MapperEntity> list = Select.findDataBySQL(SystemConnect.getConn(), sql, MapperEntity.class);
        for (MapperEntity mapperEntity : list) {
            mapperEntity.setResultColumnEntities(findResultColumnByMapperId(mapperEntity.getId()));
        }
        return list;
    }

    @Override
    public int editResultColumnCUD(ResultColumnCUD resultColumnCUD) {
        Connection conn = SystemConnect.getConn();
        int count = 0;
        try {
            conn.setAutoCommit(false);//开启事务
            count = JDBCUtils.editPo(conn, resultColumnCUD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<ResultColumnCUD> findResultColumnCUDByMapperId(String mapperId) {
        String sql = "SELECT *  FROM " + AnnotationTool.getTableName(ResultColumnCUD.class)
                + " WHERE mapperId = " + JDBCUtils.sqlStr(mapperId);
        List<ResultColumnCUD> cuds = Select.findDataBySQL(SystemConnect.getConn(), sql, ResultColumnCUD.class);
        List<ResultColumnCUD> roots = findResultColumnRoots(cuds);
        setResultColumnCUDChilds(roots, cuds);
        return roots;
    }


    private void setResultColumnCUDChilds(List<ResultColumnCUD> roots, List<ResultColumnCUD> childs) {
        for (ResultColumnCUD root : roots) {
            setResultColumnCUDChilds(root, childs);
        }
    }

    private void setResultColumnCUDChilds(ResultColumnCUD parent, List<ResultColumnCUD> childs) {
        String parentId = parent.getId();
        for (int i = 0; i < childs.size(); i++) {
            ResultColumnCUD child = childs.get(i);
            if (parentId.equals(child.getParentId())) {
                List<ResultColumnCUD> parentChilds = parent.getResultColumnCUDs();
                if (parentChilds == null) {
                    parentChilds = new ArrayList<>();
                    parent.setResultColumnCUDs(parentChilds);
                }
                parentChilds.add(child);
                childs.remove(i);
                i--;
            }
        }
    }

    private List<ResultColumnCUD> findResultColumnRoots(List<ResultColumnCUD> cuds) {
        List<ResultColumnCUD> root = new ArrayList<>();
        for (int i = 0; i < cuds.size(); i++) {
            ResultColumnCUD cud = cuds.get(i);
            if (cud.getParentId() == null) {
                root.add(cud);
                cuds.remove(i);
                i--;
            }
        }
        return root;
    }

    @Override
    public int handelData(CRUDEnum crud, MapperEntity mapperEntity, Map<String, String> dataMap) {
        JSONObject jsonObject =  JSONObject.parseObject(JSONObject.toJSONString(dataMap));
        return   handelData(crud, mapperEntity, jsonObject);
       /* Map<String, List<JSONObject>> tableMap = new HashMap<>();

        List<ResultColumnCUD> resultColumns = findResultColumnCUDByMapperId(mapper.getId());
        if (resultColumns != null) {
            for (ResultColumnCUD resultColumn : resultColumns) {
                handelData(tableMap, resultColumn, JSONObject.parseObject(JSONObject.toJSONString(dataMap)));

            }
        }
        Connection conn = Connect.getSQLConnection(mapper.getDatabaseId());
        try {
            conn.setAutoCommit(false);//开启事务
            int count = 0;
            switch (crud) {
                case INSERT:
                    count = Insert.insertManyJSONs(conn, tableMap);
                    break;
                case DELETE:
                    count = Delete.deleteDataByPri(conn, tableMap);
                    break;
                case UPDATE:
                    count = Update.updateDataJSON(conn, tableMap);
                    break;
            }
            conn.commit();
            return count;
        } catch (Exception e) {
            try {
                conn.rollback();//回滚事务
                throw new GlobRuntimeException(e.getMessage());
            } catch (SQLException ex) {
                throw new GlobRuntimeException(ex.getMessage());
            }
        }*/
    }

    @Override
    public int handelData(CRUDEnum crud, MapperEntity mapperEntity, JSONObject jsonObject) {

        Map<String, List<JSONObject>> tableMap = new HashMap<>();

        List<ResultColumnCUD> resultColumns = findResultColumnCUDByMapperId(mapperEntity.getId());
        if (resultColumns != null) {
            for (ResultColumnCUD resultColumn : resultColumns) {
                handelData(tableMap, resultColumn, jsonObject);
            }
        }
        Connection conn = Connect.getSQLConnection(mapperEntity.getDatabaseId());
        try {
            conn.setAutoCommit(false);//开启事务
            int count = 0;
            switch (crud) {
                case INSERT:
                    count = Insert.insertManyJSONs(conn, tableMap);
                    break;
                case DELETE:
                    count = Delete.deleteDataByPri(conn, tableMap);
                    break;
                case UPDATE:
                    count = Update.updateDataJSON(conn, tableMap);
                    break;
            }
            conn.commit();
            return count;
        } catch (Exception e) {
            try {
                conn.rollback();//回滚事务
                throw new GlobRuntimeException(e.getMessage());
            } catch (SQLException ex) {
                throw new GlobRuntimeException(ex.getMessage());
            }
        }
    }


    private void handelData(Map<String, List<JSONObject>> tableMap, ResultColumnCUD cud, JSONObject dataMap) {
        String property = cud.getProperty();
        String tableName = cud.getTableName();
        String str = dataMap.getString(property);
        if (Tool.isEmpty(str)) {
            JSONArray tem = dataMap.getJSONArray("_Array_");
            if(tem != null){
                str =JSONArray.toJSONString(tem);
            }else if(str == null){
                str = dataMap.toJSONString();
                if(str == null){
                    return;
                }
            }
        }
        JSONArray jsonArray;
        if (str.startsWith("[")) {
            jsonArray = JSONArray.parseArray(str);
        } else {
            JSONObject json = JSON.parseObject(str);
            jsonArray = new JSONArray();
            jsonArray.add(json);
        }
        List<JSONObject> all = tableMap.get(tableName);
        if (all == null) {
            all = new ArrayList<>();
            tableMap.put(tableName, all);
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            all.add((JSONObject) jsonArray.get(i));
        }
        List<ResultColumnCUD> childs = cud.getResultColumnCUDs();
        if (childs != null) {
            for (ResultColumnCUD child : childs) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    handelData(tableMap, child, (JSONObject) jsonArray.get(i));
                }
            }
        }
    }
}
