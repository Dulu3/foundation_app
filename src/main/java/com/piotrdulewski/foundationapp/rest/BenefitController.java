package com.piotrdulewski.foundationapp.rest;

import com.piotrdulewski.foundationapp.entity.Benefit;
import com.piotrdulewski.foundationapp.service.BenefitService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BenefitController {

    private BenefitService benefitService;

    public BenefitController(BenefitService benefitService) {
        this.benefitService = benefitService;
    }

    @ApiOperation(value = "This method is used to get list of all benefits",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/benefits")
    public List<Benefit> getBenefits() {
        return benefitService.findAll();
    }

    @ApiOperation(value = "This method is used to benefit with a given id",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/benefits/{benefitId}")
    public Benefit getBenefit(@PathVariable long benefitId) {
        return benefitService.findById(benefitId);
    }

    @ApiOperation(value = "This method is used to add new benefit",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PostMapping("/benefits")
    public Benefit addBenefit(@RequestBody Benefit benefit) {
        benefit.setId(null);
        benefitService.save(benefit);
        return benefit;
    }

    @ApiOperation(value = "This method is used to update benefit",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PutMapping("/benefits")
    public Benefit updateBenefit(@RequestBody Benefit benefit) {
        benefitService.save(benefit);
        return benefit;
    }
}