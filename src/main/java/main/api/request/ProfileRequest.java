package main.api.request;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequest {

    private MultipartFile photo;
    private String name;
    private String email;
    private String password;
    private int removePhoto;
}
