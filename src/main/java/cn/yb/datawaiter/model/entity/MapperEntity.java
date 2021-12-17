package cn.yb.datawaiter.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class MapperEntity {
    private String id;
    private  String apiId;
    private String label;
    private String sql_;
    private MapperCreateEnum crud;
    private String databaseId;
    private  String tableName;
    private List<ResultColumnEntity> resultColumnEntities;
    private List<ResultColumnCUD> resultColumnCUDs;
}
