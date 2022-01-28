package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegFormRequest {

    @JsonProperty("e_mail")
    private String eEmail;
    private String password;
    private String name;
    private String captcha;
    @JsonProperty("captcha_secret")
    private String captchaSecret;

    public RegFormRequest(String eEmail, String password, String name, String captcha, String captchaSecret) {
        this.eEmail = eEmail;
        this.password = password;
        this.name = name;
        this.captcha = captcha;
        this.captchaSecret = captchaSecret;
    }
}
