package cn.yb.datawaiter.jdbc.model;


import lombok.Data;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
public class Column {

    private String columnName;
    private String columnLabel;
    private String columnTypeName;
    private String columnClassName;
    /**
     * 字段注释
     */
    private String comment;

    private Column(String columnName, String columnLabel, String columnTypeName, String columnClassName) {
        this.columnName = columnName;
        this.columnLabel = columnLabel;
        this.columnTypeName = columnTypeName;
        this.columnClassName = columnClassName;
    }

    public static Column newInstance(ResultSetMetaData rsmd, int index){
        try {

            String columnName = rsmd.getColumnName(index).toLowerCase();
            String columnLabel = rsmd.getColumnLabel(index);
            String columnTypeName = rsmd.getColumnTypeName(index);
            String columnClassName = rsmd.getColumnClassName(index);
            return  new Column(columnName,columnLabel,columnTypeName,columnClassName);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  null;
    }
}
