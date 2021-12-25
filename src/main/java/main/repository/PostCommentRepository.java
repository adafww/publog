package main.repository;

import main.dto.CommentForPostForDto;
import main.model.PostComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCommentRepository extends CrudRepository<PostComment, Integer> {

    @Query("select " +
            "new main.dto.CommentForPostForDto(c.id, c.time, c.text, c.userId.id, tp.tagID.name) " +
            "from PostComment c " +
            "left join Post p on c.postId.id = p.id " +
            "left join Tag2Post tp on c.postId.id = tp.postId.id " +
            "where p.id = :id group by c order by c.time desc ")
    List<CommentForPostForDto> findPostCommentById(@Param("id") int id);
}
