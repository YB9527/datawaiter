package cn.yb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
@MapperScan("cn.yb.datawaiter.mapper")
public class DatawaiterApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DatawaiterApplication.class, args);
    }
    /*@Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }*/
}
