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

        String sys_cfg_app_domain = "select * from user ";
        long start = System.currentTimeMillis();
        List<JSONObject> domain = Select.findDataBySQL(SystemConnect.getConn(), sys_cfg_app_domain);
        long total = System.currentTimeMillis()- start;
        return responOk((Object)total);
    }

    @PostMapping("/post")
    @ResponseBody
    public String saveAppversion() {
        return  "post";
    }
}
