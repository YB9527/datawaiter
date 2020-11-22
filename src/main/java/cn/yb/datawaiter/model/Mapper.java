package cn.yb.datawaiter.model;

import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import lombok.Data;

import java.util.List;

@Data
public class Mapper {
    private String id;
    private  String apiId;
    private String label;
    private String sql_;
    private MapperCreateEnum crud;
    private String databaseId;
    private  String tableName;
    private  String mapperId;
    private List<ResultColumn> resultColumns;
    private List<ResultColumnCUD> resultColumnCUDs;
}
