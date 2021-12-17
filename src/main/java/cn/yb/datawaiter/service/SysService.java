package cn.yb.datawaiter.service;

import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.model.entity.ApiEntity;
import cn.yb.datawaiter.service.impl.ISysService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

@Service
public class SysService implements ISysService {

    @Autowired
    SystemConnect systemConnect;
    @Value("${SystemDatabase.ip}")
    public  void setIp(String ip) {
        SysConn = systemConnect.getConn();
    }
    private Connection SysConn;


    public ApiEntity findApiByURL(String relativeURL) {
        String sql = "SELECT * FROM "+ ApiEntity.class.getSimpleName().toLowerCase()+" WHERE  Concat(Concat(rooturl , '/'), selfURL) ="+ JDBCUtils.sqlStr(relativeURL);
        List<JSONObject> jsons = Select.findDataBySQL(SysConn,sql);
        if(Tool.isEmpty(jsons)){
            return  null;
        }
        return jsons.get(0).toJavaObject(ApiEntity.class);
    }

    @Override
    public ApiEntity findApiByURLAndProjectid(String relativeURL, String projectid) {

        String sql = "SELECT * FROM "+ ApiEntity.class.getSimpleName().toLowerCase()+" WHERE  projectid= '"+projectid+"' AND Concat(Concat(rooturl , '/'), selfURL) ="+ JDBCUtils.sqlStr(relativeURL);
        List<JSONObject> jsons = Select.findDataBySQL(SysConn,sql);
        if(Tool.isEmpty(jsons)){
            return  null;
        }
        return jsons.get(0).toJavaObject(ApiEntity.class);
    }
}
