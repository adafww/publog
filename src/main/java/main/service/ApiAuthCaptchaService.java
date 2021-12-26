package main.service;

import com.github.cage.Cage;
import com.github.cage.image.Painter;
import main.api.response.ApiAuthCaptchaResponse;
import main.repository.CaptchaCodeRepository;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.Random;

@Service
public class ApiAuthCaptchaService {

    CaptchaCodeRepository captchaCodeRepo;

    public ApiAuthCaptchaResponse getApiAuthCaptcha() {

        String captchaCode = genRandCode(4, 6);
        ApiAuthCaptchaResponse apiAuthCaptchaResponse = new ApiAuthCaptchaResponse();
        Random rnd = new Random();
        Cage cage = new Cage();
        Painter painter = new Painter();
        byte[] bytes = new Cage(
                new Painter(
                        100,
                        35,
                        painter.getBackground(),
                        painter.getQuality(),
                        painter.getEffectConfig(),
                        rnd),
                cage.getFonts(),
                cage.getForegrounds(),
                cage.getFormat(),
                cage.getCompressRatio(),
                cage.getTokenGenerator(),
                rnd)
                .draw(captchaCode);
        String encodedString =  "data:image/png;base64, " + Base64.getEncoder().encodeToString(bytes);
        String secretCode = genRandCode(15, 25);
        apiAuthCaptchaResponse.setSecret(secretCode);
        apiAuthCaptchaResponse.setImage(encodedString);

        captchaCodeRepo.insert(captchaCode, secretCode);

        return apiAuthCaptchaResponse;
    }

    private String genRandCode(int minimumLength, int maximumLength) {

        int randomLength = minimumLength + (int)(Math.random() * maximumLength);
        String[] charCategories = new String[] {
                "abcdefghijklmnopqrstuvwxyz",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "0123456789"
        };
        StringBuilder code = new StringBuilder(randomLength);
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < randomLength; i++) {
            String charCategory = charCategories[random.nextInt(charCategories.length)];
            int position = random.nextInt(charCategory.length());
            code.append(charCategory.charAt(position));
        }

        return new String(code);
    }
}
