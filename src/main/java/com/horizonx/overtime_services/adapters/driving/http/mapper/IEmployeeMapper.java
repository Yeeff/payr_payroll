package com.horizonx.overtime_services.adapters.driving.http.mapper;

import com.horizonx.overtime_services.adapters.driving.http.dto.EmployeeResponseDto;
import com.horizonx.overtime_services.domain.model.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IEmployeeMapper {


    EmployeeResponseDto toEmployeeResponseDto(Employee employee);

    List<EmployeeResponseDto> toEmployeeResponseDtoList(List<Employee> employees);
}
