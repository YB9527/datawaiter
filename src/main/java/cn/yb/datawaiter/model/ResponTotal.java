package cn.yb.datawaiter.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResponTotal extends Respon {


    public ResponTotal(Date date) {
        super(date);
    }


    public Respon ok(int total, List<JSONObject> obj) {
        super.ok(obj);
        JSONObject jsonObject = new JSONObject();
        JSONObject j = new JSONObject();
        j.put("total",total);
        j.put("rows",obj);
        jsonObject.put("data",j);
        this.setData(j);
        return this;
    }
}
