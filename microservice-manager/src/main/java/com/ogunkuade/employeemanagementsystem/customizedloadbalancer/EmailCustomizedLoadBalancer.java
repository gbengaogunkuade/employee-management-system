package com.ogunkuade.employeemanagementsystem.customizedloadbalancer;


import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;

@LoadBalancerClient(name="EMAIL-SERVICE", configuration= LoadBalancerConfiguration.class)
public class EmailCustomizedLoadBalancer {
    //
}




