package com.smartchat.users.mapper;

import com.smartchat.users.model.User;
import com.smartchat.users.persistance.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UsersToUserEntityMapper {
    public UserEntity map(User user);
}
