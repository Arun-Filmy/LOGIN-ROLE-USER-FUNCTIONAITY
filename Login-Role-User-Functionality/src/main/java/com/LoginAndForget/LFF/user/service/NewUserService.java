package com.LoginAndForget.LFF.user.service;

import com.LoginAndForget.LFF.user.role.Roles;
import com.LoginAndForget.LFF.user.User;
import com.LoginAndForget.LFF.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface NewUserService {
    public User getUserById(Long id);
    public List<UserDto> getAllUsers();
    public Optional<Roles> getRoleByIdWithUserDetails(Long id);
}
