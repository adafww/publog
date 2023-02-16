package main;

import main.api.request.RegFormRequest;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import main.service.RegisterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование RegisterService")
public class RegisterTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private CaptchaCodeRepository captchaCodeRepo;

    @Test
    @DisplayName("Успешный ответ метода getTagResponse c сохранением данных в БД")
    void registerOkTest(){

        RegFormRequest request = new RegFormRequest();
        request.setEEmail("mail@.ru");
        request.setPassword("testPassword");
        request.setName("Тест");
        request.setCaptcha("testCaptcha");
        request.setCaptchaSecret("testSecretCode");

        Mockito.when(userRepo.existsByEmail("%" + request.getEEmail() + "%")).thenReturn(false);
        Mockito.when(captchaCodeRepo.existsByCaptcha(request.getCaptcha())).thenReturn(true);

        boolean result = new RegisterService(userRepo, captchaCodeRepo).getApiAuthRegisterResponse(request).isResult();

        Mockito.verify(userRepo, times(1)).save(any(User.class));
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Ответ метода getTagResponse со всеми возможными ошибками")
    void registerFailTest(){

        RegFormRequest request = new RegFormRequest();
        request.setEEmail("mail@.ru");
        request.setPassword("testPassword");
        request.setName("Тест");
        request.setCaptcha("testCaptcha");
        request.setCaptchaSecret("testSecretCode");

        Mockito.when(userRepo.existsByEmail("%" + request.getEEmail() + "%")).thenReturn(true);
        Mockito.when(captchaCodeRepo.existsByCaptcha(request.getCaptcha())).thenReturn(false);

        Assertions.assertEquals(4, new RegisterService(userRepo, captchaCodeRepo).getApiAuthRegisterResponse(request).getErrors().size());
    }
}
