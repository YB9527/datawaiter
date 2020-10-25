package cn.yb.datawaiter.jdbc.model;

import lombok.Data;

import java.util.Objects;

@Data
public class DatabaseConnect {
    private String id;
    private DatabaseEnum databaseEnum;
    private  String ip;
    private  String databaseName;
    private  String name;
    private  String password;
    public DatabaseConnect(String ip,DatabaseEnum databaseEnum,String databasename,String name, String password ) {
        this.ip = ip;
        this.databaseEnum =databaseEnum;
        this.name = name;
        this.password = password;
        this.databaseName = databasename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseConnect that = (DatabaseConnect) o;
        return Objects.equals(id, that.id) &&
                databaseEnum == that.databaseEnum &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(databaseName, that.databaseName) &&
                Objects.equals(name, that.name) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, databaseEnum, ip, databaseName, name, password);
    }
}
