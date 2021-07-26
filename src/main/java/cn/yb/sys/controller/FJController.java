package cn.yb.sys.controller;


import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.model.UploadFile;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.FJ;
import cn.yb.sys.model.Project;
import cn.yb.sys.service.impl.IFJService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;


@Controller
@RestController
@RequestMapping(value = "/fj")
public class FJController extends BasicController {
    @Autowired
    private IFJService fjService;
    private String uploadDir;

    @Value("#{${uploadconfig.dir}}")
    public void dirMap(Map<String,String> dirMap){
        String ip = Tool.getInterIP();
        String key ;
        if(ip.startsWith("172.18.254.48")){
            key = "out";
        }else{
            key = "in";
        }
        this.uploadDir = dirMap.get(key);
    }


    @GetMapping("/findImageByObjectidAndCustomname")
    public Respon findImageByObjectidAndCustomname(String objectid, String  customname) {
        Respon respon = startRespon();
        if(Tool.isEmpty(new String[]{objectid,customname})){
            return respon.responError("null查不出来");
        }
        return  respon.ok(fjService.findImageByObjectidAndCustomname(objectid,customname));
    }

    @PostMapping("/delete")
    @ResponseBody
    public Respon delete(@RequestBody List<FJ> fjarray) {
        Respon respon = startRespon();
        return  respon.ok(fjService.delete(fjarray));
    }

    @PostMapping("/upload")
    @ResponseBody
    public Respon upload(final HttpServletRequest request) {
        Respon respon = startRespon();
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        /*MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        if(file != null){
            files.add(file);
        }*/
        Map<String, String>  params  =getAllRequestParam(request);
        if(files.size() == 0 || params.size() == 0 || !params.containsKey("fj")){
            return respon.responError("没有参数");
        }
        String fj = params.get("fj");
        List<FJ> fjArray ;
        if(fj.startsWith("[")){
            fjArray = JSON.parseArray(fj,FJ.class);
            if(files.size() != fjArray.size()){
                return  respon.responError("文件和数量对不上");
            }

        }else{
            FJ fjPo = JSON.parseObject(fj,FJ.class);
            fjArray = new ArrayList<>();
            fjArray.add(fjPo);
        }
        for (int i = 0; i < fjArray.size(); i++) {
            fjArray.get(i).setFile(files.get(i));
        }
        fjService.setAttributeAndSave(fjArray);
        for (FJ tem:fjArray
             ) {
            tem.setFile(null);
        }
        //return  respon.ok(JSONObject.parseArray(JSONObject.toJSONString(fjArray)));
        return   respon.ok(fjArray);
    }

    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new LinkedHashMap<>();
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


}
