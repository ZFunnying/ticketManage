package com.cmit.kapok;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages={"com.cmit.kapok","com.cmit.codegenerator"})
@MapperScan({"com.baomidou.mybatisplus.samples.quickstart.mapper","com.cmit.kapok.system.service.**.mapper","com.cmit.kapok.base.service.**.mapper"})
@ServletComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
