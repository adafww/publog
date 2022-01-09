package main.controller;

import com.google.gson.JsonObject;
import main.api.request.LoginRequest;
import main.api.response.ApiAuthCaptchaResponse;
import main.api.response.ApiAuthRegisterAbstractResponse;
import main.api.request.RegFormRequest;
import main.api.response.LoginResponse;
import main.api.response.UserLoginResponse;
import main.repository.UserRepository;
import main.service.ApiAuthCaptchaService;
import main.service.ApiAuthRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final ApiAuthCaptchaService apiAuthCaptchaService;
    private final ApiAuthRegisterService apiAuthRegisterService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    @Autowired
    public ApiAuthController(ApiAuthCaptchaService apiAuthCaptchaService,
                             ApiAuthRegisterService apiAuthRegisterService,
                             AuthenticationManager authenticationManager,
                             UserRepository userRepository) {
        this.apiAuthCaptchaService = apiAuthCaptchaService;
        this.apiAuthRegisterService = apiAuthRegisterService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/captcha")
    public ApiAuthCaptchaResponse captcha() throws IOException {
        return apiAuthCaptchaService.getApiAuthCaptcha();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal){

        if(principal == null){
            return ResponseEntity.ok(new LoginResponse());
        }
        return ResponseEntity.ok(getLoginResponse(principal.getName()));
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

        return ResponseEntity.ok(getLoginResponse(user.getUsername()));
    }
    private LoginResponse getLoginResponse(String email) {

        main.model.User currentUser =
                userRepository.findByEmail(
                        email).orElseThrow(
                        () -> new UsernameNotFoundException(email));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setModeration(currentUser.isModerator());
        userLoginResponse.setId(currentUser.getId());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);

        return loginResponse;
    }
}
