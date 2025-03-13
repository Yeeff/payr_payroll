package com.maxiaseo.payrll.adapters.driven.feign.mapper;

import com.maxiaseo.payrll.adapters.driven.feign.dto.FileResponseDto;
import com.maxiaseo.payrll.domain.model.ScheduleEmployeesFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IFileMapper {

    ScheduleEmployeesFile toModel(FileResponseDto fileResponseDto);
}
