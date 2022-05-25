package com.piotrdulewski.foundationapp.rest;

import com.piotrdulewski.foundationapp.entity.Request;
import com.piotrdulewski.foundationapp.service.PDFGeneratorService;
import com.piotrdulewski.foundationapp.service.RequestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RequestController {

    private RequestService requestService;
    private PDFGeneratorService pdfGeneratorService;

    public RequestController(RequestService requestService,
                             PDFGeneratorService pdfGeneratorService) {
        this.requestService = requestService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @ApiOperation(value = "This method is used to get list of all requests",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/requests")
    public List<Request> getRequests() {
        return requestService.findAll();
    }

    @ApiOperation(value = "This method is used to request with a given id",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/requests/{requestId}")
    public Request getRequest(@PathVariable long requestId) {
        Request request = requestService.findById(requestId);

        if (request == null) {
            throw new RuntimeException("Request not found - id: " + requestId);
        }
        return request;
    }

    @ApiOperation(value = "This method is used to add new request",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PostMapping("/requests")
    public Request addRequest(@RequestBody Request request) {
        request.setId(null);
        requestService.save(request);
        return request;
    }


    @ApiOperation(value = "This method is used to update request",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PutMapping("/requests")
    public Request updateRequest(@RequestBody Request request) {
        requestService.save(request);
        return request;
    }

    @ApiOperation(value = "This method is used to get attestation for done request",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/requests/attestation")
    public void getAttestation(@ApiParam(value = "You need to have in you request headers named by 'RequestId' and 'refreshToken' with relevant data ")
                                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentTime = dataFormat.format(new Date());

        String headerKey = "Content-Dispositon";
        String headerValue = "attachment; filename=attestation_" + currentTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        pdfGeneratorService.export(request, response);
    }
}

