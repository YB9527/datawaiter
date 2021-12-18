package cn.yb.datawaiter.model.entity;

import lombok.Data;

import javax.persistence.Table;
import java.util.List;

@Data
@Table(name = "mapper")
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
