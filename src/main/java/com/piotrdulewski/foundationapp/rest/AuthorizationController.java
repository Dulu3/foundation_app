package com.piotrdulewski.foundationapp.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrdulewski.foundationapp.advice.TokenRefreshException;
import com.piotrdulewski.foundationapp.dto.LoginUserDTO;
import com.piotrdulewski.foundationapp.dto.RegisterFoundationDTO;
import com.piotrdulewski.foundationapp.dto.RegisterUserDTO;
import com.piotrdulewski.foundationapp.entity.AccessToken;
import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.entity.MailVerificationToken;
import com.piotrdulewski.foundationapp.entity.User;
import com.piotrdulewski.foundationapp.payload.response.MessageResponse;
import com.piotrdulewski.foundationapp.payload.response.TokenRefreshResponse;
import com.piotrdulewski.foundationapp.security.JwtUtils;
import com.piotrdulewski.foundationapp.service.AccessTokenService;
import com.piotrdulewski.foundationapp.service.AuthorizationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    public AuthorizationController(AuthorizationService authorizationService, AccessTokenService accessTokenService, JwtUtils jwtUtils, ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.accessTokenService = accessTokenService;
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;

    }

    private final ObjectMapper objectMapper;
    private final AuthorizationService authorizationService;
    private final AccessTokenService accessTokenService;
    private final JwtUtils jwtUtils;


    @PostMapping("/login")
    @ApiOperation(value = "This method is used to login")
    public ResponseEntity<?> logginIn(@RequestBody LoginUserDTO user) {
        return authorizationService.loginIn(user);
    }

    @ApiOperation(value = "This method is used to register new user")
    @PutMapping("/registrationUser")
    public User registration(@RequestBody RegisterUserDTO obj) {
        return authorizationService.registration(obj);
    }

    @ApiOperation(value = "This method is used to register new foundation")
    @PutMapping("/registrationFoundation")
    public Foundation registration(@RequestBody RegisterFoundationDTO obj) {
        return authorizationService.registration(obj);
    }

    @ApiOperation(value = "This method is used to provide new JWT token",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PutMapping("/refreshAccessToken")
    public ResponseEntity<?> refreshAccessToken(@ApiParam(value = "refreshToken received upon logging in", example = "becf342e-787f-4702-bb31-15015c2bec11")
                                                @RequestParam("token") String token) {

        return accessTokenService.findByToken(token).map(accessTokenService::verifyExpiration)
                .map(AccessToken::getUserDetails)
                .map(userDetails -> {
                    String accessToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, token));
                }).orElseThrow(() -> new TokenRefreshException(token, "RefreshToken does not exists!"));
    }

    @ApiOperation(value = "This method is used to active account in database")
    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {

        MailVerificationToken verificationToken = authorizationService.getVerificationToken(token);

        if (authorizationService.activeUserAccount(verificationToken.getUserDetails(), verificationToken) != null) {
            return ResponseEntity.ok(new MessageResponse("Uzytkownik aktywowany"));
        }

        return ResponseEntity.ok(new MessageResponse("Uzytkownik nie zostal aktywowany"));

    }

}
