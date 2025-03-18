package com.maxiaseo.payrll.adapters.driven.feign.client;

import com.maxiaseo.payrll.adapters.driven.feign.dto.FileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "file-service", url = "${file-services.url}")
@Component
public interface IFileClientServices {

    @GetMapping("/content/{fileName}")
    FileResponseDto getContentFile(@PathVariable String fileName);

}
