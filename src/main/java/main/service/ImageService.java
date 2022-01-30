package main.service;

import main.api.response.ErrorResponse;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Objects;

@Service
public class ImageService {

    public Object upload(MultipartFile image) throws IOException {

        int resolution = 1000;
        if(!image.isEmpty() && image.getSize() > 5242880){
            Hashtable<String, String> errors = new Hashtable<>();
            errors.put("image", "Размер файла превышает допустимый размер");
            return new ErrorResponse(false, errors);
        }else if(Objects.equals(image.getContentType(), "image/jpeg") || Objects.equals(image.getContentType(), "image/png")){

            HashCodeGenerator generator = new HashCodeGenerator();
            Path forCreateDir = Paths.get("upload",
                    generator.generate(2, 6),
                    generator.generate(2, 6),
                    generator.generate(2, 6));
            Files.createDirectories(forCreateDir);
            Path dir = Paths.get(forCreateDir.toString(), image.getOriginalFilename());
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));

            if(bufferedImage.getHeight() > resolution || bufferedImage.getWidth() > resolution){

                try (OutputStream os = Files.newOutputStream(dir)) {

                    ImageIO.write(resize(bufferedImage, resolution), "jpg", os);
                    os.flush();
                }

            }else {

                try (OutputStream os = Files.newOutputStream(dir)) {

                    os.write(image.getBytes());
                    os.flush();
                }
            }

            return "\\" + dir.toString();
        }else {

            return new ErrorResponse(false);
        }
    }

    private BufferedImage resize(BufferedImage bufferedImage, int resolution){

        if(bufferedImage.getWidth() > resolution){

            return Scalr.resize(bufferedImage,
                    Scalr.Method.ULTRA_QUALITY,
                    Scalr.Mode.FIT_EXACT,
                    resolution,
                    (int) Math.round(bufferedImage.getHeight() / (bufferedImage.getWidth() / (double) resolution))
            );
        }else {

            return Scalr.resize(bufferedImage,
                    Scalr.Method.ULTRA_QUALITY,
                    Scalr.Mode.FIT_EXACT,
                    (int) Math.round(bufferedImage.getWidth() / (bufferedImage.getHeight() / (double) resolution)),
                    resolution
            );
        }
    }
}
