package com.LoginAndForget.LFF.registration;

import com.LoginAndForget.LFF.event.RegistrationCompleteEvent;
import com.LoginAndForget.LFF.event.listener.RegistrationCompleteEventListener;
import com.LoginAndForget.LFF.registration.password.PasswordRequestUtil;
import com.LoginAndForget.LFF.registration.token.VerificationToken;
import com.LoginAndForget.LFF.registration.token.VerificationTokenRepository;
import com.LoginAndForget.LFF.user.User;
import com.LoginAndForget.LFF.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final HttpServletRequest servletRequest;

    @PostMapping("/newRegistration")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        boolean b = userService.isPasswordStrong(registrationRequest.password());
        if (b) {
            User user = userService.registerUser(registrationRequest);
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            return "Success! Please check your mail for further formalities";
        }
        return "Password is not strong enough! Please use at least one UpperCase letter, one special character, and minimum 8 length of password";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        String url = applicationUrl(servletRequest)+"/register/resend-verification-token?token="+token;
        VerificationToken theToken = tokenRepository.findByToken(token);
        if(theToken.getUser().isEnabled()){
            return "This account has already verified, please Login";
        }
        String validateToken = userService.validateToken(token);
        if(validateToken.equalsIgnoreCase("Valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification link, <a href=\""+url+"\"> Get a new verification link.</a>";
    }

    @PostMapping("/reset-password-request")
    public String resetPasswordRequest(@RequestBody PasswordRequestUtil passwordRequestUtil, final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = userService.findByEmail(passwordRequestUtil.getEmail());
        String passwordResetUrl = "";
        if(user.isPresent()){
            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), applicationUrl(request), passwordResetToken);
        }
        return passwordResetUrl;
    }

    private String passwordResetEmailLink(User user, String applicationUrl, String passwordResetToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/register/reset-password?token=" + passwordResetToken;
        eventListener.sendPasswordResetVerificationEmail(url);
        log.info("Click the link to reset your password {}" + url);
        return url;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordRequestUtil passwordRequestUtil,
                                @RequestParam("token") String passwordResetToken){
        String tokenValidationResult = userService.validatePasswordResetToken(passwordResetToken);
        if(!tokenValidationResult.equalsIgnoreCase("Valid")){
            return "Invalid password reset token";
        }
        User user = userService.findUserByPasswordToken(passwordResetToken);
        if(user != null){
            boolean b = userService.changePassword(user, passwordRequestUtil.getNewPassword());
            if(!b){
                return "Password is not meeting the requirement";
            }
            return "Password has been reset successfully";
        }
        return "Invalid Password reset token";
    }

    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam ("token") String oldToken, final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User theUser = verificationToken.getUser();
        resendVerificationTokenEmail(theUser, applicationUrl(request), verificationToken);
        return "New Verification Link has been sent in your email, please check to complete your registration";
    }

    private void resendVerificationTokenEmail(User theUser, String applicationUrl, VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/register/verifyEmail?token="+verificationToken.getToken();
        eventListener.sendVerificationEmail(url);
        log.info("Click the link to verify your registration : {}", url);
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody PasswordRequestUtil requestUtil){
        User user = userService.findByEmail(requestUtil.getEmail()).get();
        if(!userService.oldPasswordIsValid(user, requestUtil.getOldPassword())){
            return "The old password doesn't matches";
        }
        boolean changed = userService.changePassword(user, requestUtil.getNewPassword());
        if(!changed){
            return "The new Password doesn't follow the requirement of password";
        }
        return "The Password changes successfully";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
