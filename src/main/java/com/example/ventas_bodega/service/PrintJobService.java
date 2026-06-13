package com.example.ventas_bodega.service;

import com.example.ventas_bodega.response.PrintJobResponse;

import java.util.List;

public interface PrintJobService {

    public List<PrintJobResponse> getPendingJobs(Long agentId);

    void markProcessing(Long id);

    void markCompleted(Long id);

    void markError(Long id, String message);
}
