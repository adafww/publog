package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.ProfileRequest;
import main.api.response.ErrorResponse;
import main.repository.UserRepository;
import org.imgscalr.Scalr;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.imageio.ImageIO;
import java.util.Hashtable;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepo;

    /*public ErrorResponse changeProfile(ProfileRequest request){

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Hashtable<String, String> errors = new Hashtable<>();
        Pattern pattern = Pattern.compile("^[А-ЯЁа-яё]+");
        boolean check = false;
        boolean checkEmail = false;
        boolean checkAddPhoto = false;
        boolean checkRemove = false;
        boolean checkPassword = false;

        if(!userEmail.equals(request.getEmail())){

            if(userRepo.existsByEmail("%" + request.getEmail() + "%")){

                errors.put("email", "Этот e-mail уже зарегистрирован");
                check = true;
            }else {

                checkEmail = true;
            }
        }

        if(request.getPhoto() != null){

            if(request.getPhoto().getSize() > 5242880){

                errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
                check = true;
            }else if(request.getRemovePhoto() == 0){

                checkAddPhoto = true;
            }else {

                checkAddPhoto = true;
                checkRemove = true;
            }
        }
        if(request.getPhoto() == null && request.getRemovePhoto() == 1){

            checkRemove = true;
        }

        if(request.getName() != null && !pattern.matcher(request.getName()).matches()){

            errors.put("name", "Имя указано неверно");
            check = true;
        }

        if(request.getPassword() != null){

            if(request.getPassword().length() < 6){

                errors.put("password", "Пароль короче 6-ти символов");
                check = true;
            }else {

                checkPassword = true;
            }
        }

        if(check){

            return new ErrorResponse(false, errors);
        }else {

            if(checkRemove) {

                //удаление фото
            }

            if(checkEmail && checkAddPhoto && checkPassword) {

                //изменение почты, имени, пароля и добавление фото
            }else if(checkEmail  && checkPassword) {

                //изменение почты, имени, пароля
                userRepo.nameEmailAndPassUpdate(
                        userEmail,
                        request.getEmail(),
                        request.getName(),
                        new BCryptPasswordEncoder(12)
                                .encode(request.getPassword()));
            } else if(checkEmail) {

                //изменение почты и имени
                System.out.println(userEmail);
                System.out.println(request.getEmail());
                System.out.println(request.getName());
                userRepo.nameAndEmailUpdate(userEmail, request.getEmail(), request.getName());
            }

            return new ErrorResponse(true);
        }
    }*/

    /*System.out.println(request.getEmail());
        System.out.println(request.getName());
        System.out.println(request.getPassword());
        System.out.println(request.getRemovePhoto());
        System.out.println(request.getPhoto().getContentType());
        System.out.println(Scalr.resize(ImageIO.read(new ByteArrayInputStream(request.getPhoto().getBytes())), Scalr.Method.ULTRA_QUALITY,
    Scalr.Mode.FIT_EXACT, 36, 36).getWidth());*/

}
