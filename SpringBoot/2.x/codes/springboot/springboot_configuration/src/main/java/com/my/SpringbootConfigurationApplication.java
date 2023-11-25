package com.my;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(ServerConfig.class)
public class SpringbootConfigurationApplication {

    @Bean
    @ConfigurationProperties(prefix = "datasource")
    public DruidDataSource datasource(){
        DruidDataSource dataSource = new DruidDataSource();
        //dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootConfigurationApplication.class, args);
        ServerConfig serverConfig = context.getBean(ServerConfig.class);
        System.out.println(serverConfig);

        DruidDataSource dataSource = context.getBean(DruidDataSource.class);
        System.out.println(dataSource.getDriverClassName());
    }

}
