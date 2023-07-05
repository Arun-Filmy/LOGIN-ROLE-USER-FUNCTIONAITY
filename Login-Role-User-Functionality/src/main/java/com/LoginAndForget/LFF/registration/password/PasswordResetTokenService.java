package com.LoginAndForget.LFF.registration.password;
import com.LoginAndForget.LFF.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public void createPasswordResetTokenForUser(User user, String passwordToken){
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public String validatePasswordResetToken(String theToken){
        PasswordResetToken byToken = passwordResetTokenRepository.findByToken(theToken);
        if(byToken == null){
            return "Invalid password reset token";
        }

        User user = byToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((byToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            return "link is already expire, resend link";
        }

        return "Valid";
    }

    public Optional<User> findUserByPasswordToken(String passwordToken){
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordToken).getUser());
    }
}
