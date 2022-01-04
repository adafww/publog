package main.controller;

import com.google.gson.JsonObject;
import main.api.response.ApiAuthCaptchaResponse;
import main.api.response.ApiAuthRegisterAbstractResponse;
import main.dto.RegFormDto;
import main.repository.CaptchaCodeRepository;
import main.service.ApiAuthCaptchaService;
import main.service.ApiAuthRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceContext;
import java.io.IOException;

@RestController
public class ApiAuthController {



    @Autowired
    ApiAuthCaptchaService apiAuthCaptchaService;

    @Autowired
    ApiAuthRegisterService apiAuthRegisterService;

    @GetMapping(value = "/api/auth/captcha")
    public ApiAuthCaptchaResponse captcha() throws IOException {
        return apiAuthCaptchaService.getApiAuthCaptcha();
    }

    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<ApiAuthRegisterAbstractResponse> register(@RequestBody RegFormDto entity) {
        return new ResponseEntity<>(apiAuthRegisterService.response(entity), HttpStatus.OK);
    }
}
