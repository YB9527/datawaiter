package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.mapper.ApiMapper;
import cn.yb.datawaiter.model.entity.Respon;
import cn.yb.datawaiter.service.TestService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@RequestMapping(value = "/test")
public class TestController extends BasicController {

    @Autowired
    private TestService testService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/t")
    public Respon findApiById(String id) throws SQLException, ClassNotFoundException {
        Respon respon = startRespon();
        testService.fun();
       /* DatabaseConnect dc = new DatabaseConnect("127.0.0.1", DatabaseEnum.mysql,"restaurant","root","1234");
        dc.setLabel("系统数据库");

        Connection conn = Connect.getSQLConnection(dc);
        List<JSONObject>  jsonObjects = Select.findDataBySQL(conn,"SELECT * FROM FOOD");*/
        return respon.ok( "");
    }

    @RequestMapping("/{a}")
    public Respon datawaiter(HttpServletRequest request, HttpServletResponse response ) {
        return handel(request, response);
    }

    public Respon handel(HttpServletRequest request, HttpServletResponse response) {
        Respon respon = startRespon();
        String url = request.getRequestURI();
       JSONObject jsonObject = getJSONParam(request);
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
