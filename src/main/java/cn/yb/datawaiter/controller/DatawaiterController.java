package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.model.*;
import cn.yb.datawaiter.service.impl.IDatawaiterService;

import cn.yb.datawaiter.service.impl.IMapperService;
import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public Respon handel(HttpServletRequest request, HttpServletResponse response) {
        Respon respon = startRespon();
        try {
            String url = request.getRequestURI();
            String relative = url.replace(baseURL, "");
            Api api = sysService.findApiByURL(relative);

            if (api != null) {
                Map<String, String> paramMap = getAllRequestParam(request);
              /*  List<Param> params = new ArrayList<>();
                for (String paramName : paramMap.keySet()) {
                    if (paramName != null) {
                        String value = paramMap.get(paramName);
                        value = value == null ? "" : value;
                        params.add(new Param("[" + paramName + "]", value));
                    }
                }*/
                //api.setParams(params);
                if (api.getQuestMethod() == QuestMethod.GET) {
                    List<JSONObject> jsons = datawaiterService.findDataByMapper(api,paramMap);
                    if (jsons != null) {
                        return respon.ok(jsons);
                    }
                } else {

                    return respon.ok(datawaiterService.handleData(api,paramMap));
                }
            }
            return respon.responError("URL地址有问题：" + url);
        } catch (GlobRuntimeException e) {
            e.printStackTrace();
            return  respon.responError(e.getMessage());
        }
    }

    public Respon mapperTesthandel(HttpServletRequest request, HttpServletResponse response) {
        Respon respon = startRespon();
        try {
            Map<String, String> paramMap = getAllRequestParam(request);
            Mapper mapper = mapperService.findMapperById(paramMap.get("mapperId"));
            paramMap.remove("mapperId");
            if(mapper != null){
                if(mapper.getCrud() != MapperCreateEnum.SELECT){
                    int count =  mapperService.handelData(CRUDEnum.SELECT ,mapper,paramMap);
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

