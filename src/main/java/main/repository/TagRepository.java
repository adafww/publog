package main.repository;

import main.dto.TagForDtoRepository;
import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select new main.dto.TagForDtoRepository(t.name, count (p.tagID)) from Tag t, Tag2Post p inner join p.tagID on t = p.tagID group by p.tagID order by count (p.tagID) desc")
    List<TagForDtoRepository> findAllForDto();
}
