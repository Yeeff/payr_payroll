package com.horizonx.overtime_services.adapters.driven.feign.mapper;

import com.horizonx.overtime_services.adapters.driven.feign.dto.FileResponseDto;
import com.horizonx.overtime_services.domain.model.ScheduleEmployeesFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IFileMapper {

    ScheduleEmployeesFile toModel(FileResponseDto fileResponseDto);
}
