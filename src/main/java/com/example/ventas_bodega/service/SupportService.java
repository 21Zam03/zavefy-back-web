package com.example.ventas_bodega.service;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.CreateSupportMessageRequest;
import com.example.ventas_bodega.response.MessageResponse;

public interface SupportService {

    MessageResponse createMessage(CreateSupportMessageRequest request, UserEntity user);

}
