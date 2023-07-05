package com.LoginAndForget.LFF.user.role.dto;

import com.LoginAndForget.LFF.user.role.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long id;
    private String name;

    public static RoleDto fromEntity(Roles role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        return roleDto;
    }
}
