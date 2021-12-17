package cn.yb.auth.controller;

import cn.yb.auth.model.Organization;
import cn.yb.auth.service.impl.IOrganizationService;
import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.model.entity.Respon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController extends BasicController {
    @Autowired
    private IOrganizationService organizationService;


    @PostMapping("/edit")
    public Respon edit(@RequestBody Organization organization) {
        Respon respon = startRespon();
        int count = organizationService.edit(organization);
        return  respon.count(count);
    }

    @RequestMapping(value = "/findByProjectid")
    public Respon findByProjectid(String projectid){
        Respon respon = startRespon();
        List<Organization> list = organizationService.findByProjectid(projectid);
        return respon.ok(list);
    }

    @PostMapping("/delete")
    public Respon delete(@RequestBody Organization organization) {
        Respon respon = startRespon();
        int count = organizationService.delete(organization);
        return  respon.ok(count);
    }

}
