package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.FiledEnum;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.List;

public class Insert {
    public static int insertManyDatas(Connection conn, String tablename, List<JSONObject> objects) {
        List<TableColumn> tableColumns = Connect.getColumnCommentByTableName(conn, tablename);
        StringBuilder sb = new StringBuilder("insert into " + tablename + "(");
        FiledEnum[] filelds = new FiledEnum[tableColumns.size()];
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn tableColumn = tableColumns.get(i);
            String columnname = tableColumn.getColumnName();
            sb.append(columnname + ",");
            filelds[i] = tableColumn.getFiledEnum();
        }
        sb.setCharAt(sb.length() - 1, ')');
        sb.append(" values (");
        for (TableColumn tableColumn : tableColumns) {
            sb.append("? ,");
        }
        sb.setCharAt(sb.length() - 1, ')');
        String sql = sb.toString();
        return JDBCUtils.executeBatch(conn, CRUDEnum.INSERT, sql, objects, tableColumns);
    }
}
