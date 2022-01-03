package main.repository;

import main.model.CaptchaCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Integer> {

    @Modifying
    @Transactional
    @Query("delete from CaptchaCode c where c.time < :date")
    void deleteByDate(@Param("date") Date date);

}
