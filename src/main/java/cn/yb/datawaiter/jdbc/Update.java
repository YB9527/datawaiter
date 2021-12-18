package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.FiledEnum;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.tools.AnnotationTool;
import cn.yb.datawaiter.tools.JSONTool;
import cn.yb.datawaiter.tools.ReflectTool;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.Dic;
import cn.yb.sys.model.FJ;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.annotation.Annotation;
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
   /* public static int updateDataJSON(Connection conn, String tablename, List<JSONObject> objects) {
        tablename = tablename.toLowerCase();
        List<TableColumn> tableColumns = Connect.getColumnCommentByTableName(conn, tablename);
        Map<String, TableColumn> tableColumnMap = ReflectTool.getIDMap("getColumnName", tableColumns);
        List<String> sqls = new ArrayList<>();
        TableColumn columnByPRI = Connect.findColumnByPRI(tableColumns);
        String sqlt="";
        for (JSONObject jsonObject : objects) {

            sqlt = getUpdateSQL(tableColumnMap, tablename, columnByPRI, jsonObject);
            //sql =  sql.replace("","\"");

            sqls.add(sqlt);
        }
        try {
            conn.setAutoCommit(false);//开启事务
            Statement statement = conn.createStatement();
            for (String sql : sqls) {
                statement.execute(sql);
            }
            //conn.setAutoCommit(true);
            return objects.size();
        } catch (SQLException e) {
            System.out.println(sqlt);
            throw new GlobRuntimeException("SQL有问题："+e.getMessage());
        }

    }*/

    public static int updateDataJSON(Connection conn, String tablename, List<JSONObject> objects) {
        if(Tool.isEmpty(objects)){
            return  0;
        }
        tablename = tablename.toLowerCase();
        List<TableColumn> tableColumns = Connect.getColumnCommentByTableName(conn, tablename);
        TableColumn columnByPRI = Connect.findColumnByPRI(tableColumns);
        StringBuilder sb = new StringBuilder("update  `" + tablename+"` set " );
        FiledEnum[] filelds = new FiledEnum[tableColumns.size()];
        for (int i = 0; i < tableColumns.size(); i++) {

            TableColumn tableColumn = tableColumns.get(i);
            String columnname = tableColumn.getColumnName();
            columnname = JDBCUtils.getKeyStr(columnname);

            sb.append(columnname +"=?,");
            filelds[i] = tableColumn.getFiledEnum();
        }
        sb.setCharAt(sb.length() - 1, ' ');
        sb.append(" WHERE "+columnByPRI.getColumnName()+"=?");

        String sql = sb.toString();
        return JDBCUtils.executeBatch(conn, CRUDEnum.UPDATE, sql, objects, tableColumns);
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
            sb.append(JDBCUtils.getKeyStr(columnname)  + "=" + JDBCUtils.getSQLValue(tableColumn, jsonObject) + ",");
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
        String className = AnnotationTool.getTableName(aClass);
        return  updateManyDataPos(conn,className,list);
    }

    public static <T> int updateManyDataPos(Connection conn, String className, List<T> list) {
        if (Tool.isEmpty(list)) {
            return 0;
        }
        List<JSONObject> jsonObjects =  JSONTool.objectToJSON(list);
        return updateDataJSON(conn,className,jsonObjects);
    }

    public static int updateDataJSON(Connection conn, String simpleName, JSONObject json) {
        List<JSONObject> list = new ArrayList<>();
        list.add(json);
        return updateDataJSON( conn,simpleName, list);
    }

    public static int updateDataJSON(Connection connection, Map<String, List<JSONObject>> tableMap) {
        int count = 0;
        for (String tableName : tableMap.keySet()){
            count +=  updateDataJSON(connection,tableName,tableMap.get(tableName));
        }
        return  count;
    }

    public static<T> int updateManyDataPosInService(Connection conn, List<T> list) {
        JDBCUtils.startTransaction(conn);
        int count = updateManyDataPos(conn,list);
        JDBCUtils.conmitTransaction(conn);
        return  count;
    }
    public static<T> int updateManyDataPosInService(Connection conn, T t) {
        JDBCUtils.startTransaction(conn);
        int count = updateDataPo(conn,t);
        JDBCUtils.conmitTransaction(conn);
        return  count;
    }

    public static int updateJSONInService(Connection conn, String tablename, Object t){

        JDBCUtils.startTransaction(conn);
        JSONObject json ;
        if(t instanceof  JSONObject){
            json = (JSONObject) t;
        }else{
            json =  (JSONObject) JSON.toJSON(t);
        }
        int count = updateDataJSON(conn,tablename,json);
        JDBCUtils.conmitTransaction(conn);
        return  count;
    }
}
