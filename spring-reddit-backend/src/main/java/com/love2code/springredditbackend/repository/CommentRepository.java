package com.love2code.springredditbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.love2code.springredditbackend.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
