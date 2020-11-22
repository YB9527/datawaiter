package cn.yb.datawaiter.model;

import lombok.Data;

import java.util.List;

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
}
