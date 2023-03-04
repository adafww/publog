package main.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Date time;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false, name = "secret_code")
    private String secretCode;

    public CaptchaCode(Date time, String code, String secretCode) {
        this.time = time;
        this.code = code;
        this.secretCode = secretCode;
    }

    public CaptchaCode() {
    }
}
