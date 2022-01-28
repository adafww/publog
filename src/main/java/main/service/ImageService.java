package main.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class ImageService {

    public String upload(MultipartFile image) throws IOException {

        if(!image.isEmpty() && image.getSize() > 5242880){

            return "{\n" +
                    " \"result\": false,\n" +
                    " \"errors\": {\n" +
                    " \"image\": \"Размер файла превышает допустимый размер\"\n" +
                    " }\n" +
                    "}";
        }else if(Objects.equals(image.getContentType(), "image/jpeg") || Objects.equals(image.getContentType(), "image/png")){

            HashCodeGenerator generator = new HashCodeGenerator();
            Path forCreateDir = Paths.get("upload",
                    generator.generate(2, 6),
                    generator.generate(2, 6),
                    generator.generate(2, 6));
            Files.createDirectories(forCreateDir);
            Path dir = Paths.get(forCreateDir.toString(), image.getOriginalFilename());

            try (OutputStream os = Files.newOutputStream(dir)) {

                os.write(image.getBytes());
                os.flush();
            }

            return "\\" + dir.toString();
        }else {

            return "{\n" +
                    " \"result\": false,\n" +
                    "}";
        }
    }
}
