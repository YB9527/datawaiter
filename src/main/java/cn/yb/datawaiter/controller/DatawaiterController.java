package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.model.entity.*;
import cn.yb.datawaiter.service.impl.IDatawaiterService;

import cn.yb.datawaiter.service.impl.IMapperService;
import cn.yb.sys.model.Project;
import cn.yb.sys.service.impl.IProjectService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Controller
@RestController
@RequestMapping(value = "/datawaiter")
public class DatawaiterController extends BasicController {


    public String baseURL = "/datawaiter";

    @RequestMapping("/{a}")
    public Respon datawaiter(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping("/{a}/{a}")
    public Respon datawaiter1(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping("/{a}/{a}/{b}")
    public Respon datawaiter2(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping("/{a}/{a}/{b}/{c}")
    public Respon datawaiter3(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping("/{a}/{a}/{b}/{c}/{d}")
    public Respon datawaiter4(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping("/{a}/{a}/{b}/{c}/{d}/{e}")
    public Respon datawaiter5(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping("/{a}/{a}/{b}/{c}/{d}/{e}/{e}")
    public Respon datawaiter6(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping("/{a}/{a}/{b}/{c}/{d}/{e}/{d}/{a}")
    public Respon datawaiter7(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping("/{a}/{a}/{b}/{c}/{d}/{e}/{d}/{d}/{a}")
    public Respon datawaiter8(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @Autowired
    private IDatawaiterService datawaiterService;

    @Autowired
    private IMapperService mapperService;

    @Autowired
    private IProjectService projectService;

    public Respon handel(HttpServletRequest request, HttpServletResponse response) {
        Respon respon = startRespon();
        try {
            String url = request.getRequestURI();
            int index = url.indexOf("/",1);
            int end = url.indexOf("/",index+1);
            String  projecturl = url.substring(index+1,end);
            Project project = projectService.findByURL(projecturl);
            String relative = url.replace(baseURL+"/"+projecturl, "");
            ApiEntity apiEntity = sysService.findApiByURLAndProjectid(relative,project.getId());

            if (apiEntity != null) {

              /*  List<Param> params = new ArrayList<>();
                for (String paramName : paramMap.keySet()) {
                    if (paramName != null) {
                        String value = paramMap.get(paramName);
                        value = value == null ? "" : value;
                        params.add(new Param("[" + paramName + "]", value));
                    }
                }*/
                //api.setParams(params);
                if (apiEntity.getQuestMethod() == QuestMethod.GET) {
                    Map<String, String> paramMap = getAllRequestParam(request);
                    JSONObject jsonObject = getJSONParam(request);
                    if(jsonObject != null){
                        for (String key : jsonObject.keySet()){
                            paramMap.put(key,jsonObject.getString(key));
                        }
                    }

                    List<JSONObject> jsons = datawaiterService.findDataByMapper(apiEntity,paramMap);
                    if (jsons != null) {
                        return respon.ok(jsons);
                    }
                } else {

                    JSONObject jsonObject = getJSONParam(request);
                    System.out.println(jsonObject);
                    return respon.ok(datawaiterService.handleData(apiEntity,jsonObject));
                }
            }
            return respon.responError("URL地址有问题：" + url);
        } catch (GlobRuntimeException  e) {
            e.printStackTrace();
            return  respon.responError(e.getMessage());
        }
    }
    public JSONObject getJSONParam(HttpServletRequest request){
        JSONObject jsonParam = new JSONObject();
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            String str = sb.toString();
            if(str.startsWith("[")){
                jsonParam.put("_Array_",JSONArray.parse(str));
            }else {
                jsonParam = JSONObject.parseObject(sb.toString());
            }
            if(jsonParam == null){
                jsonParam = new JSONObject();
            }
            Map<String, String> map = getAllRequestParam(request);
            for (String key : map.keySet() ) {
                jsonParam.put(key,map.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
    }
    public Respon mapperTesthandel(HttpServletRequest request, HttpServletResponse response) {
        Respon respon = startRespon();
        try {
            Map<String, String> paramMap = getAllRequestParam(request);
            MapperEntity mapperEntity = mapperService.findMapperById(paramMap.get("mapperId"));
            paramMap.remove("mapperId");
            if(mapperEntity != null){
                if(mapperEntity.getCrud() != MapperCreateEnum.SELECT){
                    int count =  mapperService.handelData(CRUDEnum.SELECT , mapperEntity,paramMap);
                    return respon.ok(count);
                }
            }

            return respon.responError("URL地址有问题：");
        } catch (GlobRuntimeException e) {
            return respon.responError(e.getMessage());
        }
    }

    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
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




    @RequestMapping(value = "/{a}", method = RequestMethod.POST)
    public Respon postdatawaiter(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping(value = "/{a}/{a}", method = RequestMethod.POST)
    public Respon postdatawaiter1(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping(value = "/{a}/{a}/{b}", method = RequestMethod.POST)
    public Respon postdatawaiter2(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping(value = "/{a}/{a}/{b}/{c}", method = RequestMethod.POST)
    public Respon postdatawaiter3(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping(value = "/{a}/{a}/{b}/{c}/{d}", method = RequestMethod.POST)
    public Respon postdatawaiter4(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping(value = "/{a}/{a}/{b}/{c}/{d}/{e}", method = RequestMethod.POST)
    public Respon postdatawaiter5(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping(value = "/{a}/{a}/{b}/{c}/{d}/{e}/{e}", method = RequestMethod.POST)
    public Respon postdatawaiter6(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping(value = "/{a}/{a}/{b}/{c}/{d}/{e}/{d}/{a}", method = RequestMethod.POST)
    public Respon postdatawaiter7(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }

    @RequestMapping(value = "/{a}/{a}/{b}/{c}/{d}/{e}/{d}/{d}/{a}", method = RequestMethod.POST)
    public Respon postdatawaiter8(HttpServletRequest request, HttpServletResponse response) {
        return handel(request, response);
    }
}

