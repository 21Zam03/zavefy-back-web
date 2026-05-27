package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.ClientDto;
import com.example.ventas_bodega.dto.interfaces.ClientDtoInter;
import com.example.ventas_bodega.entity.ClientEntity;

import java.util.ArrayList;
import java.util.List;

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

    public static ClientDto entityToDto(ClientEntity clientEntity) {
        ClientDto clientDto = new ClientDto();
        clientDto.setClientId(clientEntity.getClientId());
        clientDto.setEmail(clientEntity.getEmail());
        clientDto.setEnabled(clientEntity.isEnabled());
        clientDto.setDocumentNumber(clientEntity.getDocumentNumber());
        clientDto.setDocumentType(clientEntity.getClientDocumentType());
        clientDto.setPhoneNumber(clientEntity.getPhoneNumber());
        clientDto.setAddress(clientEntity.getAddress());
        clientDto.setCreatedDate(clientEntity.getCreatedDate());
        clientDto.setUpdatedDate(clientEntity.getUpdatedDate());
        clientDto.setFullName(clientEntity.getFullName());
        return clientDto;
    }

    public static List<ClientDto> entityListToDtoList(List<ClientEntity> clientEntityList) {
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (ClientEntity clientEntity : clientEntityList) {
            ClientDto clientDto = entityToDto(clientEntity);
            clientDtoList.add(clientDto);
        }
        return clientDtoList;
    }

    public static ClientEntity dtoToEntity(ClientDto clientDto) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientId(clientDto.getClientId());
        clientEntity.setFullName(clientDto.getFullName());
        clientEntity.setEmail(clientDto.getEmail());
        clientEntity.setEnabled(clientDto.isEnabled());
        clientEntity.setDocumentNumber(clientDto.getDocumentNumber());
        clientEntity.setClientDocumentType(clientDto.getDocumentType());
        clientEntity.setPhoneNumber(clientDto.getPhoneNumber());
        clientEntity.setAddress(clientDto.getAddress());
        clientEntity.setCreatedDate(clientDto.getCreatedDate());
        clientEntity.setUpdatedDate(clientDto.getUpdatedDate());
        return clientEntity;
    }

}
