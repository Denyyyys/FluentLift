package com.denyyyys.fluentLift.model.postgres.mapper;

import com.denyyyys.fluentLift.model.postgres.dto.request.UserRegistrationRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckCreatorDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;

public class AppUserMapper {
    public static AppUser toEntity(UserRegistrationRequestDto dto) {
        AppUser user = new AppUser();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRoles("ROLE_USER");
        return user;
    }

    public static DeckCreatorDto toDeckCreatorDto(AppUser user) {
        DeckCreatorDto dto = new DeckCreatorDto();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }
}
