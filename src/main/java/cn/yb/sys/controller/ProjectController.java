package cn.yb.sys.controller;

import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.Update;
import cn.yb.datawaiter.jdbc.model.Table;
import cn.yb.datawaiter.model.Mapper;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.Project;
import cn.yb.sys.service.impl.IFieldService;
import cn.yb.sys.service.impl.IProjectService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/project")
public class ProjectController extends BasicController {
    @Autowired
    private IProjectService projectService;



    @PostMapping("/edit")
    public Respon editMapper(@RequestBody Project project) {
        Respon respon = startRespon();
        if (project != null) {
            int count = projectService.edit(project);
            return respon.ok(count);
        }
        return respon.responError("保存失败");
    }

    @PostMapping("/updateAll")
    public Respon updateAll(@RequestBody List<Project> projectArray) {
        Respon respon = startRespon();
        if (projectArray != null && projectArray.size() > 0) {

            int count = projectService.edit(projectArray);
            return respon.ok(count);
        }
        return respon.responError("保存失败");
    }

    @RequestMapping(value = "/findById")
    public Respon findById(String id) {
        Respon respon = startRespon();
        if(Tool.isEmpty(id)){
            return respon.responError("id不能为null");
        }
        return respon.ok(projectService.findById(id));
    }

    @RequestMapping(value = "/findall")
    public Respon findAll() {
        Respon respon = startRespon();

        return respon.ok(projectService.findAll());
    }

    /**
     * 查询项目所有的表格
     * @return
     */
    @RequestMapping(value = "/findalltablebyprojectid")
    public Respon findalltablebyprojectid(String projectid) {
        Respon respon = startRespon();
        //List<Table> tables =  projectService.findAllTable(projectid);
        List<JSONObject> databaseInTables =  projectService.findDatabaseInTables(projectid);
        return respon.ok(databaseInTables);
    }
}
