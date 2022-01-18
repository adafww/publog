package main.repository;

import main.dto.ApiStatisticsDto;
import main.model.Tag2Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Tag2PostRepository extends CrudRepository<Tag2Post, Integer> {

    @Query("select " +
            "case when count (t) > 0 then true else false end " +
            "from Tag2Post t " +
            "left join Tag tg on t.tagID.id = tg.id " +
            "where t.tagID.name like :tagName " +
            "and t.postId.id = :postId")
    boolean existsByTagIDAndPostId(@Param("postId") int postId, @Param("tagName") String tagName);

    @Query("select " +
            "case when count (t) > 0 then true else false end " +
            "from Tag2Post t " +
            "left join Tag tg on t.tagID.id = tg.id " +
            "where t.tagID.name = :tagName")
    boolean existsByTagID(@Param("tagName") String tagName);

    @Query("select t " +
            "from Tag2Post t " +
            "left join Tag tg on t.tagID.id = tg.id " +
            "where t.tagID.name = :tagName " +
            "and t.postId.id = :postId")
    List<Tag2Post> findByName(@Param("postId") int postId, @Param("tagName") String tagName);

    @Query("select t.name " +
            "from Tag2Post tg " +
            "left join Tag t on tg.tagID.id = t.id " +
            "where tg.postId.id = :id")
    List<String> getTags(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "insert into lib.tag2post(post_id, tag_id) " +
            "VALUE (:postId, (select t.id from lib.tags t where t.name = :tagName))", nativeQuery = true)
    void saveByPostAndTagName(@Param("postId") int postId, @Param("tagName") String tagName);

    @Query("select new main.dto.ApiStatisticsDto(" +
            "(select count (p) from p), " +
            "(select count (pv) from pv where pv.value = true), " +
            "(select count (pv) from pv where pv.value = false), " +
            "(select sum (p.viewCount) from p), " +
            "(min(p.time))) " +
            "from Tag2Post t2p " +
            "left join Post p on t2p.postId.id = p.id " +
            "left join PostVote pv on p.id = pv.post.id ")
    ApiStatisticsDto getAllStatistics();

    @Query("select new main.dto.ApiStatisticsDto(" +
            "(select count (p) from p where p.user.email like :email), " +
            "(select count (pv) from PostVote pv left join User u on pv.user = u where pv.value = true and u.email like :email), " +
            "(select count (pv) from PostVote pv left join User u on pv.user = u where pv.value = false and u.email like :email), " +
            "(select sum (p.viewCount) from p where p.user.email like :email), " +
            "(min(p.time))) " +
            "from Tag2Post t2p " +
            "left join Post p on t2p.postId.id = p.id " +
            "left join PostVote pv on p.id = pv.post.id " +
            "left join User u on t2p.postId.user.id = u.id " +
            "where u.email like :email")
    ApiStatisticsDto getMyStatistics(@Param("email") String email);
}
