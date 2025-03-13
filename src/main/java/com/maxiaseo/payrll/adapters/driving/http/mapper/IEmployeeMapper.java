package com.maxiaseo.payrll.adapters.driving.http.mapper;

import com.maxiaseo.payrll.adapters.driving.http.dto.EmployeeResponseDto;
import com.maxiaseo.payrll.domain.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IEmployeeMapper {

    IEmployeeMapper INSTANCE = Mappers.getMapper(IEmployeeMapper.class);

    @Mapping(target = "totalOvertimeSurchargeHoursNightHoliday", expression = "java(employee.getTotalOvertimeSurchargeHoursNightHoliday())")
    @Mapping(target = "totalOvertimeSurchargeHoursHoliday", expression = "java(employee.getTotalOvertimeSurchargeHoursHoliday())")
    @Mapping(target = "totalSurchargeHoursHoliday", expression = "java(employee.getTotalSurchargeHoursHoliday())")
    @Mapping(target = "totalOvertimeHoursDay", expression = "java(employee.getTotalOvertimeHoursDay())")
    @Mapping(target = "totalOvertimeHoursNightHoliday", expression = "java(employee.getTotalOvertimeHoursNightHoliday())")
    @Mapping(target = "totalSurchargeHoursNightHoliday", expression = "java(employee.getTotalSurchargeHoursNightHoliday())")
    @Mapping(target = "totalSurchargeHoursNight", expression = "java(employee.getTotalSurchargeHoursNight())")
    @Mapping(target = "totalOvertimeHoursHoliday", expression = "java(employee.getTotalOvertimeHoursHoliday())")
    @Mapping(target = "totalOvertimeHoursNight", expression = "java(employee.getTotalOvertimeHoursNight())")
    EmployeeResponseDto toEmployeeResponseDto(Employee employee);

    List<EmployeeResponseDto> toEmployeeResponseDtoList(List<Employee> employees);
}
