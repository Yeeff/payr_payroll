package com.maxiaseo.payrll.adapters.driving.http.mapper;

import com.maxiaseo.payrll.adapters.driving.http.dto.EmployeeResponseDto;
import com.maxiaseo.payrll.domain.model.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IEmployeeMapper {


    EmployeeResponseDto toEmployeeResponseDto(Employee employee);

    List<EmployeeResponseDto> toEmployeeResponseDtoList(List<Employee> employees);
}
