package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.jdbc.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

public class Connect {


    static Map<DatabaseConnect, Connection> poolMap = new HashMap<>();
    static Map<String, Connection> userMap = new HashMap<>();
    private static final String PRI = "PRI";

    public static Connection getSQLConnection(DatabaseConnect databaseConnect) throws SQLException, ClassNotFoundException {
        poolMap = poolMap != null ? poolMap : new HashMap<>();
        Connection connection = poolMap.get(databaseConnect);
        if (connection == null) {
            String databasename = databaseConnect.getDatabaseName();
            String username = databaseConnect.getUsername();
            String password = databaseConnect.getPassword();
            switch (databaseConnect.getDatabaseEnum()) {
                case mysql:
                    connection = getMySQLConnection(databaseConnect);
                    break;
                case postgress:
                    connection = getPostgressConnection(databaseConnect);
                    break;
                default:
                    throw new RuntimeException("暂不支持改数据库");
            }
            if (connection != null) {
                // userMap.put(UUID.randomUUID().toString(),connection);
                poolMap.put(databaseConnect, connection);
            }

        }
        return connection;
    }

    public static Connection getMySQLConnection(DatabaseConnect databaseConnect) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + databaseConnect.getIp() + "/" + databaseConnect.getDatabaseName() + "?serverTimezone=UTC", databaseConnect.getUsername(), databaseConnect.getPassword());
        return conn;
    }

    public static Connection getPostgressConnection(DatabaseConnect databaseConnect) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://" + databaseConnect.getIp() + "/" + databaseConnect.getDatabaseName(), databaseConnect.getUsername(), databaseConnect.getPassword());
        return conn;
    }

    /**
     * 获取当前数据库下的所有表名称
     *
     * @return
     * @throws Exception
     */
    public static List<Table> getAllTableName(Connection conn) {
        try {
            List<Table> tables = new ArrayList();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES ");
            while (rs.next()) {
                String tableName = rs.getString(1);
                List<TableColumn> tableColumns = getColumnCommentByTableName(conn, tableName);
                Table table = new Table(tableName, tableColumns);
                for (TableColumn tableColumn : tableColumns) {
                    if (PRI.equals(tableColumn.getKey())) {
                        table.setPrimaryIndex(tableColumns.indexOf(tableColumn));
                        break;
                    }
                }
                tables.add(table);
            }
            rs.close();
            stmt.close();
            return tables;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得某表中所有字段的注释
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static List<TableColumn> getColumnCommentByTableName(Connection conn, String tableName) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("show full columns from " + tableName);
            List<TableColumn> tableColumns = new ArrayList<>();
            while (rs.next()) {
                tableColumns.add(TableColumn.newInstance(rs));
            }
            rs.close();
            stmt.close();
            return tableColumns;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Column> getTableColumn(ResultSetMetaData rsmd) {
        List<Column> cols = new ArrayList<>();
        try {
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                cols.add(Column.newInstance(rsmd, i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cols;
    }


    public static TableColumn findColumnByPRI(List<TableColumn> tableColumns) {
        for (TableColumn tableColumn : tableColumns) {
            if (PRI.equals(tableColumn.getKey())) {
                return tableColumn;
            }
        }
        return null;
    }

}
