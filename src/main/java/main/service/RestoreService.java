package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.PassChangerRequest;
import main.api.request.RestoreRequest;
import main.api.response.*;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.net.UnknownHostException;
import java.util.Hashtable;

@RequiredArgsConstructor
@Service
public class RestoreService {

    public final JavaMailSender emailSender;

    private final UserRepository userRepo;
    private final CaptchaCodeRepository captchaCodeRepo;

    public ErrorResponse getRestore(RestoreRequest request, String url) throws UnknownHostException {

        if(userRepo.existsByEmail(request.getEmail())){

            String code = new HashCodeGenerator().generate(35, 45);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(request.getEmail());
            simpleMailMessage.setText(url.substring(0, url.length() - 16) + "login/change-password/" + code);
            emailSender.send(simpleMailMessage);
            userRepo.codeUpdate(request.getEmail(), code);

            return new ErrorResponse(true);
        }else {

            return new ErrorResponse(false);
        }
    }

    public ErrorResponse changePass(PassChangerRequest request){

        Hashtable<String, String> errors = new Hashtable<>();
        boolean check = false;

        if(!userRepo.existsByCode("%" + request.getCode() + "%")){

            errors.put("code", "Ссылка для восстановления пароля устарела.\n" +
                    " <a href=" +
                    " \"/auth/restore\">Запросить ссылку снова</a>");
            check = true;
        }

        if(request.getPassword().length() < 6){

            errors.put("password", "Пароль короче 6-ти символов");
            check = true;
        }

        if(!captchaCodeRepo.existsByCaptcha(request.getCaptcha())){

            errors.put("captcha", "Код с картинки введён неверно");
            check = true;
        }

        if(check){

            return new ErrorResponse(false, errors);
        }else {

            userRepo.passUpdateByCode(request.getCode(), new BCryptPasswordEncoder(12)
                    .encode(request.getPassword()));

            return new ErrorResponse(true);
        }
    }
}
