package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.model.entity.ApiEntity;
import cn.yb.datawaiter.model.entity.Respon;
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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api")
public class ApiController extends BasicController {
    @Autowired
    private IDatawaiterService datawaiterService;
    @Autowired
    private IApiService apiService;

    @PostMapping("/testPostApi")
    public Respon testPostApi(@RequestBody ApiEntity apiEntity) {
        Respon respon = startRespon();
        try {

            //return responOk(datawaiterService.handleData(api)) ;
            return respon.ok("") ;
        }catch (GlobRuntimeException e){
            return respon.responError(e.getMessage()) ;
        }

    }

    @RequestMapping(value = "/findApiById")
    public Respon findApiById(String id) {
        Respon respon = startRespon();
        if(Tool.isEmpty(id)){
            return respon.responError("无id");
        }
        return respon.ok( Select.findPoById(SysConn, ApiEntity.class, id));
    }

    @RequestMapping(value = "/findbylevelid")
    public Respon findbylevelid(String id) {
        Respon respon = startRespon();
        String sql = "select * from  api where levelId = " + sqlStr(id)+" ORDER BY date";
        List<JSONObject> domain = Select.findDataBySQL(SysConn, sql);
        /*for (JSONObject json : domain){
            String  apiId = json.getString("id");
            String paramSQL = "select * from  param where apiId = "+sqlStr(apiId);
            List<JSONObject> paramas = Select.findDataBySQL(SysConn, sql);

        }*/
        return respon.ok(domain);
    }

    @RequestMapping(value = "/findParamsByApiId")
    public Respon findParamsByApiId(String apiId) {
        Respon respon = startRespon();

        return respon.ok("");
    }

    @RequestMapping(value = "/deleteByTableNameAndId")
    public Respon get(String tablename, String id) {
        Respon respon = startRespon();
        if (tablename == null || id == null) {
            return respon.responBasicError();
        }
        int count = Delete.deleteDataByPri(SysConn, tablename, id);
        return respon.ok(count);
    }


    @PostMapping("/saveApi")
    public Respon saveApi(@RequestBody ApiEntity apiEntity) {

        apiEntity.setDate(new Date());
      return  editApi(apiEntity);
    }
    /**
     * 修改 或者 保存 api
     *
     * @param apiEntity
     * @return
     */
    @PostMapping("/editApi")
    public Respon editApi(@RequestBody ApiEntity apiEntity) {
        Respon respon = startRespon();
        apiEntity.setDate(new Date());
        int count =0;
        try {
            SysConn.setAutoCommit(false);
             count = JDBCUtils.editPo(SysConn, apiEntity);
            SysConn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

       /* String tableName = Api.class.getSimpleName();
        JSONObject dbPo = Select.findDataById(SysConn,tableName, api.getId());
        if (dbPo == null) {
            //保存
            int count = Insert.insertPo(SysConn, api);
            //int count2 = Insert.insertManyPos(SysConn,  paramList);
            return responOk(count);
        } else {
            //编辑
            Update.updateDataPo(SysConn,  api);
            Delete.deleteByColoumnAndValues(SysConn, Param.class.getSimpleName(), "apiId", new String[]{api.getId()});
            //int count2 = Insert.insertManyPos(SysConn,  paramList);
        }*/

        return respon.ok(count);
    }

   /* @PostMapping("/findDataByAPI")
    public Respon findDataByAPI(@RequestBody Api api) {
        Respon respon = startRespon();
        try {

            return respon.ok(datawaiterService.findDataByMapper(api, params));
        } catch (GlobRuntimeException e) {
            return respon.responError(e.getMessage());
        }
    }*/
}
