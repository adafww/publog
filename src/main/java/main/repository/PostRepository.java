package main.repository;

import main.model.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("select p from Post p, PostComment c inner join c.postId on p = c.postId where p.moderationStatusType='ACCEPTED' group by c.postId order by count (c.postId) desc")
    List<Post> findPopular(PageRequest of);
    @Query("select p from Post p, PostVote v inner join v.post on p = v.post where p.moderationStatusType='ACCEPTED' and v.value = true group by v.value order by count (v.post) desc")
    List<Post> findBest(PageRequest of);
    @Query("select p from Post p where p.moderationStatusType='ACCEPTED'")
    List<Post> findRecentEarly(PageRequest time);
}
