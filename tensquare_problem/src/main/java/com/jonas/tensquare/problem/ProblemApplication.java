package com.jonas.tensquare.problem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import util.IdWorker;

@SpringBootApplication
@CrossOrigin
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class ProblemApplication {

    public static void main(String[] args){
        SpringApplication.run(ProblemApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
