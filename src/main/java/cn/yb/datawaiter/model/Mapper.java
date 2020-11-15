package cn.yb.datawaiter.model;

import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import lombok.Data;

import java.util.List;

@Data
public class Mapper {
    private String id;
    private String label;
    private List<ResultColumn> resultColumns;
    private String sql_;
    private CRUDEnum crud;
    private String databaseId;
    private  String tableName;

}
