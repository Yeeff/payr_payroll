package com.horizonx.overtime_services.domain.spi;

import com.horizonx.overtime_services.adapters.driven.feign.exception.FileServiceException;
import com.horizonx.overtime_services.adapters.driven.feign.exception.ScheduleFileNotFoundException;
import com.horizonx.overtime_services.domain.model.ScheduleEmployeesFile;

public interface IFileServicePort {

    ScheduleEmployeesFile getFileWithContent(Integer formId)  throws ScheduleFileNotFoundException, FileServiceException;
}
