package com.piotrdulewski.foundationapp.rest;

import com.piotrdulewski.foundationapp.entity.RequestCategory;
import com.piotrdulewski.foundationapp.service.RequestCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RequestCategoryController {

    private RequestCategoryService requestCategoryService;

    public RequestCategoryController(RequestCategoryService requestCategoryService) {
        this.requestCategoryService = requestCategoryService;
    }

    @ApiOperation(value = "This method is used to get list of all requestCategories",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/requestCategories")
    public List<RequestCategory> getRequestCategories() {
        return requestCategoryService.findAll();
    }


    @ApiOperation(value = "This method is used to requestCategories with a given id of request",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/requestCategories/{id}")
    public RequestCategory getRequestCategory(@PathVariable Long id) {
        return requestCategoryService.findById(id);
    }

    @ApiOperation(value = "This method is used to add new requestCategories",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PostMapping("/requestCategories")
    public void addRequestCategory(@RequestBody RequestCategory requestCategory) {
        requestCategory.setId(null);
        requestCategoryService.save(requestCategory);
    }


    @ApiOperation(value = "This method is used to update requestCategories",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PutMapping("/requestCategories")
    public void updateRequestCategory(@RequestBody RequestCategory requestCategory) {
        requestCategoryService.save(requestCategory);
    }
}
