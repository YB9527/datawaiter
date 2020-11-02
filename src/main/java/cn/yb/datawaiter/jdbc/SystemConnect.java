package cn.yb.datawaiter.jdbc;

import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class SystemConnect {
    private static String ip;
    private static DatabaseEnum databaseEnum;
    private static  String databaseName;
    private static String name;
    private static  String password;
    @Value("${SystemDatabase.ip}")
    public  void setIp(String ip) {
        SystemConnect.ip = ip;
    }
    @Value("${SystemDatabase.databaseEnum}")
    public void setDatabaseEnum(DatabaseEnum databaseEnum) {
        SystemConnect.databaseEnum = databaseEnum;
    }
    @Value("${SystemDatabase.databaseName}")
    public void setDatabaseName(String databaseName) {
        SystemConnect.databaseName = databaseName;
    }
    @Value("${SystemDatabase.name}")
    public void setName(String name) {
        SystemConnect.name = name;
    }
    @Value("${SystemDatabase.password}")
    public void setPassword(String password) {
        SystemConnect.password = password;
    }
    /***
     * 系统数据库
     */
    private static Connection systemConn;

    /***
     * 得到系统数据库的连接
     * @return
     */
    public static Connection getConn() {
        if (systemConn == null){
            try {
                DatabaseConnect dc = new DatabaseConnect(ip,databaseEnum,databaseName,name,password);
                dc.setLabel("系统数据库");
                systemConn = Connect.getSQLConnection(dc);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return systemConn;
    }
}
