package com.maxiaseo.payrll.adapters.driven.feign.adapter;

import com.maxiaseo.payrll.adapters.driven.feign.client.IFileClientServices;
import com.maxiaseo.payrll.adapters.driven.feign.exception.FileServiceException;
import com.maxiaseo.payrll.adapters.driven.feign.exception.ScheduleFileNotFoundException;
import com.maxiaseo.payrll.adapters.driven.feign.mapper.IFileMapper;
import com.maxiaseo.payrll.domain.model.ScheduleEmployeesFile;
import com.maxiaseo.payrll.domain.spi.IFileServicePort;
import feign.FeignException;

public class FileClientServices implements IFileServicePort {

    private IFileClientServices fileServicesClient;
    private IFileMapper fileMapper;

    public FileClientServices(IFileClientServices fileServicesClient, IFileMapper iFileMapper) {
        this.fileServicesClient = fileServicesClient;
        this.fileMapper = iFileMapper;
    }

    @Override
    public ScheduleEmployeesFile getFileWithContent(String fileName)  {
        try {
            return fileMapper.toModel(fileServicesClient.getContentFile(fileName));
        } catch (FeignException.NotFound e) {
            throw new ScheduleFileNotFoundException( fileName);
        } catch (FeignException e) {
            throw new FileServiceException( e.getMessage());
        }

    }
}
