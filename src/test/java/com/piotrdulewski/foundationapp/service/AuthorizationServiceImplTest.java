package com.piotrdulewski.foundationapp.service;


import com.piotrdulewski.foundationapp.advice.TokenRefreshException;
import com.piotrdulewski.foundationapp.dto.LoginUserDTO;
import com.piotrdulewski.foundationapp.dto.RegisterFoundationDTO;
import com.piotrdulewski.foundationapp.dto.RegisterUserDTO;
import com.piotrdulewski.foundationapp.entity.AccessToken;
import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.entity.MailVerificationToken;
import com.piotrdulewski.foundationapp.entity.User;
import com.piotrdulewski.foundationapp.repository.FoundationRepository;
import com.piotrdulewski.foundationapp.repository.UserRepository;
import com.piotrdulewski.foundationapp.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceImplTest {

    UserRepository userRepository = mock(UserRepository.class);

    FoundationRepository foundationRepository = mock(FoundationRepository.class);

    ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);

    AccessTokenService accessTokenService = mock(AccessTokenServiceImpl.class);

    MailVerificationTokenService mailVerificationTokenService = mock(MailVerificationTokenService.class);

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    JwtUtils jwtUtils = new JwtUtils();

    AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl(
            userRepository,
            bCryptPasswordEncoder,
            applicationEventPublisher,
            accessTokenService,
            mock(HttpServletRequest.class),
            jwtUtils,
            mailVerificationTokenService,
            foundationRepository);

    User user_1, user_2;

    Foundation foundation_1, foundation_2;

    @BeforeEach
    void init() {

        user_1 = new User();
        user_1.setId(0L);
        user_1.setEmail("example_u0@org.com");
        user_1.setFirstName("Jan");
        user_1.setLastName("Kowalski");
        user_1.setActive(false);

        user_2 = new User();
        user_2.setId(1L);
        user_2.setEmail("example_u1@org.com");
        user_2.setFirstName("Paweł");
        user_2.setLastName("Nietypowy");
        user_2.setActive(true);


        foundation_1 = new Foundation();
        foundation_1.setId(0L);
        foundation_1.setEmail("example_f0@org.com");
        foundation_1.setName("Wesola fundacja");

        foundation_2 = new Foundation();
        foundation_2.setId(1L);
        foundation_2.setEmail("example_f1@org.com");
        foundation_2.setName("Wesola fundacja");


    }

    @Test
    void loginInShouldReturnResponseOkWithCorrectCredentialsForUser() {
        //given
        String username = "example_u1@org.com";
        String password = "test";
        user_2.setPassword(bCryptPasswordEncoder.encode(password));
        AccessToken accessToken = new AccessToken();
        accessToken.setToken("123");
        when(foundationRepository.findByEmail(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(username)).thenReturn(ofNullable(user_2));
        when(accessTokenService.createAccessTokenFor(anyLong(), anyString())).thenReturn(accessToken);

        //when
        ResponseEntity<?> response = authorizationService.loginIn(new LoginUserDTO(username, password));

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void loginInShouldReturnResponseOkWithCorrectCredentialsForFoundation() {
        //given
        String username = "example_f1@org.com";
        String password = "test";
        foundation_2.setPassword(bCryptPasswordEncoder.encode(password));
        AccessToken accessToken = new AccessToken();
        accessToken.setToken("123");
        when(foundationRepository.findByEmail(anyString())).thenReturn(ofNullable(foundation_2));
        when(accessTokenService.createAccessTokenFor(anyLong(), anyString())).thenReturn(accessToken);

        //when
        ResponseEntity<?> response = authorizationService.loginIn(new LoginUserDTO(username, password));

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void loginInShouldResponseBadRequestWhenWrongCredentialsForUser() {
        //given
        String username = "example_u1@org.com";
        String password = "test123";
        foundation_2.setPassword(bCryptPasswordEncoder.encode("test"));
        AccessToken accessToken = new AccessToken();
        accessToken.setToken("123");
        when(foundationRepository.findByEmail(anyString())).thenReturn(ofNullable(foundation_2));
        when(accessTokenService.createAccessTokenFor(anyLong(), anyString())).thenReturn(accessToken);

        //when
        ResponseEntity<?> response = authorizationService.loginIn(new LoginUserDTO(username, password));

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void registerUserShouldCreateNotActiveUser() {
        //given
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .username("username")
                .password("test")
                .address("adres")
                .email("example@org")
                .logoPath("/")
                .build();

        //when
        User resultUser = authorizationService.registration(registerUserDTO);

        //then
        assertFalse(resultUser.isActive());

    }

    @Test
    void registerUserShouldCreateNotActiveFoundation() {
        //given
        RegisterFoundationDTO registerUserDTO = RegisterFoundationDTO.builder()
                .name("wesola fundacja")
                .password("test")
                .address("adres")
                .email("example@org")
                .logoPath("/")
                .build();

        //when
        Foundation resultFoundation = authorizationService.registration(registerUserDTO);

        //then
        assertNull(resultFoundation.getEmailVerifiedAt());

    }

    @Test
    void getVerificationTokenShouldReturnExceptionIfTokenNotFound() {
        //given
        String token = "asdagasf";
        String exceptedMessage = "Token not found";

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                authorizationService.getVerificationToken(token));
        //then
        assertEquals(exceptedMessage, thrown.getMessage());

    }

    @Test
    void activeUserAccountShouldActiveUserAccount() {
        //given
        User userToActive = user_1;
        MailVerificationToken mailVerificationToken = new MailVerificationToken();
        when(mailVerificationTokenService.verifyExpiration(any())).thenReturn(mailVerificationToken);
        when(userRepository.findByEmail(anyString())).thenReturn(ofNullable(userToActive));

        //when
        User resultUser = (User) authorizationService.activeUserAccount(userToActive, mailVerificationToken);

        //then
        assertTrue(resultUser.isActive());

    }

    @Test
    void activeUserAccountShouldActiveFoundationAccount() {
        //given
        Foundation foundationToActive = foundation_2;
        MailVerificationToken mailVerificationToken = new MailVerificationToken();
        when(mailVerificationTokenService.verifyExpiration(any())).thenReturn(mailVerificationToken);
        when(foundationRepository.findByEmail(anyString())).thenReturn(ofNullable(foundationToActive));

        //when
        Foundation resultFoundation = (Foundation) authorizationService.activeUserAccount(foundationToActive, mailVerificationToken);

        //then
        assertNotNull(resultFoundation.getEmailVerifiedAt());

    }

    @Test
    void registerFoundationShouldThrownExceptionInNameTaken() {
        //given
        String exceptedMessage = "Username or email already exists!";
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .username("username")
                .password("test")
                .address("adres")
                .email("example@org")
                .logoPath("/")
                .build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user_2));

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> authorizationService.registration(registerUserDTO));

        //then
        assertEquals(exceptedMessage, thrown.getMessage());


    }

    @Test
    void registerUserShouldThrownExceptionInNameTaken() {
        //given
        String exceptedMessage = "Username or email already exists!";
        RegisterFoundationDTO registerFoundationDTO = RegisterFoundationDTO.builder()
                .name("wesola fundacja")
                .password("test")
                .address("adres")
                .email("example@org")
                .logoPath("/")
                .build();
        when(foundationRepository.findByEmail(anyString())).thenReturn(Optional.of(foundation_2));

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> authorizationService.registration(registerFoundationDTO));

        //then
        assertEquals(exceptedMessage, thrown.getMessage());

    }

    @Test
    void activeUserAccountShouldReturnNullIfNotPassingUserOrFoundation() {
        //given
        UserDetails userDetails = null;
        MailVerificationToken mailVerificationToken = new MailVerificationToken();
        when(mailVerificationTokenService.verifyExpiration(any())).thenReturn(mailVerificationToken);
        when(userRepository.findByEmail(anyString())).thenReturn(ofNullable(user_2));

        //when
        User resultUser = (User) authorizationService.activeUserAccount(userDetails, mailVerificationToken);

        //then
        assertNull(resultUser);

    }

    @Test
    void activeUserAccountThrownTokenRefreshException() {
        //given
        User userToActive = user_1;
        MailVerificationToken mailVerificationToken = new MailVerificationToken();
        when(mailVerificationTokenService.verifyExpiration(any())).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(ofNullable(userToActive));

        //when
        TokenRefreshException tokenRefreshException = assertThrows(TokenRefreshException.class, () ->
                authorizationService.activeUserAccount(userToActive, mailVerificationToken));

        //then
        assertTrue(tokenRefreshException.getMessage().contains("Verification tokens is expired"));

    }

}
