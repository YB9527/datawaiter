package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.model.Respon;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/jdbc")
public class DataController extends  BasicController {
    @RequestMapping(value = "/get")
    public Respon get(String dabasekey,String sql) {
        Connection postgressconno = Connect.getSQLConnection(new DatabaseConnect("192.168.30.12:5432",DatabaseEnum.postgress,"lqdb","postgres","1"));

        String sys_cfg_app_domain = "select dkbm, dkbz,dkmc from dk LIMIT 1000 OFFSET 0";
        List<JSONObject> domain = Select.findDataBySQL(postgressconno, sys_cfg_app_domain);
        return responOk(domain);
    }

    @PostMapping("/post")
    @ResponseBody
    public String saveAppversion() {
        return  "post";
    }
}
