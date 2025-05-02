package com.bcb.bcb.configuration.beans;

import com.bcb.bcb.configuration.security.AuthSecutiryFilter;
import com.bcb.bcb.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class BeanConfiguration {

    @Bean
    public AuthSecutiryFilter authSecutiryFilter(@Lazy ClientRepository clientRepository) {
        return new AuthSecutiryFilter(clientRepository);
    }
}
