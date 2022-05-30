package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.dto.RegisterUserDTO;
import com.piotrdulewski.foundationapp.entity.User;
import com.piotrdulewski.foundationapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.internet.MimeMessage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class AuthorizationServiceIntegrationTest {

    @MockBean
    JavaMailSender javaMailSender;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    UserRepository userRepository;

    @Test
    void registerUserShouldSaveUserToDataBase(){
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
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        //when
        User resultUser = authorizationService.registration(registerUserDTO);
        Optional<User> opDataBase = userRepository.findByEmail("example@org");

        //then
        Assertions.assertTrue(opDataBase.isPresent());
        User inDataBaseUser = opDataBase.get();
        assertAll(
                () -> assertEquals(inDataBaseUser.getUsername(),resultUser.getUsername()),
                () -> assertEquals(inDataBaseUser.getFirstName(),resultUser.getFirstName()),
                () -> assertEquals(inDataBaseUser.getLastName(),resultUser.getLastName()),
                () -> assertEquals(inDataBaseUser.getEmail(),resultUser.getEmail())
        );

    }
}
