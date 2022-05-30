package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.User;
import com.piotrdulewski.foundationapp.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.*;



@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    User user_1, user_2, user_3, user_4, user_5;



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

    }

    @Test
    void findAllShouldReturnAllUsers(){
        //given
        List<User> expectedList = List.of(user_1,user_2,user_3,user_4);
        when(userRepository.findAll()).thenReturn(expectedList);

        //when
        List<User> resultList = userService.findAll();

        //then
        assertThat(resultList, hasSize(4));

    }

    @Test
    void findUserByIdShouldReturnUserWithSpecificId(){
        //given
        long id = 3;
        User expectedUser = user_3;
        when(userRepository.findById(id)).thenReturn(ofNullable(expectedUser));

        //when
        User resultUser = userService.findById(id);

        //then
        assertThat(resultUser, equalTo(expectedUser));

    }
    @Test
    void findUserByIdShouldReturnExceptionWhenNotFind(){
        //given
        long id = 4;
        String expectedMessage = "User not found id - : ";

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                userService.findById(id));
        String actualMessage = thrown.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void afterSavingSizeShouldIncrease(){
        //given
        User newUser = user_5;
        List<User> expectedList = List.of(user_1,user_2,user_3,user_4, newUser);
        when(userRepository.findAll()).thenReturn(expectedList);

        //when
        userService.save(newUser);

        //then
        assertThat(userService.findAll(), hasSize(5));

    }

 }
