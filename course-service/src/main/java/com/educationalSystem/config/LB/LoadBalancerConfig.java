package com.educationalSystem.config.LB;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClients(defaultConfiguration = RandomBalancerConfig.class)
public class LoadBalancerConfig {
}