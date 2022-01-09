package main.service;

import com.github.cage.Cage;
import com.github.cage.image.Painter;
import lombok.RequiredArgsConstructor;
import main.api.response.ApiAuthCaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class ApiAuthCaptchaService {

    private final CaptchaCodeRepository codeRepository;

    private final long HOUR_UTC = 3_600_000;

        public ApiAuthCaptchaResponse getApiAuthCaptcha() {

        String captchaCode = genRandCode(4, 5);
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
        Date date = Date.from(Instant.now());
        codeRepository.save(new CaptchaCode(date, captchaCode, secretCode));

        return apiAuthCaptchaResponse;
    }

    @Scheduled(fixedRate = HOUR_UTC)
    private void delCaptchaForOneHour(){

        codeRepository.deleteByDate(new Date(System.currentTimeMillis() - HOUR_UTC));
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
