package cn.yb.datawaiter.jdbc.model;

import lombok.Data;

import java.util.Objects;

@Data
public class DatabaseConnect {
    private DatabaseEnum databaseEnum;
    private  String ip;
    private  String databaseName;
    private  String username;
    private  String password;
    public DatabaseConnect(String ip,DatabaseEnum databaseEnum,String databasename,String user, String password ) {
        this.ip = ip;
        this.databaseEnum =databaseEnum;
        this.username = user;
        this.password = password;
        this.databaseName = databasename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseConnect that = (DatabaseConnect) o;
        return databaseEnum == that.databaseEnum &&
                Objects.equals(databaseName, that.databaseName) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(databaseEnum, databaseName, username, password);
    }
}
