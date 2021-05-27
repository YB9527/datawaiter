package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.model.Column;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.service.impl.IApiService;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/test")
public class TestController extends BasicController {



    @RequestMapping(value = "/t")
    public Respon findApiById(String id) throws SQLException, ClassNotFoundException {
        Respon respon = startRespon();

        DatabaseConnect dc = new DatabaseConnect("127.0.0.1", DatabaseEnum.mysql,"restaurant","root","1234");

        dc.setLabel("系统数据库");

        Connection conn = Connect.getSQLConnection(dc);
        List<JSONObject>  jsonObjects = Select.findDataBySQL(conn,"SELECT * FROM FOOD");
        return respon.ok( jsonObjects);
    }
}
