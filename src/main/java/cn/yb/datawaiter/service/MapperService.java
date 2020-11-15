package cn.yb.datawaiter.service;

import cn.yb.datawaiter.dao.impl.IMapperDao;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.model.*;
import cn.yb.datawaiter.service.impl.IMapperService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        // for (AutoCreateMapperResult aut)
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
                sql = "SELECT " + JDBCUtils.getColumnAppend(table.getColumns()) + " FROM " + tableName + " WHERE " + pri.getColumnName() + " = [" + pri.getColumnName() + "]";
                mapper.setCrud(CRUDEnum.SELECT);
                resultColumns.add(new ResultColumn(mapperId, pri));
                mapper.setLabel("查找根据ID");
                break;
            case SelectAll:
                sql = "SELECT " + JDBCUtils.getColumnAppend(table.getColumns()) + " FROM " + tableName;
                mapper.setCrud(CRUDEnum.SELECT);
                mapper.setLabel("查找所有");
                break;
            case SelectCount:
                sql = "SELECT COUNT(*) as count FROM " + tableName;
                mapper.setCrud(CRUDEnum.SELECT);
                mapper.setLabel("查找总条目数");
                break;
            case SelectPagination:
                mapper.setCrud(CRUDEnum.SELECT);
                mapper.setLabel("查找分页");
                break;
            case DeleteByPo:
                mapper.setCrud(CRUDEnum.DELETE);
                mapper.setLabel("删除根据对象");
                break;
            case SavePo:
                mapper.setCrud(CRUDEnum.INSERT);
                mapper.setLabel("添加对象");
                break;
            case UpdatePo:
                mapper.setCrud(CRUDEnum.UPDATE);
                mapper.setLabel("修改对象");
                break;
        }
        mapper.setResultColumns(resultColumns);
        mapper.setSql_(sql);
        return mapper;
    }

    @Override
    public int saveMappers(List<Mapper> mappers) {
        int count = Insert.insertManyPos(SystemConnect.getConn(), mappers);
        List<ResultColumn> rcs = new ArrayList<>();
        for (Mapper mapper : mappers) {
            if (!Tool.isEmpty(mapper.getResultColumns())) {
                rcs.addAll(mapper.getResultColumns());
            }
        }
        Insert.insertManyPos(SystemConnect.getConn(), rcs);
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
        String tableName = Mapper.class.getSimpleName();
        String sql = mapper.getSql_().replace("\"", "'");
        mapper.setSql_(sql);
        JSONObject dbPo = Select.findDataById(conn, tableName, mapper.getId());
        List<ResultColumn> resultColumns = mapper.getResultColumns();
        String mapperId = mapper.getId();
        for (ResultColumn rc : resultColumns){
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
        int count2 = Insert.insertManyPos(conn, resultColumns);
        return count;
    }

    private void clearNullData(List<ResultColumn> resultColumns) {
        for (int i = 0; i < resultColumns.size(); i++) {
            ResultColumn rc = resultColumns.get(i);
            if(rc == null || rc.getProperty() == null || rc.getPoRelation() == null || rc.getMapperId() == null){
                resultColumns.remove(i);
                i--;
            }
        }
    }

    @Override
    public int deleteMapper(Mapper mapper) {
        Delete.deleteDataByPri(SystemConnect.getConn(),Mapper.class.getSimpleName(),mapper.getId());
        List<ResultColumn> resultColumns = mapper.getResultColumns();
        Delete.deleteDataByPri(SystemConnect.getConn(),resultColumns);
        return 0;
    }

    @Override
    public List<Mapper> findMappersByDatabaseId(String databaseId) {
        String sql = "SELECT *  FROM Mapper WHERE databaseId = " + JDBCUtils.sqlStr(databaseId) ;
        List<Mapper> list = Select.findDataBySQL(SystemConnect.getConn(), sql, Mapper.class);
        return list;
    }
}
