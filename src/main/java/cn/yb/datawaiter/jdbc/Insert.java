package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.FiledEnum;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.tools.AnnotationTool;
import cn.yb.datawaiter.tools.JSONTool;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.FJ;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Insert {
    /***
     * 插入json 数据
     * @param conn
     * @param tablename
     * @param objects
     * @return
     */
    public static int insertManyJSONs(Connection conn, String tablename, List<JSONObject> objects) {
        if(Tool.isEmpty(objects)){
            return  0;
        }
         tablename =tablename.toLowerCase();
        List<TableColumn> tableColumns = Connect.getColumnCommentByTableName(conn, tablename);
        StringBuilder sb = new StringBuilder("insert into `" + tablename + "`(");
        FiledEnum[] filelds = new FiledEnum[tableColumns.size()];
        for (int i = 0; i < tableColumns.size(); i++) {

            TableColumn tableColumn = tableColumns.get(i);
            String columnname = tableColumn.getColumnName();
            columnname = JDBCUtils.getKeyStr(columnname);

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
        String className = AnnotationTool.getTableName(aClass);
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
        return insertManyJSONs(conn,className,jsonObjects);

    }

    /**
     * 实际上调用的 也是 insertManyPos
     * @param sysConn
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> int insertPo(Connection sysConn, T obj) {
        List<T> list = new ArrayList<>();
        list.add(obj);
        int count =  insertManyPos(sysConn,list);
        try {
            sysConn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  count;
    }
    public static int insertJSON(Connection conn, String tablename, JSONObject json){
        List<JSONObject> jsons = new ArrayList<>();
        jsons.add(json);
        return  insertManyJSONs(conn,tablename,jsons);
    }

    public static int insertManyJSONs(Connection connection, Map<String, List<JSONObject>> tableMap) {
        int count = 0;
        for (String tableName : tableMap.keySet()){
            count +=  insertManyJSONs(connection,tableName,tableMap.get(tableName));
            //throw  new GlobRuntimeException("aa");
        }
        return  count;
    }

    public static<T> int insertManyPosInService(Connection conn, List<T> list) {
        try {
            conn.setAutoCommit(false);//开启事务
            int count = Insert.insertManyPos(conn,list);
            conn.commit();
            return  count;
        } catch (Exception e) {
            try {
                conn.rollback();//回滚事务
                throw new GlobRuntimeException(e.getMessage());
            } catch (SQLException ex) {
                throw new GlobRuntimeException(ex.getMessage());
            }
        }
    }
    public static<T> int insertManyPosInService(Connection conn, T t) {

           JDBCUtils.startTransaction(conn);
            int count = Insert.insertPo(conn,t);
            JDBCUtils.conmitTransaction(conn);
            return  count;

    }

    public static int insertJSONInService(Connection conn, String tablename, Object t){

        JDBCUtils.startTransaction(conn);
        JSONObject json ;
        if(t instanceof  JSONObject){
            json = (JSONObject) t;
        }else{
            json =  (JSONObject) JSON.toJSON(t);
        }
        int count = Insert.insertJSON(conn,tablename,json);
        JDBCUtils.conmitTransaction(conn);
        return  count;
    }
}
