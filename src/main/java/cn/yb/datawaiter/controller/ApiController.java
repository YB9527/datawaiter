package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.model.Respon;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api")
public class ApiController extends  BasicController {

    @RequestMapping(value = "/findbylevelid")
    public Respon get(String id) {
        String sql = "select * from  api where levelManagerId = "+sqlStr(id);
        List<JSONObject> domain = Select.findDataBySQL(SysConn, sql);
        return responOk(domain);
    }

    @RequestMapping(value = "/deleteByTableNameAndId")
    public Respon get(String tablename,String id) {
        if(tablename == null || id == null){
            return  responBasicError();
        }
        int count = Delete.deleteDataByPri(SysConn,tablename,id);
        return responOk(count);
    }
}
