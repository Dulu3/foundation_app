package com.piotrdulewski.foundationapp.rest;

import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.entity.Request;
import com.piotrdulewski.foundationapp.service.FoundationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoundationController {
    private FoundationService foundationService;

    @Autowired
    public FoundationController(FoundationService foundationService) {
        this.foundationService = foundationService;
    }

    @ApiOperation(value = "This method is used to get list of all foundations",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/foundations")
    public List<Foundation> getFoundations() {
        return foundationService.findAll();
    }

    @ApiOperation(value = "This method is used to foundation with a given id",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/foundations/{foundationId}")
    public Foundation getFoundation(@PathVariable long foundationId) {

        Foundation foundation = foundationService.findById(foundationId);

        if (foundation == null) {
            throw new RuntimeException("Foundation not found - id: " + foundationId);
        }
        return foundation;
    }

    @ApiOperation(value = "This method is used to get list of requests belonging to a given foundation",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/foundationRequest/{foundationId}")
    public List<Request> getFoundationRequest(@PathVariable long foundationId) {

        Foundation foundation = foundationService.findById(foundationId);

        if (foundation == null) {
            throw new RuntimeException("Foundation not found - id: " + foundationId);
        }

        return foundation.getRequests();
    }

    @ApiOperation(value = "This method is used to add new foundation",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PostMapping("/foundations")
    public Foundation addFoundation(@RequestBody Foundation foundation) {
        foundation.setId(null);
        foundationService.save(foundation);
        return foundation;
    }


    @ApiOperation(value = "This method is used to update foundation",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PutMapping("/foundations")
    public Foundation updateFoundation(@RequestBody Foundation benefit) {
        foundationService.save(benefit);
        return benefit;
    }
}