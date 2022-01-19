package main.repository;

import main.dto.ApiStatisticsDto;
import main.dto.PostCalendarDtoRepository;
import main.dto.PostForDtoRepository;
import main.model.ModerationStatusType;
import main.model.Post;
import main.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    Post findByTitle(String title);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "group by p order by p.time desc")
    List<PostForDtoRepository> findRecent(PageRequest of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "group by p.id order by count (c.postId) desc")
    List<PostForDtoRepository> findPopular(PageRequest of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "and (select count(v) from v where v.value = false and v.post.id = p.id) = 0 " +
            "or v.value is null " +
            "group by p order by sum(case when v.value = true then 1 else 0 end) desc")
    List<PostForDtoRepository> findBest(PageRequest of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "group by p order by p.time asc")
    List<PostForDtoRepository> findEarly(PageRequest of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "left join User u on u = p.user " +
            "where p.moderationStatusType='ACCEPTED' " +
            "and p.isActive = true " +
            "and p.title like :query " +
            "or p.text like :query " +
            "or u.name like :query " +
            "group by p order by p.time DESC ")
    List<PostForDtoRepository> findPostSearch(@Param("query") String query, Pageable of);

    @Query("select " +
            "new main.dto.PostCalendarDtoRepository(p.time, count(p.time)) " +
            "from Post p " +
            "where p.moderationStatusType='ACCEPTED' " +
            "and p.isActive = true " +
            "and substring(p.time, 1, 4) like :year     " +
            "group by p.time order by count (p.time) DESC ")
    List<PostCalendarDtoRepository> findDate(@Param("year") String year, Pageable of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "and p.isActive = true " +
            "and substring(p.time, 1, 10) like :date " +
            "group by p order by count (p.time) DESC ")
    List<PostForDtoRepository> findByDate(@Param("date") String date, Pageable of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "left join Tag2Post t on p.id = t.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "and p.isActive = true " +
            "and t.tagID.name = :tag " +
            "group by p order by count (p.time) DESC ")
    List<PostForDtoRepository> findByTag(@Param("tag") String tag, Pageable of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "left join Tag2Post t on p.id = t.postId.id " +
            "where p.id = :id " +
            "group by p order by count (p.time) DESC")
    List<PostForDtoRepository> findPostId(@Param("id") int id);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "left join Tag2Post t on p.id = t.postId.id " +
            "where p.isActive = false " +
            "and p.user.email like :email " +
            "group by p order by count (p.time) DESC")
    List<PostForDtoRepository> findInactiveByEmail(@Param("email") String email, Pageable of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "left join Tag2Post t on p.id = t.postId.id " +
            "where p.moderationStatusType='NEW' " +
            "and p.isActive = true " +
            "and p.user.email like :email " +
            "group by p order by count (p.time) DESC")
    List<PostForDtoRepository> findPendingByEmail(@Param("email") String email, Pageable of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "left join Tag2Post t on p.id = t.postId.id " +
            "where p.moderationStatusType='DECLINED' " +
            "and p.isActive = true " +
            "and p.user.email like :email " +
            "group by p order by count (p.time) DESC")
    List<PostForDtoRepository> findDeclinedByEmail(@Param("email") String email, Pageable of);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "left join Tag2Post t on p.id = t.postId.id " +
            "where p.moderationStatusType='ACCEPTED' " +
            "and p.isActive = true " +
            "and p.user.email like :email " +
            "group by p order by count (p.time) DESC")
    List<PostForDtoRepository> findPublishedByEmail(@Param("email") String email, Pageable of);

    @Modifying
    @Transactional
    @Query("update Post p " +
            "set p.viewCount = p.viewCount + 1 " +
            "where p.id = :postId ")
    void incrementViewById(@Param("postId") int postId);

    @Query("select " +
            "case when count (p) > 0 then true else false end " +
            "from Post p " +
            "left join User u on p.user.id = u.id " +
            "where p.id = :postId " +
            "and u.email like :email")
    boolean isAuthor(@Param("postId") int postId, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("update Post p " +
            "set p.time = :time, p.isActive = :active, p.title = :title, p.text = :text, p.moderationStatusType = 'NEW' " +
            "where p.id = :postId ")
    void postUpdate(@Param("postId") int postId,
                    @Param("time") Date time,
                    @Param("active") boolean active,
                    @Param("title") String title,
                    @Param("text") String text);

    @Modifying
    @Transactional
    @Query("update Post p " +
            "set " +
            "p.Moderator = (select m from User m where m.email like :modEmail), " +
            "p.time = :time, " +
            "p.isActive = :active, " +
            "p.title = :title, " +
            "p.text = :text, " +
            "p.moderationStatusType = 'NEW' " +
            "where p.id = :postId ")
    void postModUpdate(@Param("modEmail") String modEmail,
                    @Param("postId") int postId,
                    @Param("time") Date time,
                    @Param("active") boolean active,
                    @Param("title") String title,
                    @Param("text") String text);

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "where p.moderationStatusType='NEW' " +
            "group by p order by count (p.time) DESC")
    List<PostForDtoRepository> findNewActivePosts();

    @Query("select " +
            "new main.dto.PostForDtoRepository(p.id, p.time, p.user.id, p.title, p.text, " +
            "(select count(v) from v where v.value = true and v.post.id = p.id), " +
            "(select count(v) from v where v.value = false and v.post.id = p.id), " +
            "(select count(c) from c where c.postId.id = p.id), p.viewCount) " +
            "from Post p " +
            "left join PostVote v on p.id = v.post.id " +
            "left join PostComment c on p.id = c.postId.id " +
            "left join User u on p.Moderator.id = u.id " +
            "where p.moderationStatusType = :status " +
            "and u.email like :email " +
            "group by p order by count (p.time) DESC")
    List<PostForDtoRepository> findModerationPosts(@Param("status") ModerationStatusType status, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("update Post p " +
            "set " +
            "p.moderationStatusType = :status " +
            "where p.id = :postId ")
    void moderationStatus(@Param("postId") int postId,  @Param("status") ModerationStatusType status);
}
