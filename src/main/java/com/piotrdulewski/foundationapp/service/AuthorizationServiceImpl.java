package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.advice.TokenRefreshException;
import com.piotrdulewski.foundationapp.dto.LoginDTO;
import com.piotrdulewski.foundationapp.dto.LoginUserDTO;
import com.piotrdulewski.foundationapp.dto.RegisterFoundationDTO;
import com.piotrdulewski.foundationapp.dto.RegisterUserDTO;
import com.piotrdulewski.foundationapp.entity.AccessToken;
import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.entity.MailVerificationToken;
import com.piotrdulewski.foundationapp.entity.User;
import com.piotrdulewski.foundationapp.event.OnRegistrationCompleteEvent;
import com.piotrdulewski.foundationapp.payload.response.JwtResponse;
import com.piotrdulewski.foundationapp.payload.response.MessageResponse;
import com.piotrdulewski.foundationapp.repository.FoundationRepository;
import com.piotrdulewski.foundationapp.repository.UserRepository;
import com.piotrdulewski.foundationapp.security.JwtUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private UserRepository userRepository;
    private FoundationRepository foundationRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ApplicationEventPublisher eventPublisher;
    private AccessTokenService accessTokenService;
    private HttpServletRequest request;
    private JwtUtils jwtUtils;
    private MailVerificationTokenService mailVerificationTokenService;


    public AuthorizationServiceImpl(UserRepository userRepository,
                                    @Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
                                    ApplicationEventPublisher eventPublisher,
                                    AccessTokenService accessTokenService,
                                    HttpServletRequest request,
                                    JwtUtils jwtUtils,
                                    MailVerificationTokenService mailVerificationTokenService,
                                    FoundationRepository foundationRepository) {
        this.userRepository = userRepository;
        this.foundationRepository = foundationRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.eventPublisher = eventPublisher;
        this.accessTokenService = accessTokenService;
        this.mailVerificationTokenService = mailVerificationTokenService;
        this.request = request;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<?> loginIn(LoginUserDTO loginUserDTO) {
        LoginDTO loginDTO = verifyClient(loginUserDTO);
        if (loginDTO != null) {
            String token = jwtUtils.generateJwtToken(loginUserDTO);
            AccessToken refreshToken = accessTokenService.createAccessTokenFor(loginDTO.getId(), loginDTO.getType());
            return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken(), loginUserDTO.getUsername(), loginDTO.getType(), loginDTO.getId()));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Bad username or password"));
    }

    @Override
    public User registration(RegisterUserDTO userDTO) {
        User user;
        if (nameOrEmailTaken(userDTO)) {
            user = User.builder()
                    .firstName(userDTO.getFirstName())
                    .lastName(userDTO.getLastName())
                    .email(userDTO.getEmail())
                    .name(userDTO.getUsername())
                    .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                    .address(userDTO.getAddress())
                    .isActive(false)
                    .isNeedy((byte) 0)
                    .logo_path("")
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            userRepository.save(user);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), request.getContextPath()));
        } else throw new RuntimeException("Username or email already exists!");
        return user;
    }

    @Override
    public Foundation registration(RegisterFoundationDTO foundationDTO) {
        Foundation foundation;
        if (nameOrEmailTaken(foundationDTO)) {
            foundation = Foundation.builder()
                    .name(foundationDTO.getName())
                    .email(foundationDTO.getEmail())
                    .password(bCryptPasswordEncoder.encode(foundationDTO.getPassword()))
                    .address(foundationDTO.getAddress())
                    .NIP(foundationDTO.getNIP())
                    .logoPath(foundationDTO.getLogoPath())
                    .description("Brak")
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            foundationRepository.save(foundation);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(foundation, request.getLocale(), request.getContextPath()));
        } else throw new RuntimeException("Username or email already exists!");
        return foundation;
    }

    @Override
    public MailVerificationToken getVerificationToken(String token) {
        return mailVerificationTokenService.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }

    @Override
    public UserDetails activeUserAccount(UserDetails user, MailVerificationToken verificationToken) {
        if (mailVerificationTokenService.verifyExpiration(verificationToken) != null) {
            if (user instanceof User) {
                User userToActive = userRepository.findByEmail(user.getUsername()).get();
                userToActive.setActive(true);
                userToActive.setEmailVerifiedAt(new Timestamp(System.currentTimeMillis()));
                userRepository.save(userToActive);
                mailVerificationTokenService.delete(verificationToken);
                return userToActive;
            } else if (user instanceof Foundation) {
                Foundation userToActive = foundationRepository.findByEmail(user.getUsername()).get();
                userToActive.setEmailVerifiedAt(new Timestamp(System.currentTimeMillis()));
                foundationRepository.save(userToActive);
                mailVerificationTokenService.delete(verificationToken);
                return userToActive;
            }
        } else throw new TokenRefreshException(verificationToken.getToken(), "Verification tokens is expired");

        return null;
    }


    private boolean nameOrEmailTaken(RegisterUserDTO userDTO) {
        return userRepository.findByEmail(userDTO.getEmail()).isEmpty();
    }

    private boolean nameOrEmailTaken(RegisterFoundationDTO foundationDTO) {
        return foundationRepository.findByEmail(foundationDTO.getEmail()).isEmpty();
    }

    private LoginDTO verifyClient(LoginUserDTO loginUserDTO) {
        Optional<Foundation> foundation = foundationRepository.findByEmail(loginUserDTO.getUsername());
        LoginDTO loginDTO;
        if (foundation.isEmpty()) {
            Optional<User> user = userRepository.findByEmail(loginUserDTO.getUsername());
            if (user.isEmpty()) return null;
            User user1 = user.get();
            loginDTO = LoginDTO.builder()
                    .id(user1.getId())
                    .username(user1.getUsername())
                    .type("user")
                    .build();
        } else {
            Foundation foundation1 = foundation.get();
            loginDTO = LoginDTO.builder()
                    .id(foundation1.getId())
                    .username(foundation1.getUsername())
                    .type("foundation")
                    .build();
        }
        return loginDTO;

    }

}
