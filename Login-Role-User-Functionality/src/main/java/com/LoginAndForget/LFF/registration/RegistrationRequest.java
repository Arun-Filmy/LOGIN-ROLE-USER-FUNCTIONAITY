package com.LoginAndForget.LFF.registration;

import com.LoginAndForget.LFF.user.role.Roles;
import org.hibernate.annotations.NaturalId;

import java.util.Set;

public record RegistrationRequest(
        String partnerId,
        String email,
        String password,
        String role) {
}
