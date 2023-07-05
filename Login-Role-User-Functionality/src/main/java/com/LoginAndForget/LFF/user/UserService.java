package com.LoginAndForget.LFF.user;

import com.LoginAndForget.LFF.registration.RegistrationRequest;
import com.LoginAndForget.LFF.registration.token.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);

    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    boolean isPasswordStrong(String password);

    void createPasswordResetTokenForUser(User user, String passwordToken);

    String validatePasswordResetToken(String passwordResetToken);

    User findUserByPasswordToken(String passwordResetToken);

    boolean changePassword(User user, String newPassword);

    VerificationToken generateNewVerificationToken(String oldToken);

    boolean oldPasswordIsValid(User user, String oldPassword);
}
