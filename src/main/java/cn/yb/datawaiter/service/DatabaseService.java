package cn.yb.datawaiter.service;

import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.service.impl.IDatabaseService;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import org.springframework.stereotype.Component;

@Component
public class DatabaseService implements IDatabaseService {

    @Override
    public DatabaseConnect findDatabaseConnect(String id) {

        return Select.findDataById2Po(SystemConnect.getConn(),DatabaseConnect.class,id);
    }
}
