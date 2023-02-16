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
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование CaptchaService")
public class CaptchaCodeServiceTest {

    @Mock
    private CaptchaCodeRepository codeRepository;

    @Test
    @DisplayName("SecretCode сгененрирован")
    void secretCodeTest() {

        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+");
        CaptchaResponse captcha = new CaptchaService(codeRepository).getApiAuthCaptcha();
        String forTestSecretCode = captcha.getSecret();

        assertTrue(forTestSecretCode.length() >= 15 && forTestSecretCode.length() <= 25 && pattern.matcher(forTestSecretCode).matches());
    }

    @Test
    @DisplayName("Изображение каптчи сгенерировано")
    void Base64ImageTest() {

        CaptchaResponse captcha = new CaptchaService(codeRepository).getApiAuthCaptcha();
        String forTestImage = captcha.getImage();

        assertTrue(Base64.isBase64(forTestImage.substring(24)));
    }

    @Test
    @DisplayName("Данные сохранены в БД")
    void CaptchaCodeRepositoryTest() {

        new CaptchaService(codeRepository).getApiAuthCaptcha();

        Mockito.verify(codeRepository, times(1)).save(any(CaptchaCode.class));
    }
}
