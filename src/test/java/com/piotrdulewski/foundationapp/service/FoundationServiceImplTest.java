package com.piotrdulewski.foundationapp.service;


import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.repository.FoundationRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoundationServiceImplTest {

    @InjectMocks
    FoundationServiceImpl foundationService;

    @Mock
    FoundationRepository foundationRepository;

    Foundation foundation_1, foundation_2, foundation_3, foundation_4;

    @BeforeEach
    void init(){

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

    }

    @Test
    void findAllShouldReturnAllFoundations(){

        //given
        List<Foundation> expectedList = List.of(foundation_1,foundation_2,foundation_3,foundation_4);
        when(foundationRepository.findAll()).thenReturn(expectedList);

        //when
        List<Foundation> resultList = foundationService.findAll();

        //then
        assertThat(resultList, hasSize(4));
    }

    @Test
    void findByIdShouldReturnFoundationWithSpecificId(){
        //given
        long id = 2;
        Foundation expectedFoundation = foundation_3;
        when(foundationRepository.findById(id)).thenReturn(ofNullable(expectedFoundation));

        //when
        Foundation resultFoundation = foundationService.findById(id);

        //given
        assertThat(expectedFoundation,equalTo(resultFoundation));
    }

    @Test
    void findRequestByIdShouldReturnExceptionWhenNotFind(){
        //given
        long id = 4;
        String expectedMessage = "Foundation not found id - : ";

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                foundationService.findById(id));
        String actualMessage = thrown.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void afterSavingSizeShouldIncrease(){

        //given
        List<Foundation> expectedList = List.of(foundation_1,foundation_2,foundation_3,foundation_4);
        when(foundationRepository.findAll()).thenReturn(expectedList);

        //when
        foundationService.save(foundation_4);

        //then
        assertThat(foundationService.findAll(),hasSize(4));
    }

}
