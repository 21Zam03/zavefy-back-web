package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.entity.PrintJobEntity;
import com.example.ventas_bodega.enums.PrintJobStatus;
import com.example.ventas_bodega.repository.PrintJobRepository;
import com.example.ventas_bodega.response.PrintJobResponse;
import com.example.ventas_bodega.service.PrintJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrintJobServiceImpl implements PrintJobService {

    private final PrintJobRepository printJobRepository;

    @Autowired
    public PrintJobServiceImpl(PrintJobRepository printJobRepository) {
        this.printJobRepository = printJobRepository;
    }

    @Override
    public List<PrintJobResponse> getPendingJobs(Long agentId) {

        return printJobRepository
                .findByAgentIdAndStatus(
                        agentId,
                        PrintJobStatus.PENDING
                )
                .stream()
                .map(job -> new PrintJobResponse(
                        job.getId(),
                        job.getContent()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void markProcessing(Long id) {
        PrintJobEntity job = printJobRepository.findById(id)
                .orElseThrow();
        job.setStatus(PrintJobStatus.PROCESSING);
    }

    @Override
    @Transactional
    public void markCompleted(Long id) {
        PrintJobEntity job = printJobRepository.findById(id)
                .orElseThrow();

        job.setStatus(PrintJobStatus.PRINTED);
        job.setProcessedAt(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void markError(Long id, String message) {
        PrintJobEntity job = printJobRepository.findById(id)
                .orElseThrow();

        job.setStatus(PrintJobStatus.ERROR);
        job.setErrorMessage(message);
        job.setProcessedAt(LocalDateTime.now());
    }

}
