package main.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileWithPhotoRequest{

    private MultipartFile photo;
    private String name;
    private String email;
    private String password;
    private int removePhoto;

    public ProfileWithPhotoRequest(String name, String email, String password, int removePhoto) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.removePhoto = removePhoto;

    }
}
