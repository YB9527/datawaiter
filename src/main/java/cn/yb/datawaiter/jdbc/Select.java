package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.jdbc.model.Column;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Select {
    public static List<JSONObject> findDataBySQL(Connection conn, String sql) {
        Statement stat = null;
        List<JSONObject> jsonObjects = new ArrayList<>();
        try {
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            List<Column> cols = Connect.getTableColumn(rsmd);
            JSONObject jsonObject;
            while (rs.next()) {
                jsonObject = new JSONObject();
                jsonObjects.add(jsonObject);
                for (Column column : cols) {
                    jsonObject.put(column.getColumnLabel(), rs.getObject(column.getColumnName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObjects;
    }
}
