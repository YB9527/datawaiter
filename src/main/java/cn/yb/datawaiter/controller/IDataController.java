package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.model.entity.Respon;
import cn.yb.datawaiter.model.query.QueryBase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IDataController<Id,Entity,T extends QueryBase> {
    @PostMapping(value = "/findTotal")
    public Respon findTotal(@RequestBody T data);

    @RequestMapping(value = "/findDataById")
    public Respon findDataById(String id);

    @PostMapping(value = "/findDataPage")
    public  Respon findDataPage(@RequestBody T data);


    @PostMapping(value = "/findDataAll")
    public  Respon findDataAll(@RequestBody T data);

    @PostMapping(value = "/updateData")
    public Respon updateData(Entity data);

    @PostMapping(value = "/saveData")
    public Respon saveData(Entity data);

    @PostMapping(value = "/deleteData")
    public Respon deleteData(Entity data);
}
