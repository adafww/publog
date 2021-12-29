package main.controller;

import main.api.response.ApiAuthCaptchaResponse;
import main.repository.CaptchaCodeRepository;
import main.service.ApiAuthCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PersistenceContext;
import java.io.IOException;

@RestController
public class ApiAuthController {



    @Autowired
    ApiAuthCaptchaService apiAuthCaptchaService;

    @GetMapping(value = "/api/auth/captcha")
    public ApiAuthCaptchaResponse captcha() throws IOException {
        return apiAuthCaptchaService.getApiAuthCaptcha();
    }
    

}
