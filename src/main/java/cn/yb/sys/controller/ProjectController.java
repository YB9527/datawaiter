package cn.yb.sys.controller;

import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.model.Mapper;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.Project;
import cn.yb.sys.service.impl.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
