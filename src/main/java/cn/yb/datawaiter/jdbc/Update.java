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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Update {

    /**
     * 修改数据库
     * @param conn
     * @param tablename
     * @param objects
     * @return
     */
    public static int updateManyDatas(Connection conn, String tablename, List<JSONObject> objects) {
        List<TableColumn> tableColumns = Connect.getColumnCommentByTableName(conn, tablename);
        Map<String, TableColumn> tableColumnMap = ReflectTool.getIDMap("getColumnName", tableColumns);
        List<String> sqls = new ArrayList<>();
        TableColumn columnByPRI = Connect.findColumnByPRI(tableColumns);
        for (JSONObject jsonObject : objects) {
            String sql = getUpdateSQL(tableColumnMap, tablename, columnByPRI, jsonObject);
            sqls.add(sql);
        }
        try {
            conn.setAutoCommit(false);//开启事务
            Statement statement = conn.createStatement();
            for (String sql : sqls) {
                statement.execute(sql);
            }
            conn.setAutoCommit(true);
            return statement.getUpdateCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 得到修改的sql 语句
     * @param tableColumnMap
     * @param tablename  表格名字
     * @param columnByPRI 主键
     * @param jsonObject
     * @return
     */
    private static String getUpdateSQL(Map<String, TableColumn> tableColumnMap, String tablename, TableColumn columnByPRI, JSONObject jsonObject) {
        StringBuilder sb = new StringBuilder("update " + tablename + " set ");
        for (String columnname : tableColumnMap.keySet()) {
            TableColumn tableColumn = tableColumnMap.get(columnname);
            sb.append(columnname + "=" + JDBCUtils.getSQLValue(tableColumn, jsonObject) + ",");
        }
        sb.setCharAt(sb.length() - 1, ' ');
        sb.append(" WHERE " + columnByPRI.getColumnName() + "=" + JDBCUtils.getSQLValue(columnByPRI, jsonObject));
        String sql = sb.toString();
        return sql;
    }

    public static <T> int updateDataPo(Connection conn, T t) {
        List<T> list = new ArrayList<>();
        list.add(t);
        return updateManyDataPos(conn,list);
    }

    public static <T> int updateManyDataPos(Connection conn, List<T> list) {
        if (Tool.isEmpty(list)) {
            return 0;
        }
        Class aClass = list.get(0).getClass();
        String className = aClass.getSimpleName();
        return  updateManyDataPos(conn,className,list);
    }

    public static <T> int updateManyDataPos(Connection conn, String className, List<T> list) {
        if (Tool.isEmpty(list)) {
            return 0;
        }
        List<JSONObject> jsonObjects =  JSONTool.objectToJSON(list);
        return updateManyDatas(conn,className,jsonObjects);
    }

    public static void updateDataJSON(Connection conn, String simpleName, JSONObject json) {
        List<JSONObject> list = new ArrayList<>();
        list.add(json);
        updateManyDatas( conn,simpleName, list);
    }
}
