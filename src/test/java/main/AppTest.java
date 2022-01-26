package main;

import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaCodeRepository;
import main.service.CaptchaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.commons.codec.binary.Base64;

//SpringExtension
@ExtendWith(MockitoExtension.class)
public class AppTest {

    @Mock
    private CaptchaCodeRepository codeRepository;

    @Test
    @DisplayName("Тестирование CaptchaService")
    void CaptchaCodeServiceTest() {

        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+");
        CaptchaResponse captcha = new CaptchaService(codeRepository).getApiAuthCaptcha();
        String forTestSecretCode = captcha.getSecret();
        String forTestImage = captcha.getImage();

        assertTrue(forTestSecretCode.length() >= 15 && forTestSecretCode.length() <= 25 && pattern.matcher(forTestSecretCode).matches());
        assertTrue(Base64.isBase64(forTestImage.substring(24)));
        Mockito.verify(codeRepository, times(1)).save(any(CaptchaCode.class));
    }
}

//CaptchaCode captchaCodeToReturn = new CaptchaCode(new Date(), "test", "test");
//Mockito.when(codeRepository.save(any(CaptchaCode.class))).thenReturn(captchaCodeToReturn);

