package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRequest;
import main.api.response.ApiAuthCaptchaResponse;
import main.api.response.ApiAuthRegisterAbstractResponse;
import main.api.request.RegFormRequest;
import main.api.response.LoginResponse;
import main.api.response.UserLoginResponse;
import main.repository.UserRepository;
import main.service.ApiAuthCaptchaService;
import main.service.ApiAuthRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final ApiAuthCaptchaService apiAuthCaptchaService;
    private final ApiAuthRegisterService apiAuthRegisterService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;


    @GetMapping(value = "/captcha")
    public ApiAuthCaptchaResponse captcha() throws IOException {
        return apiAuthCaptchaService.getApiAuthCaptcha();
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ApiAuthRegisterAbstractResponse> register(@RequestBody RegFormRequest entity) {
        return new ResponseEntity<>(apiAuthRegisterService.response(entity), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest entity) {
        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(entity.getEmail(), entity.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();
        main.model.User currentUser =
                userRepo.findByEmail(
                        user.getUsername()).orElseThrow(
                                () -> new UsernameNotFoundException(user.getUsername()
                                )
                );

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setModeration(currentUser.isModerator() == true);
        userLoginResponse.setId(currentUser.getId());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        System.out.println(entity.getEmail());
        System.out.println(entity.getPassword());
        return ResponseEntity.ok(new LoginResponse());
    }
}
