package com.piotrdulewski.foundationapp;

import com.piotrdulewski.foundationapp.rest.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;

@SpringBootTest
class FoundationappApplicationTests {

	@Test
	void contextLoads() {

	}

	@Test
	void getUser(){
		UserController userController = mock(UserController.class);

	}




}
