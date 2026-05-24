package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.ClientDto;
import com.example.ventas_bodega.dto.interfaces.ClientDtoInter;

public class ClientMapper {

    public static ClientDto mapIntefaceToDto(ClientDtoInter clientDtoInter) {
        if (clientDtoInter != null) {
            ClientDto clientDto = new ClientDto();
            clientDto.setClientId(clientDtoInter.getClientId());
            clientDto.setFirstname(clientDtoInter.getFirstname());
            clientDto.setLastname(clientDtoInter.getLastname());
            clientDto.setEmail(clientDtoInter.getEmail());
            clientDto.setEnabled(clientDtoInter.getEnabled());
            clientDto.setDocumentNumber(clientDtoInter.getDocumentNumber());
            clientDto.setDocumentType(clientDtoInter.getDocumentType());
            clientDto.setPhoneNumber(clientDtoInter.getPhoneNumber());
            clientDto.setAddress(clientDtoInter.getAddress());
            clientDto.setCreatedDate(clientDtoInter.getCreatedDate());
            clientDto.setUpdatedDate(clientDtoInter.getUpdatedDate());
            return clientDto;
        } return null;
    }

}
