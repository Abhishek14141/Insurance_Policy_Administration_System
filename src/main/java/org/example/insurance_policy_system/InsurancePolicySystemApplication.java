package org.example.insurance_policy_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InsurancePolicySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsurancePolicySystemApplication.class, args);
    }

}
