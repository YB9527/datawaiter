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
    private int paramType;

    public static TableColumn newInstance(ResultSet rs) {
        TableColumn tableColumn = new TableColumn();
        try {
            //tableColumn.collation = rs.getString("Collation");
            tableColumn.comment = rs.getString("Comment");
            //tableColumn.defaultValue = rs.getString("Default");
           // tableColumn.extra = rs.getString("Extra");
            tableColumn.columnName = rs.getString("Field");
            tableColumn.key = rs.getString("Key");
            tableColumn.isNull = rs.getString("Null").equalsIgnoreCase("YES")  ;
            //tableColumn.privileges = rs.getString("Privileges");
            String columntype = rs.getString("Type").toLowerCase();

            FiledEnum filedEnum;
            int i = columntype.indexOf("(");
            if (i != -1) {
                columntype = columntype.substring(0, i );
            }
            switch (columntype) {
                case "bit":
                case "integer":
                case "int":
                case "smallint":
                case "bigint":
                    filedEnum = FiledEnum.Integer;
                    break;
                case "double":
                case "float4":
                case "float8":
                case "float":
                case "real":
                case "numeric":
                    filedEnum = FiledEnum.Double;
                    break;
                case "text":
                case "character varying":
                case "varchar":
                case "character":
                case "longtext":
                    filedEnum = FiledEnum.String;
                    break;
                case "blob":
                case "bool":
                case "boolean":
                    filedEnum = FiledEnum.Boolean;
                    break;
                case "enum":
                    filedEnum = FiledEnum.ENUM;
                    break;
                case "datetime":
                case "date":
                    filedEnum = FiledEnum.DateTime;
                    break;
                case "time":
                case "timestamp":
                    filedEnum = FiledEnum.TimeStamp;
                    break;
                case "geometry":
                    filedEnum = FiledEnum.Blob;
                    break;

                default:
                    filedEnum = FiledEnum.String;
                    System.out.println("无法识别的类型："+columntype);
                    break;
                    //throw new RuntimeException("无法识别的类型："+columntype);
            }
            tableColumn.filedEnum = filedEnum;
            tableColumn.paramType = rs.getType();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableColumn;
    }
}
