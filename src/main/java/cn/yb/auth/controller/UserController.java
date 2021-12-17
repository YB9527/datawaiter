package cn.yb.auth.controller;

import cn.yb.auth.model.User;
import cn.yb.auth.service.impl.IUserService;
import cn.yb.datawaiter.controller.BasicController;
import cn.yb.datawaiter.model.entity.Respon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping(value = "/user")
public class UserController extends BasicController {
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/regist")
    public Respon regist(User user){
        Respon respon = startRespon();
        //检查用户
        //保存用户
        return respon;
    }
    @RequestMapping(value = "/login")
    public Respon login(User user){
        Respon respon = startRespon();
        //账号登录
        boolean accountbl =  userService.accountLogin(user.getAccount(),user.getPassword());
        //手机号登录
        boolean  phonebl=  userService.phoneLogin(user.getPhone(),user.getPassword());
        return respon;
    }

    @GetMapping("/hello")
    public String hello() {

        return "hello";
    }
}
