package cn.yb;

import cn.yb.datawaiter.tools.Tool;
import cn.yb.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").
                        allowedOrigins("*").    //allowedOrigins("https://www.dustyblog.cn"). //允许跨域的域名，可以用*表示允许任何域名使用
                        allowedMethods("*"). //允许任何方法（post、get等）
                        allowedHeaders("*"). //允许任何请求头
                        allowCredentials(true). //带上cookie信息
                        exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(1L); //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
            }
        };
    }
    @Bean
    public AuthInterceptor initAuthInterceptor(){
        return new AuthInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initAuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/sys/**")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/image/**");
    }


    private String uploadDir;

    @Value("#{${uploadconfig.dir}}")
    public void dirMap(Map<String,String> dirMap){
        String ip = Tool.getInterIP();
        String key ;
        if(ip.startsWith("172.18.254.48") ){
            key = "out";
        }else{
            key = "in";
        }
        this.uploadDir = dirMap.get(key);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /imgpriew/**为前端URL访问路径 后面 file:xxxx为本地磁盘映射
        System.out.println("ip:"+Tool.getInterIP());
        System.out.println("uploadDir:"+uploadDir);
        registry.addResourceHandler("/imgpriew/**").addResourceLocations("file:" + uploadDir);
    }
}
