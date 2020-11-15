package cn.yb.datawaiter.jdbc.model;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class TableColumn {
    private String collation;
    private String comment;
    private String defaultValue;
    private String extra;
    private String columnName;
    private String key;
    private Boolean isNull;
    private String privileges;
    private FiledEnum filedEnum;

    public static TableColumn newInstance(ResultSet rs) {
        TableColumn tableColumn = new TableColumn();
        try {
            //tableColumn.collation = rs.getString("Collation");
            //tableColumn.comment = rs.getString("Comment");
            //tableColumn.defaultValue = rs.getString("Default");
           // tableColumn.extra = rs.getString("Extra");
            tableColumn.columnName = rs.getString("Field");
            tableColumn.key = rs.getString("Key");
            //tableColumn.isNull = rs.getString("Null") == "YES" ? true : false;
            //tableColumn.privileges = rs.getString("Privileges");
            String columntype = rs.getString("Type").toLowerCase();
            FiledEnum filedEnum;
            int i = columntype.indexOf("(");
            if (i != -1) {
                columntype = columntype.substring(0, i );
            }
            switch (columntype) {
                case "integer":
                case "int":
                    filedEnum = FiledEnum.Integer;
                    break;
                case "double":
                case "float":
                    filedEnum = FiledEnum.Double;
                    break;
                case "text":
                case "varchar":
                    filedEnum = FiledEnum.String;
                    break;
                case "blob":
                    filedEnum = FiledEnum.Blob;
                    break;
                case "enum":
                    filedEnum = FiledEnum.ENUM;
                    break;
                case "datetime":
                    filedEnum = FiledEnum.DateTime;
                    break;

                default:
                    throw new RuntimeException("无法识别的类型："+columntype);
            }
            tableColumn.filedEnum = filedEnum;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableColumn;
    }
}
