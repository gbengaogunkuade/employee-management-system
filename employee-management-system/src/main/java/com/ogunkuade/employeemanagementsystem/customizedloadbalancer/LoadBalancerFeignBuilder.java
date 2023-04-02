package com.ogunkuade.employeemanagementsystem.customizedloadbalancer;


import feign.Feign;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoadBalancerFeignBuilder {

    @Bean
    @LoadBalanced
    public Feign.Builder getFeignBuilder(){
        return Feign.builder();
    }


}




