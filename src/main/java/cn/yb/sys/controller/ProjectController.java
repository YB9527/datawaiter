package cn.yb.sys.controller;

import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.controller.IDataController;
import cn.yb.datawaiter.model.entity.Respon;
import cn.yb.datawaiter.model.query.QueryBase;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.Project;
import cn.yb.sys.model.ProjectVo;
import cn.yb.sys.service.impl.IProjectService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* 类注解 */
@Api(value = "desc of class")
@Controller
@RestController
@RequestMapping(value = "/project")
public class ProjectController extends BasicController  implements IDataController<String,String,QueryBase> {
    @Autowired
    private IProjectService projectService;



    @PostMapping("/edit")
    public Respon edit(@RequestBody Project project) {
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
    @GetMapping(value = "/findById")
    public Respon findById(@ApiParam(value = "根据ID查找" , required=true )String id) {
        return  findDataById(id);
    }

    public Respon findDataById(String id) {
        Respon respon = startRespon();
        if(Tool.isEmpty(id)){
            return respon.responError("id不能为null");
        }
        return respon.ok(projectService.findById(id));
    }



    @ApiOperation(value = "desc of metho多少发多少d", notes = "实打实")
    @PostMapping(value = "/findall")
    public Respon findAll(@ApiParam(value = "QueryBase参数" , required=true )  @RequestBody QueryBase data) {
        Respon respon = startRespon();

        return respon.ok(projectService.findAll(data.getSearchkey()));
    }

    /**
     * 查询项目所有的表格
     * @return
     */
    @GetMapping(value = "/findalltablebyprojectid")
    public Respon findalltablebyprojectid(String projectid) {
        Respon respon = startRespon();
        //List<Table> tables =  projectService.findAllTable(projectid);
        List<JSONObject> databaseInTables =  projectService.findDatabaseInTables(projectid);
        return respon.ok(databaseInTables);
    }



    public Respon findTotal(@RequestBody QueryBase data){
        Respon respon = startRespon();
        int count = projectService.findTotal(data.getSearchkey());
        return respon.ok(count);
    }


    public Respon findDataPage(@RequestBody QueryBase data){
        Respon respon = startRespon();
        List<ProjectVo> list = projectService.findPageData(data);
        return respon.ok(list);
    }

    @Override
    public Respon findDataAll(@RequestBody QueryBase data) {
        return findAll(data);
    }



    public  Respon updateData(@RequestBody String data) {
        Respon respon = startRespon();
        Project project = toObject(data,Project.class);
        if (project != null ) {
            int count = projectService.edit(project);
            return respon.ok(count);
        }
        return respon.responError("保存失败");
    }

    @Override
    public Respon saveData(String data) {

        return updateData(data);
    }

    @Override
    public Respon deleteData(String data) {
        return null;
    }


}
