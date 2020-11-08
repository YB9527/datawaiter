package cn.yb.datawaiter.service;

import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.model.Level;
import cn.yb.datawaiter.service.impl.ILevelService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class LevelService  implements ILevelService {


    @Override
    public JSONObject findDataById( String id) {

        return Select.findDataById(SystemConnect.getConn(),Level.class, id);

    }

    @Override
    public void findParent(JSONObject json) {
        if(json != null){
            String jsonId = json.getString("parentId");
            if(jsonId != null){
                JSONObject parent = findDataById(jsonId);
                if(parent != null){
                    json.put("parent",parent) ;
                    findParent(parent);
                }else{
                    return ;
                }
            }
        }
    }
}
