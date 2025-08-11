package com.denyyyys.langLearner.model.postgres.mapper;

import com.denyyyys.langLearner.model.postgres.dto.request.UserRegistrationRequestDto;
import com.denyyyys.langLearner.model.postgres.entity.AppUser;

public class AppUserMapper {
    public static AppUser toEntity(UserRegistrationRequestDto dto) {
        AppUser user = new AppUser();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRoles("ROLE_USER");
        return user;
    }
}
