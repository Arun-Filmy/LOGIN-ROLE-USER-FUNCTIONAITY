package com.LoginAndForget.LFF.user.service;

import com.LoginAndForget.LFF.user.role.Roles;

import java.util.Optional;

public interface NewRoleService {
    public Roles findRoleByName(String name);
    public Optional<Roles> findRoleById(Long id);
}
