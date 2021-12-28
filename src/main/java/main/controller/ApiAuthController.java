package main.controller;

import main.api.response.ApiAuthCaptchaResponse;
import main.service.ApiAuthCaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ApiAuthController {

    ApiAuthCaptchaService apiAuthCaptchaService = new ApiAuthCaptchaService();

    @GetMapping(value = "/api/auth/captcha")
    public ApiAuthCaptchaResponse captcha() throws IOException {
        return apiAuthCaptchaService.getApiAuthCaptcha();
    }
    

}
