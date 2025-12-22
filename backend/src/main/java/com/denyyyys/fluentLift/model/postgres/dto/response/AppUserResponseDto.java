package com.denyyyys.fluentLift.model.postgres.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResponseDto {
    private String name;
    private String email;
    private Long id;
}
