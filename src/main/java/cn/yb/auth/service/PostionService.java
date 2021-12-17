package cn.yb.auth.service;

import cn.yb.auth.model.Position;
import cn.yb.auth.service.impl.IPositionService;
import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.jdbc.model.SelectBuild;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostionService implements IPositionService {
    @Override
    public int edit(Position position) {
        return JDBCUtils.editPo(SystemConnect.getConn(),position);
    }

    @Override
    public int delete(Position position) {
        //检查此职位是否有人
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("positionid=",position.getId());
        String sql = Select.getSQL("user_position",map);
        int  usercount = Select.findDataBySQL(SystemConnect.getConn(),sql).size();
        if(usercount == 0){
            return   Delete.deleteDataInServerice(SystemConnect.getConn(),position);
        }
        return usercount;
    }

    @Override
    public List<Position> findGroupByProjectid(String projectid) {
        return SelectBuild.newInstance(Position.class)
                .setWhereFiled("projectid = ",projectid)
                .setWhereFiled(" AND pid IS NULL",null)
                .build(SystemConnect.getConn(),Position.class);
    }

    @Override
    public List<Position> findByProjectid(String projectid) {
        return SelectBuild.newInstance(Position.class)
                .setWhereFiled("projectid = ",projectid)
                .order("ORDER BY  level")
                .build(SystemConnect.getConn(),Position.class);
    }
}
