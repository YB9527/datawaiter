package cn.yb.auth.controller;

import cn.yb.auth.model.Position;
import cn.yb.auth.service.impl.IPositionService;
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
@RequestMapping(value = "/postion")
public class PostionController extends BasicController {
    @Autowired
    private IPositionService positionService;

    @PostMapping("/edit")
    public Respon edit(@RequestBody Position position) {
        Respon respon = startRespon();
        int count = positionService.edit(position);
        return  respon.count(count);
    }

    @PostMapping("/delete")
    public Respon delete(@RequestBody Position position) {
        Respon respon = startRespon();
        int count = positionService.delete(position);
        return  respon.ok(count);
    }

    @RequestMapping(value = "/findGroupByProjectid")
    public Respon findGroupByProjectid(String projectid){
        Respon respon = startRespon();
        List<Position>   list = positionService.findGroupByProjectid(projectid);
        return respon.ok(list);

    }

    @RequestMapping(value = "/findByProjectid")
    public Respon findByProjectid(String projectid){
        Respon respon = startRespon();
        List<Position>   list = positionService.findByProjectid(projectid);
        return respon.ok(list);
    }

}
