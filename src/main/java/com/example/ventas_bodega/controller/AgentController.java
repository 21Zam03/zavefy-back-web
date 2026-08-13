package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;

    @Autowired
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/{agentId}/heartbeat")
    public ResponseEntity<Void> heartbeat(
            @PathVariable Long agentId) {

        agentService.updateLastConnected(agentId);

        return ResponseEntity.ok().build();
    }

}
