package com.love2code.springredditbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.love2code.springredditbackend.model.Vote;
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
