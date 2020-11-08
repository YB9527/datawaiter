package cn.yb.datawaiter.service;

import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Param;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.service.impl.ISysService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Service
public class DatawaiterService implements IDatawaiterService {

    @Autowired
    ISysService sysService;


    @Override
    public List<JSONObject> findDataByApi(Api api) {
        if (api != null) {
            String connId = api.getDatabaseConnectId();
            Connection conn = Connect.getSQLConnection(connId);
            String sql = api.getSql_();
            List<Param> params = api.getParams();
            if (params != null) {
                for (Param param : params) {
                    String paramName = param.getParamName();
                    if (paramName != null) {
                        String value = param.getTestValue();
                        value = value == null ? "" : value;
                        sql = sql.replace(paramName, value);
                    }
                }
            }
            return Select.findDataBySQL(conn, sql);
        }
        return null;
    }
}
