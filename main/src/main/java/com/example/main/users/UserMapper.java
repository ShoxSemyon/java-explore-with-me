package com.example.main.users;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto convertToUserDto(User user);

    UserShortDto convertToUserShortDto(User user);

    User convertToUser(UserDto userDto);
}
