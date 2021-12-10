package main.services;

import main.model.PostComment;
import org.springframework.data.repository.CrudRepository;

public interface PostCommentRepository extends CrudRepository<PostComment, Integer> {
}
