package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.model.Respon;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/jdbc")
public class DataController extends  BasicController {

    @RequestMapping(value = "/get")
    public Respon get(String dabasekey,String sql) {
        Respon respon = startRespon();
        String sys_cfg_app_domain = "select * from user ";
        List<JSONObject> domain = Select.findDataBySQL(SystemConnect.getConn(), sys_cfg_app_domain);
        return respon.ok(domain);
    }

    @PostMapping("/post")
    @ResponseBody
    public String saveAppversion() {
        return  "post";
    }
}
