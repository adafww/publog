package main.controller;

import main.api.request.LoginRequest;
import main.api.response.*;
import main.api.request.RegFormRequest;
import main.service.ApiAuthCaptchaService;
import main.service.ApiAuthRegisterService;
import main.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final ApiAuthCaptchaService apiAuthCaptchaService;
    private final ApiAuthRegisterService apiAuthRegisterService;
    private final LoginService loginService;

    @Autowired
    public ApiAuthController(ApiAuthCaptchaService apiAuthCaptchaService,
                             ApiAuthRegisterService apiAuthRegisterService, LoginService loginService) {
        this.apiAuthCaptchaService = apiAuthCaptchaService;
        this.apiAuthRegisterService = apiAuthRegisterService;
        this.loginService = loginService;
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
        return ResponseEntity.ok(loginService.getLoginResponse(principal.getName()));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ApiAuthRegisterAbstractResponse> register(@RequestBody RegFormRequest entity) {
        return new ResponseEntity<>(apiAuthRegisterService.getApiAuthRegisterResponse(entity), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest entity) {
        return ResponseEntity.ok(loginService.getLoginResponse(entity.getEmail(), entity.getPassword()));
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>(new LogoutResponse(), HttpStatus.OK);
    }
}
