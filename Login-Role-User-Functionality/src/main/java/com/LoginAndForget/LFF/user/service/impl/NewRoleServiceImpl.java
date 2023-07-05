package com.LoginAndForget.LFF.user.service.impl;

import com.LoginAndForget.LFF.user.role.Roles;
import com.LoginAndForget.LFF.user.role.repo.RoleRepository;
import com.LoginAndForget.LFF.user.service.NewRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewRoleServiceImpl implements NewRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Roles findRoleByName(String name) {
        return roleRepository.findByName(name);
    }
    @Override
    public Optional<Roles> findRoleById(Long id) {
        return roleRepository.findById(id);
    }
}