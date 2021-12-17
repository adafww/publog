package main.repository;

import main.dto.UserDto;
import main.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select new main.dto.UserDto(u.id, u.name) from User u where u.id = :id")
    List<UserDto> findById(@Param("id") int id);
}
