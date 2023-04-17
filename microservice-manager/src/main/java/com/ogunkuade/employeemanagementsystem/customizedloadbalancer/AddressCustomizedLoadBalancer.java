package com.ogunkuade.employeemanagementsystem.customizedloadbalancer;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;

@LoadBalancerClient(name="ADDRESS-SERVICE", configuration= LoadBalancerConfiguration.class)
public class AddressCustomizedLoadBalancer {
    //
}




