package com.maxiaseo.payrll.configuration;

import com.maxiaseo.payrll.adapters.driven.feign.adapter.FileClientServices;
import com.maxiaseo.payrll.adapters.driven.feign.client.IFileClientServices;
import com.maxiaseo.payrll.adapters.driven.feign.mapper.IFileMapper;
import com.maxiaseo.payrll.domain.api.IPayrollServicesPort;
import com.maxiaseo.payrll.domain.api.usecase.PayrollServices;
import com.maxiaseo.payrll.domain.service.file.FileDataProcessor;
import com.maxiaseo.payrll.domain.spi.IFileServicePort;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class BeanConfiguration {

    private IFileClientServices fileServicesClient;
    private IFileMapper fileMapper;

    @Bean
    public FileDataProcessor getFileDataProcessor(){
        return new FileDataProcessor();
    }

    @Bean
    public IFileServicePort getFileServicesPort(){
        return new FileClientServices(fileServicesClient, fileMapper);
    }

    @Bean
    public IPayrollServicesPort getPayrollServicePort(){
        return new PayrollServices(getFileDataProcessor(), getFileServicesPort());
    }


}
