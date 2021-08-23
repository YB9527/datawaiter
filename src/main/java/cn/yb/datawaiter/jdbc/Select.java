package cn.yb.datawaiter.jdbc;

import cn.yb.auth.model.User;
import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.model.Column;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.model.Mapper;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.Project;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Select {
    public static List<JSONObject> findDataBySQL(Connection conn, String sql) {
       // System.out.println(sql) ;
        if(sql.contains("DatabaseConnect")){
            int a =1;
            
        }
        Statement stat = null;
        List<JSONObject> jsonObjects = new ArrayList<>();
        try {
            stat = conn.createStatement();

            ResultSet rs = stat.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            List<Column> cols = Connect.getTableColumn(rsmd);
            JSONObject jsonObject;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            while (rs.next()) {
                jsonObject = new JSONObject();
                jsonObjects.add(jsonObject);

                for (Column column : cols) {
                    Object value = rs.getObject(column.getColumnLabel());
                    switch (column.getColumnTypeName()){
                        case "DATETIME":
                            if(value != null){
                                value = df.format(value);
                            }
                            break;
                    }
                    jsonObject.put(column.getColumnLabel(), value);
                }
            }
        } catch (SQLException e) {
            System.out.println(sql) ;
            throw new GlobRuntimeException(e.getMessage());
        }
        return jsonObjects;
    }
    /**
     * 查找表中所有对象
     *
     * @param sysConn
     * @param clazz
     * @return
     */
    public static List<JSONObject> findDataAllByPoName(Connection sysConn, Class clazz) {

        String className = clazz.getSimpleName().toLowerCase();
        return findDataBySQL(sysConn, "select * from " + className);
    }

    public static List<JSONObject> findDataAllByTableName(Connection sysConn, String tablename) {
        return findDataBySQL(sysConn, "select * from " + tablename);
    }


    public static JSONObject findDataById(Connection conn, Class clazz, String id) {
        String className = clazz.getSimpleName().toLowerCase();;
        return findDataById(conn, className, id);
    }

    public static <T> T findDataById2Po(Connection conn, Class<T> clazz, String id) {
        String className = clazz.getSimpleName().toLowerCase();;
        JSONObject json = findDataById(conn, className, id);
        if (json != null) {
            return json.toJavaObject(clazz);
        } else {
            return null;
        }

    }

    public static JSONObject findDataById(Connection conn, String tableName, String id) {
        tableName = tableName.toLowerCase();
        List<JSONObject> jsons = findDataBySQL(conn, "select * from " + tableName + " where id =" + JDBCUtils.sqlStr(id));
        if (Tool.isEmpty(jsons)) {
            return null;
        }
        return jsons.get(0);
    }

    public static <T> List<T> findDataBySQL(Connection sysConn, String sql, Class<T> clazz) {
        List<JSONObject> jsons = findDataBySQL(sysConn, sql);
        List<T> list = new ArrayList<>();
        if (!Tool.isEmpty(jsons)) {
            for (JSONObject json : jsons) {
                list.add(json.toJavaObject(clazz));
            }
        }
        return list;
    }

    public static <T> T findPoById(Connection conn, Class<T> clazz, String id) {
        JSON json = findDataById(conn, clazz.getSimpleName().toLowerCase(), id);
        if (json != null) {
            return json.toJavaObject(clazz);
        }
        return null;
    }

    public static <T> T findOneDataBySQL(Connection conn, String sql, Class<T> mapperClass) {
        List<T> list = findDataBySQL(conn, sql, mapperClass);
        return list.size() > 0 ? list.get(0) : null;
    }


    public static<T> String getSQL(Class<T> tClass, Map<String, Object> map) {
        String tableName = tClass.getSimpleName().toLowerCase();
        return  getSQL(tableName,  map);
    }
    public static<T> String getSQL(String tablename, Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM `" +tablename+"`");
        if(map != null && map.keySet().size() > 0){
            sb.append(" WHERE " );
            for (String key: map.keySet() ) {
                Object value = map.get(key);
                if(value instanceof  String){
                    sb.append(" "+key + "'"+value+"'");
                }else if(value != null){
                    sb.append(" "+key + value);
                }else{
                    sb.append(" "+key );
                }
            }
        }
        return  sb.toString();
    }

    public static <T> List<T> findDataAllToPo(Connection conn, Class<T> tClass) {
        String str = getSQL(tClass,null);
        return  findDataBySQL(conn,str,tClass);
    }

    public static <T> List<T> findDataByMap(Connection conn,Class<T> tClass, Map<String, Object> map) {
        String str = getSQL(tClass,map);
        return  findDataBySQL(conn,str,tClass);
    }

    public static  List<JSONObject> findDataByMap(Connection conn,String tableName, Map<String, Object> map) {
        String str = getSQL(tableName,map);
        return  findDataBySQL(conn,str);
    }
}
