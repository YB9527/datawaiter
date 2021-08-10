package cn.yb.datawaiter.jdbc.model;

import lombok.Data;

import java.util.List;

@Data
public class Table {
    private  String databaseid;
    private  String name;
    private List<TableColumn> columns;
    private Integer primaryIndex;
    private String content;
    public Table(String databaseid,String name, String content, List<TableColumn> columns) {
        this.name = name;
        this.databaseid = databaseid;
        this.columns = columns;
        this.content = content;
    }


}
