package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.FiledEnum;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.model.ResultColumn;
import cn.yb.datawaiter.model.ResultColumnCUD;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class JDBCUtils {


    public static void setPS(PreparedStatement ps, List<TableColumn> tableColumns, JSONObject jsonObject) {

          /*  Map<Object, TableColumn> columnMap = ReflectTool.getIDMap("columnName", tableColumns);
            for (String key : jsonObject.keySet()){
                TableColumn tableColumn = columnMap.get(key);

            }*/
        for (int i = 0; i < tableColumns.size(); i++) {
            setPS(ps, tableColumns.get(i), i + 1, jsonObject);
        }
       /* try {
        } catch (CommonException e) {
            e.printStackTrace();
        }*/


    }

    public static void setPS(PreparedStatement ps, TableColumn tableColumn, int i, JSONObject jsonObject) {
        try {
            String columnName = tableColumn.getColumnName();
            switch (tableColumn.getFiledEnum()) {
                case Integer:
                    Integer value = jsonObject.getInteger(columnName);
                    if (value == null) {
                        ps.setNull(i, Types.INTEGER);
                    } else {
                        ps.setInt(i, value);
                    }
                    break;
                case Double:
                    Double dvalue = jsonObject.getDouble(columnName);
                    if (dvalue == null) {
                        ps.setNull(i, Types.DOUBLE);
                    } else {
                        ps.setDouble(i, dvalue);
                    }

                    break;
                case Boolean:
                    Boolean bl = jsonObject.getBoolean(columnName);

                    if (bl != null) {
                        ps.setBoolean(i, bl);
                    } else {
                        ps.setNull(i, Types.BOOLEAN);
                    }
                    break;
                case Blob:
                    ps.setNull(i, Types.BINARY);
                    break;
                case ENUM:
                case String:
                    /*Double svalue = jsonObject.getDouble(tableColumn.getColumnName());
                    if(svalue == null){
                        ps.setNull(i, Types.VARCHAR);
                    }else{
                        ps.setDouble(i , svalue);
                    }*/
                    ps.setString(i, jsonObject.getString(columnName));
                    break;
                case TimeStamp:
                case DateTime:
                    Date date = jsonObject.getDate(columnName);
                    if (date != null ) {
                        long lg = date.getTime(); //日期转时间戳
                        ps.setTimestamp(i, new Timestamp(lg / 1000 * 1000));

                    } else {
                        if(tableColumn.getFiledEnum() == FiledEnum.TimeStamp){
                            int a1 =Types.TIME_WITH_TIMEZONE;
                            int a2 =tableColumn.getParamType();
                            ps.setNull(i,Types.TIME_WITH_TIMEZONE);
                        }else{
                            ps.setNull(i,Types.DATE);
                        }
                    }
                    break;
                default:
                    throw new GlobRuntimeException("类型无法识别：" + tableColumn.getFiledEnum());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到sql value
     *
     * @param tableColumn
     * @param jsonObject
     * @return
     */
    public static String getSQLValue(TableColumn tableColumn, JSONObject jsonObject) {
        String sqlvalue = "";
        switch (tableColumn.getFiledEnum()) {
            case Integer:
                sqlvalue = "" + jsonObject.getInteger(tableColumn.getColumnName());
                break;
            case Double:
                sqlvalue = "" + jsonObject.getDouble(tableColumn.getColumnName());
                break;
            case DateTime:
            case String:
                String tem = jsonObject.getString(tableColumn.getColumnName());
                if(tem == null  || tem.equals("null")){
                    sqlvalue = null;
                }else{
                    sqlvalue = "\"" + jsonObject.getString(tableColumn.getColumnName()) + "\"";
                }
                break;
            case ENUM:
                sqlvalue = "\"" + jsonObject.getString(tableColumn.getColumnName()) + "\"";
                break;
            default:
                break;
        }
        return sqlvalue;
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
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);//开启事务
            int countTotal = 0;

            final int batchSize = 1000;
            int count = 0;
            TableColumn columnByPRI = Connect.findColumnByPRI(tableColumns);
            int priIndex = tableColumns.size() + 1;
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
            ps = conn.prepareStatement(sql);
            for (JSONObject jsonObject : objects) {
                JDBCUtils.setPS(ps, tableColumns, jsonObject);
                switch (crudEnum) {
                    case INSERT:
                        break;
                    case DELETE:
                        break;
                    case UPDATE:
                        JDBCUtils.setPS(ps, columnByPRI, priIndex, jsonObject);
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
            return countTotal;
        } catch (SQLException e) {
            try {
                conn.rollback();//回滚事务
                if (ps != null) {
                    throw new GlobRuntimeException(e.getMessage() + ",SQL:" + ps.toString());
                } else {
                    throw new GlobRuntimeException(e.getMessage());
                }

            } catch (SQLException ex) {
                throw new GlobRuntimeException(ex.getMessage());
            }
        }
    }

    public static String sqlStr(String str) {
        return "\"" + str + "\"";
    }

    public static String getColumnAppend(List<TableColumn> columns) {
        StringBuilder sb = new StringBuilder(" ");
        for (TableColumn column : columns) {
            sb.append(column.getColumnName() + " ,");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static List<ResultColumn> getResultColumn(String mapperId, List<TableColumn> columns) {
        List<ResultColumn> rcs = new ArrayList<>();
        for (TableColumn tc : columns) {
            ResultColumn rc = new ResultColumn(mapperId, tc);
            rc.setMapperId(mapperId);
            rcs.add(rc);
        }
        return rcs;
    }

    public static int editPo(Connection conn, Object obj) {
        if (obj == null) {
            return 0;
        }
        String tableName = obj.getClass().getSimpleName();
        JSONObject jsonObject = (JSONObject) JSON.toJSON(obj);

        return editJSON(conn, tableName, jsonObject);

    }

    private static int editJSON(Connection conn, String tableName, JSONObject jsonObject) {
        TableColumn columnByPRI = Connect.findColumnByPRI(conn, tableName);
        JSONObject jsonInDB = Select.findDataById(conn, tableName, jsonObject.getString(columnByPRI.getColumnName()));
        int count;
        if (jsonInDB == null) {
            count = Insert.insertJSON(conn, tableName, jsonObject);
        }else{
            count = Update.updateDataJSON (conn, tableName, jsonObject);
        }
        return count;
    }

    public static String getKeyStr(String columnname) {

        switch (columnname){
            case "key":
            case "column":
            case "describe":
                columnname = "`"+columnname+ "`";
                break;
        }
        return columnname;
    }
}
