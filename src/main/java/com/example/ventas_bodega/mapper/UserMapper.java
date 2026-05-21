package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.UserDto;
import com.example.ventas_bodega.entity.UserEntity;

public class UserMapper {

    public static UserDto buildCompanyDtoFromController(
            String username,
            String password,
            String firstName,
            String lastName,
            String userEmail,
            String userPhoneNumber
    ) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmail(userEmail);
        userDto.setPhoneNumber(userPhoneNumber);
        return userDto;
    }

    public static UserEntity dtoToEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname(userDto.getFirstName());
        userEntity.setLastname(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        return userEntity;
    }

}
