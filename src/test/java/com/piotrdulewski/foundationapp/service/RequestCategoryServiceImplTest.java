package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.entity.RequestCategory;
import com.piotrdulewski.foundationapp.repository.RequestCategoryRepository;
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
public class RequestCategoryServiceImplTest {

    @InjectMocks
    RequestCategoryServiceImpl requestCategoryService;

    @Mock
    RequestCategoryRepository requestCategoryRepository;

    RequestCategory requestCategory_1, requestCategory_2, requestCategory_3, requestCategory_4;

    @BeforeEach
    void init(){

        requestCategory_1 = new RequestCategory();
        requestCategory_1.setId(0L);
        requestCategory_1.setName("Wesola fundacja");

        requestCategory_2 = new RequestCategory();
        requestCategory_2.setId(1L);
        requestCategory_2.setName("Wesola fundacja");

        requestCategory_3 = new RequestCategory();
        requestCategory_3.setId(2L);
        requestCategory_3.setName("Wesola fundacja");

        requestCategory_4 = new RequestCategory();
        requestCategory_4.setId(3L);
        requestCategory_4.setName("Wesola fundacja");

    }

    @Test
    void findAllShouldReturnAllRequestCategories(){

        //given
        List<RequestCategory> expectedList = List.of(requestCategory_1, requestCategory_2, requestCategory_3, requestCategory_4);
        when(requestCategoryRepository.findAll()).thenReturn(expectedList);

        //when
        List<RequestCategory> resultList = requestCategoryService.findAll();

        //then
        assertThat(resultList, hasSize(4));
    }

    @Test
    void findByIdShouldReturnRequestCategoryWithSpecificId(){
        //given
        long id = 2;
        RequestCategory expectedRequestCategory = requestCategory_3;
        when(requestCategoryRepository.findById(id)).thenReturn(ofNullable(expectedRequestCategory));

        //when
        RequestCategory resultRequestCategory = requestCategoryService.findById(id);

        //given
        assertThat(expectedRequestCategory,equalTo(resultRequestCategory));
    }

    @Test
    void findByIdShouldReturnExceptionWhenNotFind(){
        //given
        long id = 4;
        String expectedMessage = "RequestCategory not found id - : ";

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                requestCategoryService.findById(id));
        String actualMessage = thrown.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void afterSavingSizeShouldIncrease(){

        //given
        List<RequestCategory> expectedList = List.of(requestCategory_1, requestCategory_2, requestCategory_3, requestCategory_4);
        when(requestCategoryRepository.findAll()).thenReturn(expectedList);

        //when
        requestCategoryService.save(requestCategory_4);

        //then
        assertThat(requestCategoryService.findAll(),hasSize(4));
    }

}
