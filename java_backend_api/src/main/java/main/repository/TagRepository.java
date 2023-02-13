package main.repository;

import main.dto.TagForDtoRepository;
import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("select " +
            "new main.dto.TagForDtoRepository(t.name, count (p.tagID)) " +
            "from Tag t, Tag2Post p " +
            "inner join p.tagID on t = p.tagID " +
            "inner join Post post on p.postId.id = post.id " +
            "where p.postId.moderationStatusType = 'ACCEPTED' " +
            "group by p.tagID " +
            "order by count (p.tagID) desc")
    List<TagForDtoRepository> findAllForDto();

    @Query("select case when count (t) > 0 then true else false end from Tag t where t.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("select t from Tag t where t.name = :name")
    Tag findByName(@Param("name") String name);

    @Query("select t.name " +
            "from Tag t " +
            "left join Tag2Post t2 on t.id = t2.tagID.id " +
            "where t2.postId.id = :id")
    List<String> tagsByPostId(@Param("id") int id);

    @Query("select count (t) " +
            "from Tag t " +
            "left join Tag2Post tp on t.id = tp.tagID.id " +
            "where tp.tagID.name = :name")
    int tagsByName(@Param("name") String name);
}
