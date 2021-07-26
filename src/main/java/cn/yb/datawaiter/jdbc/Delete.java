package cn.yb.datawaiter.jdbc;

import cn.yb.auth.model.User;
import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.model.Column;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.model.ResultColumn;
import cn.yb.datawaiter.tools.JSONTool;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.FJ;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Delete {
    public static int deleteByColoumnAndValues(Connection conn, String tablename, String column, Object[] values) {
        try {
            tablename =tablename.trim().toLowerCase();
            if(values == null){
                return  0;
            }
            conn.setAutoCommit(false);//开启事务
            int countTotal = 0;
            int max = 2000;
            for (int i = 0; i < values.length / max + 1; i++) {
                Statement stmt = conn.createStatement();
                StringBuilder stringBuilder = new StringBuilder("DELETE FROM " + tablename);
                stringBuilder.append(" WHERE " + column + " in  (");
                int formax = (i+1) * max > values.length?values.length: (i+1) * max;
                for (int j = i * max; j < formax; j++) {
                    Object value = values[j];
                    //执行添加 到 dept 表的数据的 sql 语句
                        String typename = value.getClass().getSimpleName();
                        switch (typename) {
                            case "String":
                                stringBuilder.append("'" + value + "',");
                                break;
                            case "Integer":
                                stringBuilder.append(value + ",");
                                break;
                            default:
                                throw new RuntimeException("不能识别的类型");
                        }
                }
                stringBuilder.setCharAt(stringBuilder.length() - 1, ')');
                String sql = stringBuilder.toString();

                //将 sql 发送到 Oracle 执行
                countTotal += stmt.executeUpdate(sql);
                stmt.close();
            }
            //conn.commit();
            return countTotal;
        } catch (SQLException e) {
            throw new GlobRuntimeException(e.getMessage());
        }
    }

    /**
     * 根据对象的组件，删除数据
     *
     * @param conn
     * @param tablename
     * @param datas
     */
    public static int deleteDataByPri(Connection conn, String tablename, List<JSONObject> datas) {
        if (Tool.isEmpty(tablename) || Tool.isEmpty(datas)) {
            return 0;
        }
        List<TableColumn> columns = Connect.getColumnCommentByTableName(conn, tablename);
        TableColumn columnByPRI = Connect.findColumnByPRI(columns);
        Object[] values = new Object[datas.size()];
        String columnName = columnByPRI.getColumnName();
        JSONObject jsonObject;
        int max = 5000;


        for (int i = 0; i < datas.size(); i++) {
            jsonObject = datas.get(i);
            Object value = null;
            if (jsonObject != null) {
                value = jsonObject.get(columnName);
            }
            values[i] = value;
        }
        return deleteByColoumnAndValues(conn, tablename, columnName, values);
    }


    public static int deleteDataByPri(Connection conn, String tablename, String id) {
        List<TableColumn> columns = Connect.getColumnCommentByTableName(conn, tablename);
        TableColumn columnByPRI = Connect.findColumnByPRI(columns);
        Object[] values = new Object[]{id};
        return deleteByColoumnAndValues(conn, tablename, columnByPRI.getColumnName(), values);
    }

    public static<T> int deleteDataByPri(Connection conn,  List<T> deleteList) {
        if(Tool.isEmpty(deleteList)){
            return  0;
        }
        return  deleteDataByPri(conn,deleteList.get(0).getClass().getSimpleName(), JSONTool.objectToJSON(deleteList));
    }

    public static int deleteDataByPri(Connection connection, Map<String, List<JSONObject>> tableMap) {
        int count = 0;
        for (String tableName : tableMap.keySet()){
            count +=  deleteDataByPri(connection,tableName,tableMap.get(tableName));
            //throw  new GlobRuntimeException("123");
        }
        return  count;
    }

    public static<T> int deleteDataByPri(Connection conn, T t) {
        List<T> list = new ArrayList<>();
        list.add(t);
        return deleteDataByPri(conn,list);
    }

    public static<T> int deleteDataByPriInServerice(Connection conn, List<T> list) {
        JDBCUtils.startTransaction(conn);
        int count = deleteDataByPri(conn,list);
        JDBCUtils.conmitTransaction(conn);
        return  count;
    }
}
