package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.exception.CommonException;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.tools.ReflectTool;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class JDBCUtils {


    public static void setPS(PreparedStatement ps, List<TableColumn> tableColumns, JSONObject jsonObject) {

          /*  Map<Object, TableColumn> columnMap = ReflectTool.getIDMap("columnName", tableColumns);
            for (String key : jsonObject.keySet()){
                TableColumn tableColumn = columnMap.get(key);

            }*/
            for (int i = 0; i < tableColumns.size(); i++) {
                setPS(ps, tableColumns.get(i), i+1, jsonObject);
            }
       /* try {
        } catch (CommonException e) {
            e.printStackTrace();
        }*/


    }
    public static void setPS(PreparedStatement ps, TableColumn tableColumn, int i, JSONObject jsonObject) {
        try {

            switch (tableColumn.getFiledEnum()) {
                case Integer:
                    Integer value = jsonObject.getInteger(tableColumn.getColumnName());
                    if(value == null){
                        ps.setNull(i, Types.INTEGER);
                    }else{
                        ps.setInt(i , value);
                    }
                    break;
                case Double:
                    Double dvalue = jsonObject.getDouble(tableColumn.getColumnName());
                    if(dvalue == null){
                        ps.setNull(i, Types.DOUBLE);
                    }else{
                        ps.setDouble(i , dvalue);
                    }

                    break;
                case String:
                    /*Double svalue = jsonObject.getDouble(tableColumn.getColumnName());
                    if(svalue == null){
                        ps.setNull(i, Types.VARCHAR);
                    }else{
                        ps.setDouble(i , svalue);
                    }*/
                    ps.setString(i , jsonObject.getString(tableColumn.getColumnName()));
                    break;
                default:
                    ps.setString(i , null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到sql value
     * @param tableColumn
     * @param jsonObject
     * @return
     */
    public static String getSQLValue(TableColumn tableColumn,JSONObject jsonObject){
        String sqlvalue = "";
        switch (tableColumn.getFiledEnum()) {
            case Integer:
                sqlvalue = ""+jsonObject.getInteger(tableColumn.getColumnName());
                break;
            case Double:
                sqlvalue = ""+jsonObject.getDouble(tableColumn.getColumnName());
                break;
            case String:
                sqlvalue = "'"+jsonObject.getString(tableColumn.getColumnName())+"'";
                break;
            default:
             break;
        }
        return  sqlvalue;
    }

    public static int executeBatch(PreparedStatement ps) throws SQLException {
        int countTotal = 0;
        int[] count = ps.executeBatch();
        for (int c : count) {
            countTotal += c;
        }
        return countTotal;
    }

    public static int executeBatch(Connection conn, CRUDEnum crudEnum, String sql, List<JSONObject> objects, List<TableColumn> tableColumns) {
        try {
            conn.setAutoCommit(false);//开启事务
            int countTotal = 0;

            final int batchSize = 1000;
            int count = 0;
            TableColumn columnByPRI = Connect.findColumnByPRI(tableColumns);
            int priIndex = tableColumns.size()+1;
            switch (crudEnum) {
                case INSERT:
                    break;
                case DELETE:
                    break;
                case UPDATE:
                    sql = sql + " WHERE  " + columnByPRI.getColumnName() + " = ?";
                    break;
                case SELECT:
                    break;
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            for (JSONObject jsonObject : objects) {
                JDBCUtils.setPS(ps, tableColumns, jsonObject);
                switch (crudEnum) {
                    case INSERT:
                        break;
                    case DELETE:
                        break;
                    case UPDATE:
                        JDBCUtils.setPS(ps,columnByPRI,priIndex,jsonObject);
                        break;
                    case SELECT:
                        break;
                }
                ps.addBatch();
                if (++count % batchSize == 0) {
                    countTotal += JDBCUtils.executeBatch(ps);
                }
            }
            countTotal += JDBCUtils.executeBatch(ps);
            ps.close();
            conn.setAutoCommit(true);
            return countTotal;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();//回滚事务
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }
}
