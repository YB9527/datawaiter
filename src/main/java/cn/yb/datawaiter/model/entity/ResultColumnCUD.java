package cn.yb.datawaiter.model.entity;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResultColumnCUD {
    private String id;
    private String property;
    private String tableName;
    private String mapperId;
    private String parentId;
    private List<ResultColumnCUD> resultColumnCUDs;

    public ResultColumnCUD(String id, String property, String tableName, String mapperId, String parentId) {
        this.id = id;
        this.property = property;
        this.tableName = tableName;
        this.mapperId = mapperId;
        this.parentId = parentId;
    }

    public ResultColumnCUD() {

    }

    public ResultColumnCUD(MapperEntity mapperEntity) {
        this.id = UUID.randomUUID().toString();
        this.property = mapperEntity.getTableName();
        this.tableName =  mapperEntity.getTableName();
        this.mapperId = mapperEntity.getId();
    }
}
