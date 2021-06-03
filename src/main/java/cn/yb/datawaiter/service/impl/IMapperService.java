package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.AutoCreateMapper;
import cn.yb.datawaiter.model.Mapper;
import cn.yb.datawaiter.model.ResultColumnCUD;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IMapperService {
    List<Mapper> createMapper(String databaseId, Table table, List<AutoCreateMapper> autos);

    int saveMappers(List<Mapper> mappers);

    List<Mapper> findMappersByDatabaseIdAndTableName(String databaseId, String tableName);
    List<JSONObject> findMappersByDatabaseIdAndTableNameAndCount(String databaseId, String tableName);

    Mapper findMapperById(String id);


    int editMapper(Mapper mapper);

    int deleteMapper(Mapper mapper) ;

    List<Mapper> findMappersByDatabaseId(String databaseId);

    int editResultColumnCUD(ResultColumnCUD resultColumnCUD);

    List<ResultColumnCUD> findResultColumnCUDByMapperId(String mapperId);

    /**
     * 处理数据
     * @param mapper
     * @param paramMap
     * @return
     */
    int handelData(CRUDEnum crud, Mapper mapper, Map<String, String> paramMap);
    int handelData(CRUDEnum crud, Mapper mapper,JSONObject jsonObject);

}
