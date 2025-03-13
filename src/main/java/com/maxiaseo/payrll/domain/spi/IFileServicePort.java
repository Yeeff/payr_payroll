package com.maxiaseo.payrll.domain.spi;

import com.maxiaseo.payrll.adapters.driven.feign.exception.FileServiceException;
import com.maxiaseo.payrll.adapters.driven.feign.exception.ScheduleFileNotFoundException;
import com.maxiaseo.payrll.domain.model.ScheduleEmployeesFile;

public interface IFileServicePort {

    ScheduleEmployeesFile getFileWithContent(String fileName)  throws ScheduleFileNotFoundException, FileServiceException;
}
