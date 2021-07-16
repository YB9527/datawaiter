package cn.yb.auth.service;

import cn.yb.auth.model.Position;
import cn.yb.auth.model.User;
import cn.yb.auth.service.impl.IPositionService;
import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.model.Condition;
import com.alibaba.fastjson.JSONObject;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;
@Service
public class PostionService implements IPositionService {
    @Override
    public int edit(Position position) {
        return JDBCUtils.editPo(SystemConnect.getConn(),position);
    }

    @Override
    public int delete(Position position) {
        //检查此职位是否有人
        Map<String,String> map = new TreeMap<>();
        map.put("postionid","="+position.getId());
        String sql = Select.getSQL(User.class,map);
        int  usercount = Select.findDataBySQL(SystemConnect.getConn(),sql).size();
        if(usercount == 0){
            return   Delete.deleteDataByPri(SystemConnect.getConn(),position);
        }
        return usercount;
    }
}
