package com.example.ventas_bodega.scheduler;

import com.example.ventas_bodega.service.OpportunityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CronJobs {

    private static final Logger log = LoggerFactory.getLogger(CronJobs.class);

    private final OpportunityService opportunityService;

    @Autowired
    public CronJobs(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @Scheduled(fixedDelay = 10000)
    public void OpportunityProcess() {
        try {
            log.info("INICIANDO PROCESAMIENTO DE OPORTUNIDADES");
            opportunityService.createOpportunity();
            log.info("PROCESAMIENTO FINALIZADO");
        } catch (Exception e) {
            log.error("ERROR PROCESANDO OPORTUNIDADES");
        }
    }


}
