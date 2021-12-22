package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.model.*;
import com.mysql.cj.jdbc.ConnectionImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

public class Connect {

    private static Map<String, Connection> connMap = new HashMap<>();
    static Map<String, Connection> userMap = new HashMap<>();
    private static final String PRI = "PRI";

    static {
        timer1();
    }
    public static Connection getSQLConnection(DatabaseConnect databaseConnect) throws SQLException, ClassNotFoundException {

        Connection connection = null;
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
        return connection;
    }
    public static void timer1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                for (Connection connection:  userMap.values()) {
                    getAllTableName(connection);
                }
                timer1();
            }
        }, 1000*60*60*7);// 设定指定的时间time,此处为2000毫秒
    }

    public static Connection getMySQLConnection(DatabaseConnect databaseConnect) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + databaseConnect.getIp() + "/" + databaseConnect.getDatabaseName() + "?serverTimezone=GMT%2B8&autoReconnect=true", databaseConnect.getUsername(), databaseConnect.getPassword());
        return conn;
    }

    public static Connection getPostgressConnection(DatabaseConnect databaseConnect) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://" + databaseConnect.getIp() + "/" + databaseConnect.getDatabaseName(), databaseConnect.getUsername(), databaseConnect.getPassword());
        return conn;
    }

    public static List<Table> getAllTableName(String dabaseid){
        Connection conn = getSQLConnection(dabaseid);
        return getAllTableName(conn);

    }
    /**
     * 获取当前数据库下的所有表名称
     *
     * @return
     * @throws Exception
     */
    public static List<Table> getAllTableName(Connection conn) {
        try {
            String databaseid = "";
            for(String databaseidtem : connMap.keySet()){
                if(conn == connMap.get(databaseidtem)){
                    databaseid = databaseidtem;
                    break;
                }
            }
            List<Table> tables = new ArrayList();
            Statement stmt = conn.createStatement();
            String sql;

            String databasename = ((ConnectionImpl) conn).getDatabase();
           if(conn.getClientInfo().size() > 0 && conn.getClientInfo().getProperty("ApplicationName").equals("PostgreSQL JDBC Driver")){
               sql = " select tablename from pg_tables where schemaname='public'";
            }else{
               sql = "select TABLE_NAME,TABLE_COMMENT from INFORMATION_SCHEMA.Tables where table_schema = '"+databasename+"' ";
           }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                String content = rs.getString(2);
                List<TableColumn> tableColumns = getColumnCommentByTableName(conn, tableName);
                Table table = new Table(databaseid,tableName,content, tableColumns);
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
        String sql="";
        tableName =tableName.toLowerCase();
        try {
            Statement stmt = conn.createStatement();

            if(conn.getClientInfo().size() > 0 && conn.getClientInfo().getProperty("ApplicationName").equals("PostgreSQL JDBC Driver")){
                       sql ="select a.attname as Field,format_type(a.atttypid,a.atttypmod) as Type, " +
                               "(case " +
                               "when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='p')>0 then 'PRI' \n" +
                               "else '' " +
                               "end) as KEY, " +
                               "(case " +
                               "when a.attnotnull=true then 'Y' " +
                               "else 'N' " +
                               "end) as nullable, " +
                               "col_description(a.attrelid,a.attnum) as comment " +
                               "from pg_attribute a  "+
                               "where attstattarget=-1 and attrelid = (select oid from pg_class where relname ='"+tableName+"')" ;
            }else{
              sql = "show full columns from " + tableName.toLowerCase();
            }
            ResultSet rs = stmt.executeQuery(sql);
            List<TableColumn> tableColumns = new ArrayList<>();
            while (rs.next()) {
                tableColumns.add(TableColumn.newInstance(rs));
            }
            rs.close();
            stmt.close();
            if(tableColumns.size() == 0){
                throw  new GlobRuntimeException(tableName+" :数据库没有这张表，或者表内没有字段");
            }
            return tableColumns;
        } catch (SQLException e) {
            System.out.println(sql);
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
        throw  new GlobRuntimeException("此表没有主键");
    }
    public static TableColumn findColumnByPRI(Connection conn,String tablename) {
        List<TableColumn> columns = Connect.getColumnCommentByTableName(conn, tablename);
        return Connect.findColumnByPRI(columns);
    }
    public static Connection getSQLConnection(String connId) {

        Connection connect = connMap.get(connId);
        if (connect == null) {
            DatabaseConnect databaseConnect = Select.findPoById(SystemConnect.getConn(), DatabaseConnect.class, connId);
            if (databaseConnect != null) {
                try {
                    connect = Connect.getSQLConnection(databaseConnect);
                    connMap.put(connId, connect);
                } catch (Exception e) {
                    throw  new GlobRuntimeException("连接数据库异常"+e.getMessage());
                }
            }
        }
        return  connect;
    }


    public static void removeSQLConnection(String id) {
        if(id != null){
            connMap.remove(id);
        }
    }


    public static Table getTable(Connection coon, String tableName) {
        List<Table> tables = getAllTableName(coon);
        for (Table table : tables){
            if(table.getName().equals(tableName)){
                return  table;
            }
        }
        return  null;
    }

    public static void breakConnect() {
        try {
            for (String key:connMap.keySet()
            ) {
                Connection connect = connMap.get(key);
                connect.close();
            }
            connMap.clear();

            for (String key:userMap.keySet()
            ) {
                Connection connect = userMap.get(key);
                connect.close();
            }
            userMap.clear();

        }catch (SQLException s){

        }


    }
}
