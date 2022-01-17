package main.repository;

import main.dto.CommentForPostForDto;
import main.model.PostComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends CrudRepository<PostComment, Integer> {

    @Query("select " +
            "new main.dto.CommentForPostForDto(c.id, c.time, c.text, c.userId.id) " +
            "from PostComment c " +
            "left join Post p on c.postId.id = p.id " +
            "where p.id = :id group by c order by c.time desc ")
    List<CommentForPostForDto> findPostCommentById(@Param("id") int id);

    @Query("select p " +
            "from PostComment pc " +
            "left join Post p on pc.postId.id = p.id " +
            "where pc.postId.id = :id")
    main.model.Post findPostById(@Param("id") int id);

    @Query("select pc " +
            "from PostComment pc " +
            "where pc.id = :id")
    PostComment findById(@Param("id") int id);
}
