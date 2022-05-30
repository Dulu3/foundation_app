package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.entity.Request;
import com.piotrdulewski.foundationapp.repository.FoundationRepository;
import com.piotrdulewski.foundationapp.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {
    @InjectMocks
    RequestServiceImpl requestService;

    @Mock
    RequestRepository requestRepository;

    Request request_1, request_2, request_3, request_4;

    @BeforeEach
    void init(){

         request_1 = new Request();
         request_1.setId(0L);
         request_1.setName("Wesola fundacja");

         request_2 = new Request();
         request_2.setId(1L);
         request_2.setName("Wesola fundacja");

         request_3 = new Request();
         request_3.setId(2L);
         request_3.setName("Wesola fundacja");

         request_4 = new Request();
         request_4.setId(3L);
         request_4.setName("Wesola fundacja");

    }

    @Test
    void findAllRequests(){

        //given
        List<Request> expectedList = List.of(request_1,request_2,request_2,request_4);
        when(requestRepository.findAll()).thenReturn(expectedList);

        //when
        List<Request> resultList = requestService.findAll();

        //then
        assertThat(resultList, hasSize(4));
    }

    @Test
    void findByIdShouldReturnRequestWithSpecificId(){
        //given
        long id = 2;
        Request expectedRequest = request_3;
        when(requestRepository.findById(id)).thenReturn(ofNullable(expectedRequest));

        //when
        Request resultRequest = requestService.findById(id);

        //given
        assertThat(expectedRequest,equalTo(resultRequest));
    }
    @Test
    void findRequestByIdShouldReturnExceptionWhenNotFind(){
        //given
        long id = 4;
        String expectedMessage = "Request not found id -  : ";

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                requestService.findById(id));
        String actualMessage = thrown.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void afterSavingSizeShouldIncrease(){

        //given
        List<Request> expectedList = List.of(request_1,request_2,request_2,request_4);
        when(requestRepository.findAll()).thenReturn(expectedList);

        //when
        requestService.save(request_4);

        //then
        assertThat(requestService.findAll(),hasSize(4));
    }
}
