package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ErrorResponse;
import main.api.request.RegFormRequest;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Hashtable;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class RegisterService {


    private final UserRepository userRepo;
    private final CaptchaCodeRepository captchaCodeRepo;

    public ErrorResponse getApiAuthRegisterResponse (RegFormRequest regFormRequest){

        Hashtable<String, String> errors = new Hashtable<>();
        Pattern pattern = Pattern.compile("^[А-ЯЁа-яё]+");
        boolean check = false;

        if(userRepo.existsByEmail("%" + regFormRequest.getEEmail() + "%")){

            errors.put("email", "Этот e-mail уже зарегистрирован");
            check = true;
        }
        if(!pattern.matcher(regFormRequest.getName()).matches()){

            errors.put("name", "Имя указано неверно");
            check = true;
        }

        if(regFormRequest.getPassword().length() < 6){

            errors.put("password", "Пароль короче 6-ти символов");
            check = true;
        }

        if(!captchaCodeRepo.existsByCaptcha(regFormRequest.getCaptcha())){

            errors.put("captcha", "Код с картинки введён неверно");
            check = true;
        }

        if(check){

            return new ErrorResponse(false, errors);
        }else {

            userRepo.save(
                    new User(
                            new Date(),
                            regFormRequest.getName(),
                            regFormRequest.getEEmail(),
                            new BCryptPasswordEncoder(12)
                                    .encode(regFormRequest.getPassword())
                    )
            );

            return new ErrorResponse(true);
        }
    }
}
