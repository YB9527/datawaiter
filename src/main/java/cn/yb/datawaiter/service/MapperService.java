package cn.yb.datawaiter.service;

import cn.yb.datawaiter.dao.impl.IMapperDao;
import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.model.*;
import cn.yb.datawaiter.service.impl.IMapperService;
import cn.yb.datawaiter.tools.Tool;
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

    @Override
    public List<Mapper> createMapper(String databaseId, Table table, List<AutoCreateMapper> autos) {
        if (Tool.isEmpty(databaseId) || table == null || Tool.isEmpty(autos)) {
            return null;
        }
        List<Mapper> mappers = new ArrayList<>();
        for (AutoCreateMapper auto : autos) {
            Mapper mapper = createMapper(databaseId, table, auto);
            mappers.add(mapper);
        }
        return mappers;
    }


    private Mapper createMapper(String databaseId, Table table, AutoCreateMapper auto) {
        Mapper mapper = new Mapper();
        String tableName = table.getName();
        mapper.setDatabaseId(databaseId);
        mapper.setId(UUID.randomUUID().toString());
        mapper.setTableName(tableName);
        List<ResultColumn> resultColumns = new ArrayList<>();
        String sql = "";
        String mapperId = mapper.getId();
        TableColumn pri = table.getColumns().get(table.getPrimaryIndex());
        switch (auto) {
            case SelectById:
                sql = "SELECT " + "*" + " FROM " + tableName + " WHERE " + pri.getColumnName() + " = [" + pri.getColumnName() + "]";
                mapper.setCrud(MapperCreateEnum.SELECT);
                resultColumns.add(new ResultColumn(mapperId, pri));
                mapper.setLabel("查找根据ID");
                break;
            case SelectAll:
                sql = "SELECT " + "*" + " FROM " + tableName;
                mapper.setCrud(MapperCreateEnum.SELECT);
                mapper.setLabel("查找所有");
                break;
            case SelectCount:
                sql = "SELECT COUNT(*) as count FROM " + tableName;
                mapper.setCrud(MapperCreateEnum.SELECT);
                mapper.setLabel("查找总条目数");
                break;
            case SelectPagination:
                mapper.setCrud(MapperCreateEnum.SELECT);
                mapper.setLabel("查找分页");
                break;
            case DeleteByPo:
                mapper.setCrud(MapperCreateEnum.EDIT);
                mapper.setLabel("删除根据对象");
                break;
            case SavePo:
                mapper.setCrud(MapperCreateEnum.EDIT);
                mapper.setLabel("添加对象");
                break;
            case UpdatePo:
                mapper.setCrud(MapperCreateEnum.EDIT);
                mapper.setLabel("修改对象");
                break;
        }
        mapper.setResultColumns(resultColumns);
        mapper.setSql_(sql);
        return mapper;
    }

    @Override
    public int saveMappers(List<Mapper> mappers) {
        Connection conn =  SystemConnect.getConn();
        int count = Insert.insertManyPos(conn, mappers);
        List<ResultColumn> rcs = new ArrayList<>();
        for (Mapper mapper : mappers) {
            if (!Tool.isEmpty(mapper.getResultColumns())) {
                rcs.addAll(mapper.getResultColumns());
            }
        }
        try {
            Insert.insertManyPos(conn, rcs);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<Mapper> findMappersByDatabaseIdAndTableName(String databaseId, String tableName) {
        String sql = "SELECT *  FROM Mapper WHERE databaseId = " + JDBCUtils.sqlStr(databaseId) + " AND tableName=" + JDBCUtils.sqlStr(tableName);
        List<Mapper> list = Select.findDataBySQL(SystemConnect.getConn(), sql, Mapper.class);
        return list;
    }

    @Override
    public Mapper findMapperById(String id) {
        Mapper mapper = Select.findDataById2Po(SystemConnect.getConn(), Mapper.class, id);
        String selectResultColumnSQL = "SELECT * FROM " + ResultColumn.class.getSimpleName() + " WHERE mapperId = " + JDBCUtils.sqlStr(id);
        List<ResultColumn> list = Select.findDataBySQL(SystemConnect.getConn(), selectResultColumnSQL, ResultColumn.class);
        mapper.setResultColumns(list);
        return mapper;
    }

    @Override
    public int editMapper(Mapper mapper) {
        Connection conn = SystemConnect.getConn();
        try {
            conn.setAutoCommit(false);//开启事务

            String tableName = Mapper.class.getSimpleName();
            String sql = mapper.getSql_().replace("\"", "'");
            mapper.setSql_(sql);
            JSONObject dbPo = Select.findDataById(conn, tableName, mapper.getId());
            List<ResultColumn> resultColumns = mapper.getResultColumns();
            List<ResultColumnCUD> resultColumnCUDs = mapper.getResultColumnCUDs();

            String mapperId = mapper.getId();
            for (ResultColumn rc : resultColumns) {
                rc.setMapperId(mapperId);
            }
            clearNullData(resultColumns);
            int count;
            if (dbPo == null) {
                //保存
                count = Insert.insertPo(conn, mapper);
            } else {
                //编辑
                count = Update.updateDataPo(conn, mapper);
                Delete.deleteByColoumnAndValues(conn, ResultColumn.class.getSimpleName(), "mapperId", new String[]{mapper.getId()});
            }
            if (resultColumnCUDs != null) {
                Delete.deleteByColoumnAndValues(conn, ResultColumnCUD.class.getSimpleName(), "mapperId", new String[]{mapper.getId()});
                List<ResultColumnCUD> all = findResultColumnCUDAll(resultColumnCUDs);
                Insert.insertManyPos(conn, all);
            }
            int count2 = Insert.insertManyPos(conn, resultColumns);
            conn.setAutoCommit(true);
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

    private void clearNullData(List<ResultColumn> resultColumns) {
        for (int i = 0; i < resultColumns.size(); i++) {
            ResultColumn rc = resultColumns.get(i);
            if (rc == null || rc.getProperty() == null || rc.getPoRelation() == null || rc.getMapperId() == null) {
                resultColumns.remove(i);
                i--;
            }
        }
    }

    @Override
    public int deleteMapper(Mapper mapper)   {
        Connection conn = SystemConnect.getConn();
        Delete.deleteDataByPri(conn, Mapper.class.getSimpleName(), mapper.getId());
        List<ResultColumn> resultColumns = mapper.getResultColumns();
        int count =  Delete.deleteDataByPri(conn, resultColumns);
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<Mapper> findMappersByDatabaseId(String databaseId) {
        String sql = "SELECT *  FROM Mapper WHERE databaseId = " + JDBCUtils.sqlStr(databaseId);
        List<Mapper> list = Select.findDataBySQL(SystemConnect.getConn(), sql, Mapper.class);
        return list;
    }

    @Override
    public int editResultColumnCUD(ResultColumnCUD resultColumnCUD) {
        return JDBCUtils.editPo(SystemConnect.getConn(), resultColumnCUD);
    }

    @Override
    public List<ResultColumnCUD> findResultColumnCUDByMapperId(String mapperId) {
        String sql = "SELECT *  FROM " + ResultColumnCUD.class.getSimpleName()
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
    public int handelData(CRUDEnum crud, Mapper mapper, Map<String, String> dataMap) {
        Map<String, List<JSONObject>> tableMap = new HashMap<>();

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
        }
    }


    private void handelData(Map<String, List<JSONObject>> tableMap, ResultColumnCUD cud, JSONObject dataMap) {
        String property = cud.getProperty();
        String tableName = cud.getTableName();
        String str = dataMap.getString(property);
        if(Tool.isEmpty(str)){
            return;
        }
        JSONArray jsonArray;
        if (str.startsWith("[")) {
            jsonArray = dataMap.getJSONArray(property);
        } else {
            JSONObject json = dataMap.getJSONObject(property);
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
