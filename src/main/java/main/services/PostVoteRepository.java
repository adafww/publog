package main.services;

import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PostVoteRepository extends CrudRepository<PostVote, Integer> {
}
