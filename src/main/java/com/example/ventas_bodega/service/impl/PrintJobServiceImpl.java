package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.entity.PrintJobEntity;
import com.example.ventas_bodega.enums.PrintJobStatus;
import com.example.ventas_bodega.repository.AgentRepository;
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
    private final AgentRepository agentRepository;

    @Autowired
    public PrintJobServiceImpl(PrintJobRepository printJobRepository, AgentRepository agentRepository) {
        this.printJobRepository = printJobRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public List<PrintJobResponse> getPendingJobs(Long agentId) {

        // Todos los jobs de este poll son del mismo agente, así que el nombre de
        // impresora configurado en tb_agente se resuelve una sola vez.
        String printerName = agentRepository.findById(agentId)
                .map(agent -> agent.getDefaultPrinter())
                .orElse(null);

        return printJobRepository
                .findByAgentIdAndStatus(
                        agentId,
                        PrintJobStatus.PENDING
                )
                .stream()
                .map(job -> {
                    PrintJobResponse response = new PrintJobResponse(job.getId(), job.getContent());
                    response.setPrinterName(printerName);
                    return response;
                })
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
