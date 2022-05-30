package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Benefit;
import com.piotrdulewski.foundationapp.entity.Request;
import com.piotrdulewski.foundationapp.repository.BenefitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
public class BenefitServiceImplTest {
    @InjectMocks
    BenefitServiceImpl benefitService;

    @Mock
    BenefitRepository benefitRepository;

    Benefit benefit_1, benefit_2, benefit_3, benefit_4;

    @BeforeEach
    void init(){

        benefit_1 = new Benefit();
        benefit_1.setId(0L);
        benefit_1.setName("Wesola fundacja");

        benefit_2 = new Benefit();
        benefit_2.setId(1L);
        benefit_2.setName("Wesola fundacja");

        benefit_3 = new Benefit();
        benefit_3.setId(2L);
        benefit_3.setName("Wesola fundacja");

        benefit_4 = new Benefit();
        benefit_4.setId(3L);
        benefit_4.setName("Wesola fundacja");

    }

    @Test
    void findAllShouldReturnAllBenefits(){

        //given
        List<Benefit> expectedList = List.of(benefit_1, benefit_2, benefit_2, benefit_4);
        when(benefitRepository.findAll()).thenReturn(expectedList);

        //when
        List<Benefit> resultList = benefitService.findAll();

        //then
        assertThat(resultList, hasSize(4));
    }

    @Test
    void findBenefitByIdShouldReturnBenefitOfId(){
        //given
        long id = 2;
        Benefit expectedBenefit = benefit_3;
        when(benefitRepository.findById(id)).thenReturn(ofNullable(expectedBenefit));

        //when
        Benefit resultBenefit = benefitService.findById(id);

        //thhen
        assertThat(expectedBenefit,equalTo(resultBenefit));
    }
    @Test
    void findBenefitByIdShouldReturnExceptionWhenNotFind(){
        //given
        long id = 4;
        String expectedMessage = "Benefit not found id - : ";

        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                benefitService.findById(id));
        String actualMessage = thrown.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void afterSavingSizeShouldIncrease(){

        //given
        List<Benefit> expectedList = List.of(benefit_1, benefit_2, benefit_2, benefit_4);
        when(benefitRepository.findAll()).thenReturn(expectedList);

        //when
        benefitService.save(benefit_4);

        //then
        assertThat(benefitService.findAll(),hasSize(4));
    }
}
