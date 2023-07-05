package com.LoginAndForget.LFF.user.service.impl;


import com.LoginAndForget.LFF.user.role.Roles;
import com.LoginAndForget.LFF.user.User;
import com.LoginAndForget.LFF.user.UserRepository;
import com.LoginAndForget.LFF.user.dto.UserDto;
import com.LoginAndForget.LFF.user.service.NewRoleService;
import com.LoginAndForget.LFF.user.service.NewUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewUserServiceImpl implements NewUserService {

    private final UserRepository userRepository;
    private final NewRoleService roleService;
    private final PasswordEncoder encoder;

    public NewUserServiceImpl(UserRepository userRepository, NewRoleService roleService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new NoSuchElementException("User not found."));
        System.out.println("--------------------------------------------------------------");
        System.out.println(user);
        System.out.println("--------------------------------------------------------------");
        return user;
    }
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());

    }
    @Override
    public Optional<Roles> getRoleByIdWithUserDetails(Long id){
        Optional<Roles> roleById = roleService.findRoleById(id);
        return roleById;
    }
}

