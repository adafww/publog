package main.service;

import com.github.cage.Cage;
import com.github.cage.image.Painter;
import lombok.RequiredArgsConstructor;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class CaptchaService {

    private final CaptchaCodeRepository codeRepository;

    private final long HOUR_UTC = 3_600_000;

        public CaptchaResponse getApiAuthCaptcha() {
        HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();

        String captchaCode = hashCodeGenerator.generate(4, 5);
        CaptchaResponse captchaResponse = new CaptchaResponse();
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
        String secretCode = hashCodeGenerator.generate(15, 25);
        captchaResponse.setSecret(secretCode);
        captchaResponse.setImage(encodedString);
        Date date = Date.from(Instant.now());
        codeRepository.save(new CaptchaCode(date, captchaCode, secretCode));

        return captchaResponse;
    }

    @Scheduled(fixedRate = HOUR_UTC)
    private void delCaptchaForOneHour(){

        codeRepository.deleteByDate(new Date(System.currentTimeMillis() - HOUR_UTC));
    }
}
