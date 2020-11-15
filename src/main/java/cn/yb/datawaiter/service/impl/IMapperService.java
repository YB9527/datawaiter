package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.AutoCreateMapper;
import cn.yb.datawaiter.model.Mapper;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.List;

public interface IMapperService {
    List<Mapper> createMapper(String databaseId, Table table, List<AutoCreateMapper> autos);

    int saveMappers(List<Mapper> mappers);

    List<Mapper> findMappersByDatabaseIdAndTableName(String databaseId, String tableName);

    Mapper findMapperById(String id);


    int editMapper(Mapper mapper);

    int deleteMapper(Mapper mapper);

    List<Mapper> findMappersByDatabaseId(String databaseId);
}
