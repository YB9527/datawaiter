package cn.yb.sys.service.impl;

import cn.yb.sys.model.Project;

import java.util.List;

public interface IProjectService {
    int edit(Project project);

    List<? extends Project> findAll();

    Project findById(String id);

    int edit(List<Project> projectArray);

    Project findByURL(String projecturl);
}
