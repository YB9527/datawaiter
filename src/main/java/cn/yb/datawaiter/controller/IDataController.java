package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.controller.query.QueryBase;
import cn.yb.datawaiter.model.Respon;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IDataController {
    @PostMapping(value = "/findTotal")
    public Respon findTotal(@RequestBody QueryBase data);

    @RequestMapping(value = "/findDataById")
    public Respon findDataById(String id);

    @PostMapping(value = "/findPageData")
    public Respon findPageData(@RequestBody QueryBase data);


    @PostMapping(value = "/findDataAll")
    public Respon findDataAll(String data);

    @PostMapping(value = "/updateData")
    public Respon updateData(String data);

    @PostMapping(value = "/saveData")
    public Respon saveData(String data);

    @PostMapping(value = "/deleteData")
    public Respon deleteData(Object data);
}
