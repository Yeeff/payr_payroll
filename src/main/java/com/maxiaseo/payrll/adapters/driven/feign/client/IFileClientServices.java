package com.maxiaseo.payrll.adapters.driven.feign.client;

import com.maxiaseo.payrll.adapters.driven.feign.dto.FileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "file-service", url = "https://maxi-api-file-1034515474137.southamerica-east1.run.app/api/file")
//@FeignClient(name = "file-service", url = "http://localhost:8090/api/file")
public interface IFileClientServices {

    @GetMapping("/content/{fileName}")
    FileResponseDto getContentFile(@PathVariable String fileName);

}
