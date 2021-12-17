package cn.yb.sys.service.impl;

import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.query.QueryBase;
import cn.yb.sys.model.Project;
import cn.yb.sys.model.ProjectVo;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface IProjectService {
    int edit(Project project);

    List<? extends Project> findAll(String name);

    Project findById(String id);

    int edit(List<Project> projectArray);

    Project findByURL(String projecturl);

    List<Table > findAllTable(String projectid);

    List<JSONObject> findAllDatabaseByProjectid(String projectid);

    List<JSONObject> findDatabaseInTables(String projectid);

    int findTotal(String name);

    List<ProjectVo> findPageData(QueryBase data);
}
