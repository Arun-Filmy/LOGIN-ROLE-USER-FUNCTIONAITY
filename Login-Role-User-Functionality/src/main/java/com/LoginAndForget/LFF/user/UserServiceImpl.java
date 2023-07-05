package com.LoginAndForget.LFF.user;

import com.LoginAndForget.LFF.exception.UserAlreadyExistsException;
import com.LoginAndForget.LFF.registration.RegistrationRequest;
import com.LoginAndForget.LFF.registration.password.PasswordResetTokenService;
import com.LoginAndForget.LFF.registration.token.VerificationToken;
import com.LoginAndForget.LFF.registration.token.VerificationTokenRepository;
import com.LoginAndForget.LFF.user.role.Roles;
import com.LoginAndForget.LFF.user.service.NewRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final NewRoleService roleService;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = this.findByEmail(request.email());
        if(user.isPresent()){
            throw new UserAlreadyExistsException("User is already with mail "+request.email());
        }
        var newUser = new User();
        newUser.setPartnerId(request.partnerId());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        String roles = request.role();

        for (String roleName : roles.split(",")) {
            Roles retrievedRole = roleService.findRoleByName(roleName.trim());
            if (retrievedRole != null) {
                newUser.getRoles().add(retrievedRole);
            } else if(retrievedRole == null){
                try {
                    throw new RoleNotFoundException("The role '" + roleName + "' is not found");
                } catch (RoleNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }else{
                break;
            }
        }
        return userRepository.save(newUser);
    }
    @Override
    public boolean isPasswordStrong(String password) {
        if (!isStrongPassword(password)) {
            return false;
        }
        return true;
    }
    private boolean isStrongPassword(String password) {
        // Check if the password meets the requirements for a strong password
        // Minimum 8 characters, alphanumeric characters, special characters, and numbers.
        String pattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }
    @Override
    public String validateToken(String theToken) {
        VerificationToken byToken = tokenRepository.findByToken(theToken);
        if(byToken == null){
            return "Invalid Verification Message";
        }

        User user = byToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((byToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            return "Token is already expire";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "Valid";
    }
    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
    }
    @Override
    public String validatePasswordResetToken(String passwordResetToken) {
        return passwordResetTokenService.validatePasswordResetToken(passwordResetToken);
    }
    @Override
    public User findUserByPasswordToken(String passwordResetToken) {
        return passwordResetTokenService.findUserByPasswordToken(passwordResetToken).get();
    }
    @Override
    public boolean changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        if(!isPasswordStrong(newPassword)) {
            return false;
        }
        userRepository.save(user);
        return true;
    }
    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = tokenRepository.findByToken(oldToken);
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        return tokenRepository.save(verificationToken);
    }
    @Override
    public boolean oldPasswordIsValid(User user, String oldPassword){
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}
