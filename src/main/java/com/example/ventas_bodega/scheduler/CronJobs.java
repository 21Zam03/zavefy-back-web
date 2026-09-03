package com.example.ventas_bodega.scheduler;

import com.example.ventas_bodega.service.NotificationService;
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
    private final NotificationService notificationService;

    @Autowired
    public CronJobs(OpportunityService opportunityService, NotificationService notificationService) {
        this.opportunityService = opportunityService;
        this.notificationService = notificationService;
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

    @Scheduled(fixedDelay = 3600000)
    public void NotificationsProcess() {
        log.info("INICIANDO REVISIÓN DE NOTIFICACIONES");
        runCheck("RECEIVABLE_DUE_SOON", notificationService::checkReceivablesDueSoon);
        runCheck("RECEIVABLE_OVERDUE", notificationService::checkReceivablesOverdue);
        runCheck("STOCK_LOW", notificationService::checkLowStock);
        runCheck("STOCK_OUT", notificationService::checkOutOfStock);
        runCheck("CAJA_OPEN_TOO_LONG", notificationService::checkOpenCajaTooLong);
        log.info("REVISIÓN DE NOTIFICACIONES FINALIZADA");
    }

    // Cada tipo corre aislado: si uno falla, no debe tumbar la revisión de los demás.
    private void runCheck(String type, Runnable check) {
        try {
            check.run();
        } catch (Exception e) {
            log.error("ERROR REVISANDO NOTIFICACIONES DE TIPO " + type, e);
        }
    }

}
