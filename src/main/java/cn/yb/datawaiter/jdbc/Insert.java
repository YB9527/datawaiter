package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.FiledEnum;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.tools.JSONTool;
import cn.yb.datawaiter.tools.ReflectTool;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.List;

public class Insert {
    /***
     * 插入json 数据
     * @param conn
     * @param tablename
     * @param objects
     * @return
     */
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

    /**
     * 插入 java对象
     * @param conn
     * @param list
     * @param <T>
     * @return
     */
    public static <T> int insertManyPos(Connection conn, List<T> list) {
        if (Tool.isEmpty(list)) {
            return 0;
        }
        Class aClass = list.get(0).getClass();
        String className = aClass.getSimpleName();
        return  insertManyPos(conn,className,list);
    }

    /**
     * 插入 java 对象
     * @param conn
     * @param className
     * @param list
     * @param <T>
     * @return
     */
    public static <T> int insertManyPos(Connection conn, String className, List<T> list) {
        if (Tool.isEmpty(list)) {
            return 0;
        }
        List<JSONObject> jsonObjects =  JSONTool.objectToJSON(list);
        return insertManyDatas(conn,className,jsonObjects);

    }
}
