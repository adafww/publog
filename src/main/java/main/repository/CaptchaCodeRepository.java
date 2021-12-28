package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;

public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Integer> {

    /*@Modifying
    @Query(value = "insert into lib.captcha_codes(code, secret_code, time) values (:code, :secretCode, CURRENT_DATE) ", nativeQuery = true)
    void insert(@Param("code") String code, @Param("secretCode") String secretCode);*/

}
