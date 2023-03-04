package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PostVoteRepository extends CrudRepository<PostVote, Integer> {

    @Query("select " +
            "case when count (pv) > 0 then true else false end " +
            "from PostVote pv " +
            "where pv.post.id = :postId " +
            "and pv.user.id = :userId " )
    boolean voted(@Param("postId") int postId, @Param("userId") int userId);

    @Query("select " +
            "case when count (pv) > 0 then true else false end " +
            "from PostVote pv " +
            "where pv.post.id = :postId " +
            "and pv.user.id = :userId " +
            "and pv.value = true " )
    boolean isLike(@Param("postId") int postId, @Param("userId") int userId);

    @Query("select case when count (pv) > 0 then true else false end " +
            "from PostVote pv " +
            "left join User u on pv.user.id = u.id " +
            "left join Post p on pv.post.id = p.id " +
            "where u.email like :email " +
            "and p.moderationStatusType = 'ACCEPTED'")
    boolean userExists(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("delete " +
            "from PostVote pv " +
            "where pv.post.id = :postId and pv.user.id = :userId")
    void deleteByUserIdAndPostId(@Param("postId") int postId, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("update PostVote pv " +
            "set pv.value = :value " +
            "where pv.post.id = :postId and pv.user.id = :userId")
    void update(@Param("postId") int postId, @Param("userId") int userId, @Param("value") boolean value);

    @Modifying
    @Transactional
    @Query(value = "insert into " +
            "lib.post_votes(time, value, post_id, user_id) " +
            "VALUES (:time, :value, :postId, :userId)",
            nativeQuery = true)
    void insertInto(@Param("time") Date time, @Param("postId") int postId, @Param("userId") int userId, @Param("value") boolean value);

    @Query("select case when pv.value = true then 1 else 0 end " +
            "from PostVote pv " +
            "left join User u on pv.user.id = u.id " +
            "left join Post p on pv.post.id = p.id " +
            "where u.email like :email " +
            "and p.moderationStatusType = 'ACCEPTED'")
    List<Byte> getUserVotes(@Param("email") String email);
}
