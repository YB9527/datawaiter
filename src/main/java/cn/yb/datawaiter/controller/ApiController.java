package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.Column;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Param;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping(value = "/api")
public class ApiController extends BasicController {
    @Autowired
    private IDatawaiterService datawaiterService;


    @PostMapping("/testPostApi")
    public Respon testPostApi(@RequestBody Api api) {
        try {
            return responOk(datawaiterService.handleData(api)) ;
        }catch (GlobRuntimeException e){
            return responError(e.getMessage()) ;
        }

    }

    @RequestMapping(value = "/findApiById")
    public Respon findApiById(String id) {

        if(Tool.isEmpty(id)){
            return responError("无id");
        }
        return responOk( Select.findPoById(SysConn,Api.class, id));
    }

    @RequestMapping(value = "/findbylevelid")
    public Respon findbylevelid(String id) {

        String sql = "select * from  api where levelId = " + sqlStr(id);
        List<JSONObject> domain = Select.findDataBySQL(SysConn, sql);
        /*for (JSONObject json : domain){
            String  apiId = json.getString("id");
            String paramSQL = "select * from  param where apiId = "+sqlStr(apiId);
            List<JSONObject> paramas = Select.findDataBySQL(SysConn, sql);

        }*/
        return responOk(domain);
    }

    @RequestMapping(value = "/findParamsByApiId")
    public Respon findParamsByApiId(String apiId) {
        List<Param> params = sysService.findParamsByApiId(apiId);
        return responOk(params);
    }

    @RequestMapping(value = "/deleteByTableNameAndId")
    public Respon get(String tablename, String id) {
        if (tablename == null || id == null) {
            return responBasicError();
        }
        int count = Delete.deleteDataByPri(SysConn, tablename, id);
        return responOk(count);
    }

    @PostMapping("/saveApi")
    public Respon saveApi(@RequestBody JSONObject json) {
        int count = Insert.insertJSON(SysConn, Api.class.getSimpleName(), json);
        List<Param> params = json.getJSONArray("params").toJavaList(Param.class);
        int count2 = Insert.insertManyPos(SysConn, params);
        return responOk(count);
    }

    /**
     * 修改 或者 保存 api
     *
     * @param api
     * @return
     */
    @PostMapping("/editApi")
    public Respon editApi(@RequestBody Api api) {
        String tableName = Api.class.getSimpleName();
        String sql = api.getSql_().replace("\"","'");

        api.setSql_(sql);
        JSONObject dbPo = Select.findDataById(SysConn,tableName, api.getId());
        List<Param> paramList = api.getParams();
        if (dbPo == null) {
            //保存
            int count = Insert.insertPo(SysConn, api);
            int count2 = Insert.insertManyPos(SysConn,  paramList);
            return responOk(count);
        } else {
            //编辑
            Update.updateDataPo(SysConn,  api);
            Delete.deleteByColoumnAndValues(SysConn, Param.class.getSimpleName(), "apiId", new String[]{api.getId()});
            int count2 = Insert.insertManyPos(SysConn,  paramList);
        }
        return responOk(1);
    }

    @PostMapping("/findDataByAPI")
    public Respon findDataByAPI(@RequestBody Api api) {
        try {
            return responOk(datawaiterService.findDataByApi(api));
        } catch (GlobRuntimeException e) {
            return responError(e.getMessage());
        }
    }
}
