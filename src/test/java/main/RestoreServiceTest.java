package main;

import main.api.request.PassChangerRequest;
import main.api.request.RestoreRequest;
import main.api.response.ErrorResponse;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import main.service.RestoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.net.UnknownHostException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование RestoreService")
public class RestoreServiceTest {

    @Mock
    public JavaMailSender emailSender;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CaptchaCodeRepository captchaCodeRepo;

    @Test
    @DisplayName("Сообщение метода getRestoreTest успешно отправлено, код сохранен в БД")
    void getRestoreTest() throws UnknownHostException {

        String email = "email@mail.ru";
        RestoreRequest request = new RestoreRequest();
        request.setEmail(email);

        Mockito.when(userRepo.existsByEmail(request.getEmail())).thenReturn(true);

        Assertions.assertTrue(new RestoreService(emailSender, userRepo, captchaCodeRepo).getRestore(request, "testUrlTestUrlTestUrl").isResult());
        Mockito.verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
        Mockito.verify(userRepo, times(1)).codeUpdate(anyString(), anyString());
    }

    @Test
    @DisplayName("Пароль успешно обновлен")
    void changePass() {

        PassChangerRequest request = new PassChangerRequest();
        request.setCaptcha("test");
        request.setCode("code");
        request.setPassword("testPass");
        request.setCaptchaSecret("secretCaptchaTest");


        Mockito.when(userRepo.existsByCode("%" + request.getCode() + "%")).thenReturn(true);
        Mockito.when(captchaCodeRepo.existsByCaptcha(request.getCaptcha())).thenReturn(true);

        Assertions.assertTrue(new RestoreService(emailSender, userRepo, captchaCodeRepo).changePass(request).isResult());
        Mockito.verify(userRepo, times(1)).passUpdateByCode(anyString(), anyString());
    }

    @Test
    @DisplayName("Получены все возможные ошибки при попытке обновить пароль")
    void changePassErrors() {

        PassChangerRequest request = new PassChangerRequest();
        request.setCaptcha("test");
        request.setCode("code");
        request.setPassword("test");
        request.setCaptchaSecret("secretCaptchaTest");


        Mockito.when(userRepo.existsByCode("%" + request.getCode() + "%")).thenReturn(false);
        Mockito.when(captchaCodeRepo.existsByCaptcha(request.getCaptcha())).thenReturn(false);

        ErrorResponse response = new RestoreService(emailSender, userRepo, captchaCodeRepo).changePass(request);

        Assertions.assertFalse(response.isResult());
        Assertions.assertEquals(3, response.getErrors().size());
    }
}
