package cn.yb.datawaiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//排除自动配置
public class DatawaiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatawaiterApplication.class, args);
    }

}
