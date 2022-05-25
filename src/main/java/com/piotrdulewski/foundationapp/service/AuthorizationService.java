package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.dto.LoginUserDTO;
import com.piotrdulewski.foundationapp.dto.RegisterFoundationDTO;
import com.piotrdulewski.foundationapp.dto.RegisterUserDTO;
import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.entity.MailVerificationToken;
import com.piotrdulewski.foundationapp.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthorizationService {
    ResponseEntity<?> loginIn(LoginUserDTO LoginuserDTO);

    User registration(RegisterUserDTO userDTO);

    Foundation registration(RegisterFoundationDTO FoundationDTO);

    MailVerificationToken getVerificationToken(String token);

    UserDetails activeUserAccount(UserDetails user, MailVerificationToken verificationToken);
}
