package cn.yb.sys.service;

import cn.yb.datawaiter.controller.query.QueryBase;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.SelectBuild;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.Project;
import cn.yb.sys.model.ProjectVo;
import cn.yb.sys.service.impl.IProjectService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.*;

@Service
public class ProjectService implements IProjectService {
    private  Map<String,Project> projectURLMap = new HashMap<>();
    public Connection getSysConnection(){
        return  SystemConnect.getConn();
    }

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
        String sql = "SELECT * FROM (SELECT * FROM project WHERE isdelete != 1 OR isdelete is NULL  )project \n" +
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
        String sql ="SELECT * FROM (SELECT * FROM project WHERE id = '"+id+"' AND  (isdelete != 1 OR isdelete is NULL))project\n" +
                "\tLEFT JOIN (SELECT projectid,path as imagepath FROM fj WHERE isdelete != 1) as fj\n" +
                "\tON project.id = fj.projectid ";
        ProjectVo pro = Select.findOneDataBySQL(SystemConnect.getConn(),sql, ProjectVo.class);
        return pro;
    }

    @Override
    public int edit(List<Project> projectArray) {
        return Update.updateManyDataPosInService(SystemConnect.getConn(),projectArray);
    }

    @Override
    public Project findByURL(String projecturl) {
        if(projectURLMap.containsKey(projecturl)){
            return  projectURLMap.get(projecturl);
        }
        Map<String,Object> map = new LinkedHashMap();
        map.put(" url = ",projecturl);
        List<Project> projects = Select.findDataByMap(SystemConnect.getConn(),Project.class,map);
        if(projects.size() > 0){
            projectURLMap.put(projecturl,projects.get(0));
            return  projects.get(0);
        }
        return null;
    }



    @Override
    public List<JSONObject> findAllDatabaseByProjectid(String projectid) {
        String sql = "SELECT * FROM (SELECT * FROM databaseconnect  WHERE  projectid = '"+projectid+"' ) databaseconnect\n" +
                "\t\tleft JOIN  \n" +
                "\t\t(SELECT databaseConnectId,count(*) as apiCount from api GROUP BY databaseConnectId) as api \n" +
                "\t\ton databaseconnect.id = api.databaseConnectId ";
        return Select.findDataBySQL(getSysConnection(),sql);
    }

    @Override
    public List<JSONObject> findDatabaseInTables(String projectid) {
        List<JSONObject> list = new ArrayList<>();
        List<DatabaseConnect> databaseConnects = SelectBuild.newInstance(DatabaseConnect.class)
                .setWhereFiled("projectid = " ,projectid)
                .build(getSysConnection(),DatabaseConnect.class);
        if(!Tool.isEmpty(databaseConnects)){
            for(DatabaseConnect connect : databaseConnects){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",connect.getId());
                jsonObject.put("databaseName",connect.getDatabaseName());
                jsonObject.put("label",connect.getLabel());
                jsonObject.put("tableArray",Connect.getAllTableName(connect.getId()));
                list.add( jsonObject);
            }
        }

        return list;
    }

    @Override
    public int findTotal(String name) {
        SelectBuild selectBuild =SelectBuild
                .newInstance(Project.class);
        if(!Tool.isEmpty(name)){
            selectBuild
                    .setWhereFiled(" name LIKE ","%"+name+"%");
        }
        int count = selectBuild
                .findCount(getSysConnection());
        return count;
    }

    @Override
    public List<ProjectVo> findPageData(QueryBase data) {

        SelectBuild selectBuild =SelectBuild
                .newInstance(Project.class);
        if(!Tool.isEmpty(data.searchkey)){
            selectBuild
                    .setWhereFiled(" name LIKE ","%"+data.searchkey+"%");
        }

        return selectBuild.setLimit(data.pagenum,data.pagecount).build(getSysConnection(),ProjectVo.class);
    }

    @Override
    public List<Table> findAllTable(String projectid) {
        List<Table> list = new ArrayList<>();
        List<DatabaseConnect> databaseConnects = SelectBuild.newInstance(DatabaseConnect.class)
                .setWhereFiled("projectid = " ,projectid)
                .build(getSysConnection(),DatabaseConnect.class);
        if(!Tool.isEmpty(databaseConnects)){
            for(DatabaseConnect connect : databaseConnects){
                list.addAll( Connect.getAllTableName(connect.getId()));
            }
        }

        return list;
    }
}
