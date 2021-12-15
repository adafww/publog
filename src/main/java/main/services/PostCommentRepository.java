package main.services;

import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {
}
