package com.horizonx.overtime_services.configuration;

import com.horizonx.overtime_services.adapters.driven.feign.adapter.FileClientServices;
import com.horizonx.overtime_services.adapters.driven.feign.client.IFileClientServices;
import com.horizonx.overtime_services.adapters.driven.feign.mapper.IFileMapper;
import com.horizonx.overtime_services.adapters.driven.jpa.mysql.adapter.PayrollPersistentAdapter;
import com.horizonx.overtime_services.domain.api.IPayrollServicesPort;
import com.horizonx.overtime_services.domain.api.usecase.PayrollServices;
import com.horizonx.overtime_services.domain.service.file.FileDataProcessor;
import com.horizonx.overtime_services.domain.spi.IFileServicePort;
import com.horizonx.overtime_services.domain.spi.IPayrollPersistentPort;
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
        return new FileDataProcessor(getPayrollPersistentAdapter());
    }

    @Bean
    public IFileServicePort getFileServicesPort(){
        return new FileClientServices(fileServicesClient, fileMapper);
    }

    @Bean
    public IPayrollServicesPort getPayrollServicePort(){
        return new PayrollServices(getFileDataProcessor(), getFileServicesPort());
    }

    @Bean
    public IPayrollPersistentPort getPayrollPersistentAdapter(){
        return new PayrollPersistentAdapter();
    }


}
