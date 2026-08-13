package com.example.ventas_bodega.service;

public interface AgentService {

    void createPrintJob(Long company, Long defaultAgentId, String ticket);
    void updateLastConnected(Long agentId);
    boolean isAgentConnected(Long agentId);

}
