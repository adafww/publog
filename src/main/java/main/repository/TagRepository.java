package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select t from Tag t, Tag2Post p inner join p.tagID on t = p.tagID group by p.tagID order by count (p.tagID) desc")
    List<Tag> findSortedObject();
}
