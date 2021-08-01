package cn.yb.datawaiter.jdbc.model;

import lombok.Data;

import java.util.List;

@Data
public class Table {
    private  String name;
    private List<TableColumn> columns;
    private Integer primaryIndex;
    private String label;
    public Table(String name, String label, List<TableColumn> columns) {
        this.name = name;
        this.columns = columns;
        this.label = label;
    }


}
