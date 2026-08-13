package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.entity.AgentEntity;
import com.example.ventas_bodega.entity.PrintJobEntity;
import com.example.ventas_bodega.enums.AgentStatus;
import com.example.ventas_bodega.enums.PrintJobStatus;
import com.example.ventas_bodega.repository.AgentRepository;
import com.example.ventas_bodega.repository.PrintJobRepository;
import com.example.ventas_bodega.service.AgentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AgentServiceImpl implements AgentService {

    private final PrintJobRepository printJobRepository;
    private final AgentRepository agentRepository;

    public AgentServiceImpl(PrintJobRepository printJobRepository,  AgentRepository agentRepository) {
        this.printJobRepository = printJobRepository;
        this.agentRepository = agentRepository;
    }

    public void createPrintJob(Long companyId, Long defaultAgentId, String ticket) {

        /*
        AgentEntity agent = agentRepository.findFirstByCompanyIdAndStatus(
                        companyId,
                        AgentStatus.ONLINE
                ).orElseThrow();
        * */

        PrintJobEntity job = new PrintJobEntity();

        job.setCompanyId(companyId);
        job.setAgentId(defaultAgentId);
        job.setContent(ticket);
        job.setStatus(PrintJobStatus.PENDING);

        printJobRepository.save(job);
    }

    @Override
    public void updateLastConnected(Long agentId) {
        int result = agentRepository.updateLastSeen(agentId);

        if (result == 0) {
            throw new RuntimeException(
                    "No se encontró el agente con ID: " + agentId
            );
        }
    }

    @Override
    public boolean isAgentConnected(Long agentId) {
        LocalDateTime lastSeen =
                agentRepository.findLastSeenById(agentId);

        if (lastSeen == null) {
            return false;
        }

        return lastSeen.isAfter(
                LocalDateTime.now().minusSeconds(60)
        );
    }


}
