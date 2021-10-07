package com.love2code.springredditbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.love2code.springredditbackend.dto.SubredditDto;
import com.love2code.springredditbackend.service.SubredditService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubRedditController {
	
	private final SubredditService subredditService;
	
	@PostMapping
	public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
		log.info("Creating Subreddit :: Controller");
		return ResponseEntity.status(HttpStatus.CREATED)
						.body(subredditService.save(subredditDto));
		
	}
	
	@GetMapping
	public ResponseEntity<List<SubredditDto>> getAllSubreddits(){
		log.info("Getting All Subreddit :: Controller");
		return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAllSubredditService());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SubredditDto> getSubredditById(@PathVariable("id") Long id){
		log.info("Getting Subreddit By Id " + id + " :: Controller");
		return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubredditByIdService(id));
	}

}
