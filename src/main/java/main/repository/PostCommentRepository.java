package main.repository;

import main.dto.CommentForPostForDto;
import main.model.PostComment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PostCommentRepository extends CrudRepository<PostComment, Integer> {

    @Query("select " +
            "new main.dto.CommentForPostForDto(c.id, c.time, c.text, c.userId.id) " +
            "from PostComment c " +
            "left join Post p on c.postId.id = p.id " +
            "where p.id = :id group by c order by c.time desc ")
    List<CommentForPostForDto> findPostCommentById(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "insert into " +
            "lib.post_comments(text, time, post_id, user_id) " +
            "VALUE(:text, :time, :postId, :userId)",
            nativeQuery = true)
    void saveCommentPost(@Param("postId") int postId, @Param("userId") int userId, @Param("time") Date time, @Param("text") String text);

    @Modifying
    @Transactional
    @Query(value = "insert into " +
            "lib.post_comments(text, time, parent_id, post_id, user_id) " +
            "VALUE(:text, :time, :parentId, :postId, :userId) ",
            nativeQuery = true)
    void saveParentCommentPost(@Param("parentId") int parentId, @Param("postId") int postId, @Param("userId") int userId, @Param("time") Date time, @Param("text") String text);

    @Query("select p.id from PostComment p where p.time = :time")
    int id(@Param("time") Date time);
}
