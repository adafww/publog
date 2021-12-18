package main.repository;

import main.dto.PostForDtoRepository;
import main.model.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "inner join PostVote v on p = v.post " +
            "inner join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "group by p order by p.time desc")
    List<PostForDtoRepository> findRecent(PageRequest of);
    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "inner join PostVote v on p.id = v.post.id " +
            "inner join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "group by p.id order by count (c.postId) desc")
    List<PostForDtoRepository> findPopular(PageRequest of);
    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "inner join PostVote v on p.id = v.post.id " +
            "inner join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' and v.value = true " +
            "group by p.id order by count (v.post) desc")
    List<PostForDtoRepository> findBest(PageRequest of);
    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "inner join PostVote v on p = v.post " +
            "inner join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "group by p order by p.time asc")
    List<PostForDtoRepository> findEarly(PageRequest of);
}
