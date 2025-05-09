package com.horizonx.overtime_services.adapters.driven.feign.adapter;

import com.horizonx.overtime_services.adapters.driven.feign.client.IFileClientServices;
import com.horizonx.overtime_services.adapters.driven.feign.exception.FileServiceException;
import com.horizonx.overtime_services.adapters.driven.feign.exception.ScheduleFileNotFoundException;
import com.horizonx.overtime_services.adapters.driven.feign.mapper.IFileMapper;
import com.horizonx.overtime_services.domain.model.ScheduleEmployeesFile;
import com.horizonx.overtime_services.domain.spi.IFileServicePort;
import feign.FeignException;

public class FileClientServices implements IFileServicePort {

    private IFileClientServices fileServicesClient;
    private IFileMapper fileMapper;

    public FileClientServices(IFileClientServices fileServicesClient, IFileMapper iFileMapper) {
        this.fileServicesClient = fileServicesClient;
        this.fileMapper = iFileMapper;
    }

    @Override
    public ScheduleEmployeesFile getFileWithContent(Integer formId)  {
        try {
            return fileMapper.toModel(fileServicesClient.getContentFile(formId));
        } catch (FeignException.NotFound e) {
            throw new ScheduleFileNotFoundException( formId.toString());
        } catch (FeignException e) {
            throw new FileServiceException( e.getMessage());
        }

    }
}
