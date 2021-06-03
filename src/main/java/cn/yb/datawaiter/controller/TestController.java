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
import cn.yb.datawaiter.model.QuestMethod;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/{a}")
    public Respon datawaiter(HttpServletRequest request, HttpServletResponse response ) {
        return handel(request, response);
    }

    public Respon handel(HttpServletRequest request, HttpServletResponse response) {
        Respon respon = startRespon();
        String url = request.getRequestURI();
        Map<String, String> paramMap = getAllRequestParam(request);
        return respon.ok("");

    }
    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {

        //获取到JSONObject
        JSONObject jsonParam = this.getJSONParam(request);
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }
        return res;
    }
    public JSONObject getJSONParam(HttpServletRequest request){
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            jsonParam = JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
    }
}
