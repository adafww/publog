package main.repository;

import main.dto.UserDto;
import main.dto.UserDtoWithPhoto;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u " +
            "from User u " +
            "where u.email = :email")
    User findByName(@Param("email") String email);

    Optional<User> findByEmail(String email);

    @Query("select new main.dto.UserDto(u.id, u.name) from User u where u.id = :id")
    List<UserDto> findById(@Param("id") int id);

    @Query("select new main.dto.UserDtoWithPhoto(u.id, u.name, u.photo) from User u where u.id = :id")
    List<UserDtoWithPhoto> findByIdWithPhoto(@Param("id") int id);

    @Query("select " +
            "case when count (u) > 0 then true else false end " +
            "from User u where u.email like :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("select " +
            "case when count (u) > 0 then true else false end " +
            "from User u where u.code like :code")
    boolean existsByCode(@Param("code") String code);

    @Query("select u.id from User u where u.email like :email")
    int idByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("update User u " +
            "set u.code = :code " +
            "where u.email like :email")
    void codeUpdate(@Param("email") String email, @Param("code") String code);

    @Modifying
    @Transactional
    @Query("update User u " +
            "set u.password = :newPass " +
            "where u.code like :code")
    void passUpdate(@Param("code") String code, @Param("newPass") String newPass);

    @Modifying
    @Transactional
    @Query("update User u " +
            "set u.email = :newEmail, u.name = :name " +
            "where u.email like :email ")
    void nameAndEmailUpdate(@Param("email") String email, @Param("newEmail") String newEmail, @Param("name") String name);

    @Modifying
    @Transactional
    @Query("update User u " +
            "set u.email = :newEmail, u.name = :name, u.password = :pass " +
            "where u.email like :email")
    void nameEmailAndPassUpdate(@Param("email") String email,
                                @Param("newEmail") String newEmail,
                                @Param("name") String name,
                                @Param("pass") String pass);
}
