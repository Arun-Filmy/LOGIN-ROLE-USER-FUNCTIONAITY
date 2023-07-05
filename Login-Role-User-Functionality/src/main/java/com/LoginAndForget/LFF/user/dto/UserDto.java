package com.LoginAndForget.LFF.user.dto;
import com.LoginAndForget.LFF.user.role.Roles;
import com.LoginAndForget.LFF.user.role.dto.RoleDto;
import com.LoginAndForget.LFF.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private Set<RoleDto> roles;

    public static UserDto fromEntity(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getEmail());
        userDto.setPassword(user.getPassword());

        Set<RoleDto> roleDtos = user.getRoles().stream()
                .map((Roles role) -> RoleDto.fromEntity(role))
                .collect(Collectors.toSet());
        userDto.setRoles(roleDtos);

        return userDto;
    }
}
