package com.love2code.springredditbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.love2code.springredditbackend.model.Post;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
