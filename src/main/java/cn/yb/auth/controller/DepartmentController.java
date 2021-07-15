package cn.yb.auth.controller;

import cn.yb.auth.model.Department;
import cn.yb.auth.model.Position;
import cn.yb.auth.model.User;
import cn.yb.auth.service.impl.IDepartmentService;
import cn.yb.auth.service.impl.IUserService;
import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Respon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Controller
@RestController
@RequestMapping(value = "/department")
public class DepartmentController extends BasicController {
    @Autowired
    private IDepartmentService departmentService;

    @PostMapping("/edit")
    public Respon edit(@RequestBody Department department) {
        Respon respon = startRespon();
        int count = departmentService.edit(department);
        return  respon.count(count);
    }

    @PostMapping("/delete")
    public Respon delete(@RequestBody Department department) {
        Respon respon = startRespon();
        int count = departmentService.delete(department);
        return  respon.count(count,"部门有成员无法删除");
    }

}
