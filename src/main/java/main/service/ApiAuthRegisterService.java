package main.service;

import main.api.response.ApiAuthRegisterAbstractResponse;
import main.api.response.ApiAuthRegisterBadResponse;
import main.api.response.ApiAuthRegisterOkResponse;
import main.dto.RegFormDto;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Hashtable;
import java.util.regex.Pattern;

@Service
public class ApiAuthRegisterService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    CaptchaCodeRepository captchaCodeRepo;

    public ApiAuthRegisterAbstractResponse response (RegFormDto regFormDto){

        Hashtable<String, String> errors = new Hashtable<>();
        Pattern pattern = Pattern.compile("^[А-ЯЁа-яё]+");
        boolean check = false;

        if(userRepo.existsByEmail("%" + regFormDto.getEEmail() + "%")){
            errors.put("email", "Этот e-mail уже зарегистрирован");
            check = true;
        }
        if(!pattern.matcher(regFormDto.getName()).matches()){
            errors.put("name", "Имя указано неверно");
            check = true;
        }

        if(regFormDto.getPassword().length() < 6){
            errors.put("password", "Пароль короче 6-ти символов");
            check = true;
        }

        if(!captchaCodeRepo.existsByCaptcha(regFormDto.getCaptcha())){
            errors.put("captcha", "Код с картинки введён неверно");
            check = true;
        }


        if(check){

            return new ApiAuthRegisterBadResponse(errors);
        }else {

            userRepo.save(new User(new Date(), regFormDto.getName(), regFormDto.getEEmail(), regFormDto.getPassword()));

            return new ApiAuthRegisterOkResponse();
        }
    }
}
