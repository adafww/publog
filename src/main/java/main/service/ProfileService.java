package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.ProfileRequest;
import main.api.request.ProfileWithPhotoRequest;
import main.api.response.ErrorResponse;
import main.repository.UserRepository;
import org.imgscalr.Scalr;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Hashtable;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepo;

    public ErrorResponse editProfile(ProfileWithPhotoRequest request) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Hashtable<String, String> errors = new Hashtable<>();
        Pattern pattern = Pattern.compile("^[А-ЯЁа-яё]+");
        boolean check = false;
        boolean checkEmail = false;
        boolean checkName = false;
        boolean checkAddPhoto = false;
        boolean checkRemove = false;
        boolean checkPassword = false;


        if(!userEmail.equals(request.getEmail())){

            if(userRepo.existsByEmail(request.getEmail())){

                errors.put("email", "Этот e-mail уже зарегистрирован");
                check = true;
            }else {

                checkEmail = true;
            }
        }

        if(!userRepo.existsNameByEmail(userEmail, request.getName())){

            checkName = true;
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

            if(checkPassword) {

                userRepo.passUpdate(userEmail, new BCryptPasswordEncoder(12).encode(request.getPassword()));
            }

            if(checkEmail) {

                userRepo.emailUpdate(userEmail, request.getEmail());
            }

            if(checkName) {

                userRepo.nameUpdate(userEmail, request.getName());
            }

            if(checkRemove) {

                userRepo.removePhoto(userEmail);
            }

            if(checkAddPhoto) {

                try {

                    userRepo.addPhoto(userEmail, uploadPhoto(ImageIO.read(new ByteArrayInputStream(request.getPhoto().getBytes()))));
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            return new ErrorResponse(true);
        }
    }

    private String uploadPhoto(BufferedImage bufferedImage) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(processing(bufferedImage), "png", out);

        return "data:image/png;base64, " + Base64.getEncoder().encodeToString(out.toByteArray());
    }

    private BufferedImage processing(BufferedImage bufferedImage){

        if(bufferedImage.getWidth() == bufferedImage.getHeight()){

            return Scalr.resize(bufferedImage,
                    Scalr.Method.ULTRA_QUALITY,
                    Scalr.Mode.FIT_EXACT,
                    36,
                    36
            );
        } else if(bufferedImage.getWidth() > bufferedImage.getHeight()){

            return Scalr.resize(cropWidth(bufferedImage),
                    Scalr.Method.ULTRA_QUALITY,
                    Scalr.Mode.FIT_EXACT,
                    36,
                    36
            );
        }else {

            return Scalr.resize(cropHeight(bufferedImage),
                    Scalr.Method.ULTRA_QUALITY,
                    Scalr.Mode.FIT_EXACT,
                    36,
                    36
            );
        }
    }

    private static BufferedImage cropHeight(BufferedImage src){

        return Scalr.crop(
                src,
                0,
                src.getHeight() / 2 - src.getWidth() / 2,
                src.getWidth() - 1,
                src.getWidth() - 1
        );
    }

    private static BufferedImage cropWidth(BufferedImage src){

        return Scalr.crop(
                src,
                src.getWidth() / 2 - src.getHeight() / 2,
                0,
                src.getHeight() - 1,
                src.getHeight() - 1
        );
    }
}
