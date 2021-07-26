package cn.yb.sys.service;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.sys.model.Project;
import cn.yb.sys.model.ProjectVo;
import cn.yb.sys.service.impl.IProjectService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService implements IProjectService {
    @Override
    public int edit(Project project) {
        return JDBCUtils.editPoInService(SystemConnect.getConn(),project);
    }

    @Override
    public List<? extends Project> findAll() {

        return Select.findDataBySQL(SystemConnect.getConn(),findSQL(null), ProjectVo.class);
        //return Select.findDataAllToPo(SystemConnect.getConn(),Project.class);
    }
    public String findSQL(String id){
        String sql = "SELECT * FROM project \n" +
                "\tLEFT JOIN (SELECT objectid,path as imagepath   FROM fj WHERE isdelete != 1) as fj\n" +
                "\tON project.id = fj.objectid ORDER BY seq";
        if (id == null){
            return  sql;
        }else{
            return  sql + " AND id = '"+id+"'";
        }
    }
    @Override
    public ProjectVo findById(String id) {
        String sql ="SELECT * FROM (SELECT * FROM project WHERE id = '"+id+"' )project\n" +
                "\tLEFT JOIN (SELECT projectid,path as imagepath FROM fj WHERE isdelete != 1) as fj\n" +
                "\tON project.id = fj.projectid ";
        ProjectVo pro = Select.findOneDataBySQL(SystemConnect.getConn(),sql, ProjectVo.class);
        return pro;
    }

    @Override
    public int edit(List<Project> projectArray) {
        return Update.updateManyDataPosInService(SystemConnect.getConn(),projectArray);
    }
}
