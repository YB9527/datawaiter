package cn.yb.datawaiter.crawler;

import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.Column;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Param;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.tools.FileTool;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Controller
@RestController
@RequestMapping(value = "/crawler")
public class CrawlerController extends BasicController {

    @RequestMapping(value = "/get")
    public Respon getRequst(String url) throws IOException {
        Respon respon = startRespon();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("application/x-www-form-urlencoded", "gbk")
                .build();
        Call call = new OkHttpClient().newCall(request);
        okhttp3.Response res = call.execute();
        if(res.isSuccessful()){
            byte[] b = res.body().bytes(); //获取数据的bytess
            String info = new String(b, "GB2312"); //然后将其转为gb2312
            return respon.ok(info);
        }else {
            return  respon.responError("请求失败："+url);
        }

    }
    @RequestMapping(value = "/test")
    public Respon test(){
        File text = new File("d:\\123 (2).txt");
        Respon respon = startRespon();
        try {
            FileReader fr = new FileReader(text);
            BufferedReader br = new BufferedReader(fr);
            String tempString = null;
            int line = 1;
            List<String > sqls = new ArrayList<>();
            List<JSONObject> jsons = new ArrayList<>();
            int i=0;
            while ((tempString = br.readLine()) != null) {//BufferedReader有readLine()，可以实现按行读取
                JSONObject json = JSONObject.parseObject(tempString);
                sqls.add( "insert into m_dict_data(id,pid,dictid,code,label) values ('"+json.getString("id")+"' ," +
                        "'"+json.getString("pid")+
                        "' ,'"+json.getString("dictid")+"' ,'"+
                        json.getString("code")+"' ,'"+
                        json.getString("label")+"' )");
                switch (i++ % 3  ){
                    case 0:
                        json.put("y",true);
                        json.put("tt",new Date());
                        break;
                    case  1:
                        break;
                    case  2:
                        json.put("y",null);
                        break;
                }
                jsons.add(json);
                //break;
            }
            int count = jsons.size();
            //FileTool.writeStrs(sqls,"d:/444.txt");
            //Connection sqlConnection = Connect.getSQLConnection("5e687c02-60f0-4bd8-b6f7-6a202986b998");

            int xzqy2 = Insert.insertManyJSONs(SysConn, "xzqy", jsons);
            SysConn.commit();
            return respon.ok(xzqy2);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
       return  respon.responError("操作失败");

    }
}
