package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.jdbc.model.DatabaseConnect;

public interface IDatabaseService {
    DatabaseConnect findDatabaseConnect(String databaseId);
}
