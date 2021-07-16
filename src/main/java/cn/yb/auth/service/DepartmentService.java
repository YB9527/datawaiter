package cn.yb.auth.service;

import cn.yb.auth.model.Department;
import cn.yb.auth.model.Position;
import cn.yb.auth.model.User;
import cn.yb.auth.service.impl.IDepartmentService;
import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.SystemConnect;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;
@Service
public class DepartmentService implements IDepartmentService {
    @Override
    public int edit(Department department) {
        JDBCUtils.editPo(SystemConnect.getConn(),department);
        return 0;
    }

    @Override
    public int delete(Department department) {
        //检查此职位是否有人
        Map<String,String> map = new TreeMap<>();
        map.put("postionid","="+department.getId());
        String sql = Select.getSQL(User.class,map);
        int  usercount = Select.findDataBySQL(SystemConnect.getConn(),sql).size();
        if(usercount == 0){
            return   Delete.deleteDataByPri(SystemConnect.getConn(),department);
        }
        return usercount;
    }
}
