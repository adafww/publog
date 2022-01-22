package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRequest;
import main.api.request.PassChangerRequest;
import main.api.request.RestoreRequest;
import main.api.response.*;
import main.api.request.RegFormRequest;
import main.service.CaptchaService;
import main.service.RegisterService;
import main.service.LoginService;
import main.service.RestoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.Principal;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final RestoreService restoreService;

    public final JavaMailSender emailSender;

    @PostMapping("/password")
    public ResponseEntity<ErrorResponse> changePassword(@RequestBody PassChangerRequest request){
        return new ResponseEntity<>(restoreService.changePass(request), HttpStatus.OK);
    }

    @PostMapping("/restore")
    public ResponseEntity<ErrorResponse> restore(@RequestBody RestoreRequest request) throws UnknownHostException {
        return new ResponseEntity<>(restoreService.getRestore(request), HttpStatus.OK);
    }

    @GetMapping(value = "/captcha")
    public CaptchaResponse captcha() throws IOException {
        return captchaService.getApiAuthCaptcha();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal){

        if(principal == null){
            return ResponseEntity.ok(new LoginResponse());
        }
        return ResponseEntity.ok(loginService.getLoginResponse(principal.getName()));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ErrorResponse> register(@RequestBody RegFormRequest entity) {
        return new ResponseEntity<>(registerService.getApiAuthRegisterResponse(entity), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest entity) {
        return ResponseEntity.ok(loginService.getLoginResponse(entity.getEmail(), entity.getPassword()));
    }

    @GetMapping("/logout")
    public ResponseEntity<ErrorResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>(new ErrorResponse(true), HttpStatus.OK);
    }
}
