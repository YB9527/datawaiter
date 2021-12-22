package cn.yb;

import cn.yb.datawaiter.tools.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * 跨域配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
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
