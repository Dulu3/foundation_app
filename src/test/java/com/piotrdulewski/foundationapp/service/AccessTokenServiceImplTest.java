package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.advice.TokenRefreshException;
import com.piotrdulewski.foundationapp.entity.AccessToken;
import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.entity.User;
import com.piotrdulewski.foundationapp.repository.AccessTokenRepository;
import com.piotrdulewski.foundationapp.repository.BenefitRepository;
import com.piotrdulewski.foundationapp.repository.FoundationRepository;
import com.piotrdulewski.foundationapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessTokenServiceImplTest {

    @InjectMocks
    AccessTokenServiceImpl accessTokenService;

    @Mock
    AccessTokenRepository accessTokenRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    FoundationRepository foundationRepository;

    User user_1,user_2,user_3,user_4,user_5;

    Foundation foundation_1,foundation_2,foundation_3,foundation_4;

    AccessToken accessToken_1, accessToken_2;

    @BeforeEach
    void init(){
        user_1 = new User();
        user_1.setId(0L);
        user_1.setFirstName("Jan");
        user_1.setLastName("Kowalski");
        user_1.setActive(true);

        user_2 = new User();
        user_2.setId(1L);
        user_2.setFirstName("Paweł");
        user_2.setLastName("Nietypowy");
        user_2.setActive(true);

        user_3 = new User();
        user_3.setId(2L);
        user_3.setFirstName("Piotr");
        user_3.setLastName("Morski");
        user_3.setActive(true);

        user_4 = new User();
        user_4.setId(3L);
        user_4.setFirstName("Kamil");
        user_4.setLastName("Niepotwierdzony");
        user_4.setActive(false);

        user_5 = new User();
        user_5.setId(4L);
        user_5.setFirstName("Przemek");
        user_5.setLastName("Dziwny");
        user_5.setActive(true);

        foundation_1 = new Foundation();
        foundation_1.setId(0L);
        foundation_1.setEmail("example@org.com");
        foundation_1.setName("Wesola fundacja");

        foundation_2 = new Foundation();
        foundation_2.setId(1L);
        foundation_2.setEmail("example@org.com");
        foundation_2.setName("Wesola fundacja");

        foundation_3 = new Foundation();
        foundation_3.setId(2L);
        foundation_3.setEmail("example@org.com");
        foundation_3.setName("Wesola fundacja");

        foundation_4 = new Foundation();
        foundation_4.setId(3L);
        foundation_4.setEmail("example@org.com");
        foundation_4.setName("Wesola fundacja");

        accessToken_1 = generateToken();
        accessToken_1.setUserDetails(foundation_1);
        accessToken_2 = generateToken();
        accessToken_2.setUserDetails(user_1);



    }

    @Test
    void findByTokenShouldReturnToken(){
        //given
        AccessToken expectedAccessToken = accessToken_1;
        when(accessTokenRepository.findByToken(expectedAccessToken.getToken())).thenReturn(ofNullable(accessToken_1));

        //when
        Optional<AccessToken> opt = accessTokenService.findByToken(expectedAccessToken.getToken());

        //then
        assertTrue(opt.isPresent());
        AccessToken resultAccessToken = opt.get();
        assertEquals(expectedAccessToken.getToken(), resultAccessToken.getToken());

    }
    @Test
    void generateTokenForShouldGenerateTokenForUser(){
        //given
        long id = 1;
        User expectedUser = user_2;
        when(userRepository.findById(id)).thenReturn(ofNullable(user_2));
        when(accessTokenRepository.save(any(AccessToken.class))).thenAnswer((Answer<AccessToken>) invocation -> {
            Object[] args = invocation.getArguments();
            return (AccessToken) args[0];
        });

        //when
        AccessToken resultToken = accessTokenService.createAccessTokenFor(id,"user");

        //then
        assertEquals(resultToken.getUserDetails(), expectedUser);

    }

    @Test
    void generateTokenForShouldGenerateTokenForFoundation(){
        //given
        long id = 2;
        Foundation expectedFoundation= foundation_3;
        when(foundationRepository.findById(id)).thenReturn(ofNullable(foundation_3));
        when(accessTokenRepository.save(any(AccessToken.class))).thenAnswer((Answer<AccessToken>) invocation -> {
            Object[] args = invocation.getArguments();
            return (AccessToken) args[0];
        });

        //when
        AccessToken resultToken = accessTokenService.createAccessTokenFor(id,"foundation");

        //then
        assertEquals(resultToken.getUserDetails(), expectedFoundation);

    }
    @Test
    void generateTokenForShouldReturnRuntimeExceptionIfUserNotFound(){
        //given
        long id = 4;
        String expectedMessage = "User not found - : ";

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                accessTokenService.createAccessTokenFor(id,"user"));
        String actualMessage = thrown.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void generateTokenForShouldReturnRunTimeExceptionIfUserNotFound(){
        //given
        long id = 4;
        String expectedMessage = "Foundation not found - : ";

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                accessTokenService.createAccessTokenFor(id,"foundation"));
        String actualMessage = thrown.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void verifyExpirationShouldThrownTokenRefreshException(){
        //given
        String expectedMessage = "Refresh token utracil waznosc";
        AccessToken token = accessToken_1;
        token.setExpiryAt(new Timestamp(System.currentTimeMillis()));

        //when
        TokenRefreshException thrown = assertThrows(TokenRefreshException.class, () ->
                accessTokenService.verifyExpiration(token));
        String actualMessage = thrown.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void verifyExpirationShouldReturnTheSameToken(){
        //given
        AccessToken expectedToken = accessToken_1;
        expectedToken.setExpiryAt(new Timestamp(System.currentTimeMillis() + 20 * 1000));

        //when
        AccessToken resultToken = accessTokenService.verifyExpiration(expectedToken);


        //then
        assertEquals(expectedToken, resultToken);

    }


    private static AccessToken generateToken(){
        AccessToken token = new AccessToken();
        token.setToken(UUID.randomUUID().toString());
        token.setCratedAt(new Timestamp(System.currentTimeMillis()));
        token.setExpiryAt(new Timestamp(System.currentTimeMillis() + 24 * 360 * 1000));
        return token;
    }

}
