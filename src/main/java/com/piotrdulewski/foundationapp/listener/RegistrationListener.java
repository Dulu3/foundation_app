package com.piotrdulewski.foundationapp.listener;

import com.piotrdulewski.foundationapp.entity.MailVerificationToken;
import com.piotrdulewski.foundationapp.event.OnRegistrationCompleteEvent;
import com.piotrdulewski.foundationapp.service.MailVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private MailVerificationTokenService mailVerificationTokenService;

    @Value("${spring.base.uri}")
    private String baseUri;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        UserDetails user = event.getUser();
        MailVerificationToken verificationToken = mailVerificationTokenService.createVerificationToken(user);
        String address = user.getUsername();
        String subject = "Potwierdzenie rejestracji";
        String confirmationUrl = event.getAppUrl() + "/api/auth/registrationConfirm?token=" + verificationToken.getToken();
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(address);
        email.setSubject(subject);
        email.setText("\r\n" + baseUri + confirmationUrl);
        new Thread(() -> mailSender.send(email)).start();
    }


}
