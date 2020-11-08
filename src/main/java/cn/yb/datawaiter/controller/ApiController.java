package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.Column;
import cn.yb.datawaiter.jdbc.model.TableColumn;
import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Param;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
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
     * @param json
     * @return
     */
    @PostMapping("/editApi")
    public Respon editApi(@RequestBody JSONObject json) {
        String tableName = Api.class.getSimpleName();
        TableColumn pri = Connect.findColumnByPRI(SysConn,tableName);
        JSONObject dbPo = Select.findDataById(SysConn,tableName, json.getString(pri.getColumnName()));
        List<JSONObject> params = null;

        JSONArray paramsJSON = json.getJSONArray("params");
        if (paramsJSON != null && paramsJSON.size() > 0) {
            params = paramsJSON.toJavaList(JSONObject.class);
        }
        if (dbPo == null) {
            //保存
            int count = Insert.insertJSON(SysConn, Api.class.getSimpleName(), json);
            int count2 = Insert.insertManyJSONs(SysConn, Param.class.getSimpleName(), params);
            return responOk(count);
        } else {
            //编辑

            Update.updateDataJSON(SysConn, Api.class.getSimpleName(), json);
            Delete.deleteByColoumnAndValues(SysConn, Param.class.getSimpleName(), "apiId", new String[]{json.getString("id")});
            int count2 = Insert.insertManyJSONs(SysConn, Param.class.getSimpleName(), params);
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
