package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.entity.AutoCreateMapper;
import cn.yb.datawaiter.model.entity.MapperEntity;
import cn.yb.datawaiter.model.entity.ResultColumnCUD;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface IMapperService {
    List<MapperEntity> createMapper(String databaseId, Table table, List<AutoCreateMapper> autos);

    int saveMappers(List<MapperEntity> mapperEntities);

    List<MapperEntity> findMappersByDatabaseIdAndTableName(String databaseId, String tableName);
    List<JSONObject> findMappersByDatabaseIdAndTableNameAndCount(String databaseId, String tableName);

    MapperEntity findMapperById(String id);


    int editMapper(MapperEntity mapperEntity);

    int deleteMapper(MapperEntity mapperEntity) ;

    List<MapperEntity> findMappersByDatabaseId(String databaseId);

    int editResultColumnCUD(ResultColumnCUD resultColumnCUD);

    List<ResultColumnCUD> findResultColumnCUDByMapperId(String mapperId);

    /**
     * 处理数据
     * @param mapperEntity
     * @param paramMap
     * @return
     */
    int handelData(CRUDEnum crud, MapperEntity mapperEntity, Map<String, String> paramMap);
    int handelData(CRUDEnum crud, MapperEntity mapperEntity, JSONObject jsonObject);

}
