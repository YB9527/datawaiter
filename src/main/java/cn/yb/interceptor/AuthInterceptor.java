package cn.yb.interceptor;

import cn.yb.datawaiter.tools.Tool;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AuthInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String token = request.getHeader("token");
        return  true;
       /* if (Tool.isEmpty(token)) {
            response.getWriter().print("用户未登录，请登录后操作！");
            return false;
        }
        Object loginStatus =null;
        if( Objects.isNull(loginStatus)){
            response.getWriter().print("token错误，请查看！");
            return false;
        }
        return true;*/
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
