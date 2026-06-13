package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.response.PrintJobResponse;
import com.example.ventas_bodega.service.PrintJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/print-jobs")
public class PrintJobController {

    private final PrintJobService printJobService;

    @Autowired
    public PrintJobController(PrintJobService printJobService) {
        this.printJobService = printJobService;
    }

    @GetMapping("/pending")
    public List<PrintJobResponse> getPendingJobs(
            @RequestParam Long agentId) {

        return printJobService.getPendingJobs(agentId);
    }

    @PostMapping("/{id}/processing")
    public ResponseEntity<Void> markProcessing(@PathVariable Long id) {
        printJobService.markProcessing(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/completed")
    public ResponseEntity<Void> markCompleted(@PathVariable Long id) {
        printJobService.markCompleted(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/error")
    public ResponseEntity<Void> markError(
            @PathVariable Long id,
            @RequestBody(required = false) String message) {

        printJobService.markError(id, message);
        return ResponseEntity.ok().build();
    }
}
