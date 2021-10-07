package com.love2code.springredditbackend.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.love2code.springredditbackend.dto.SubredditDto;
import com.love2code.springredditbackend.exception.SpringRedditException;
import com.love2code.springredditbackend.mapper.SubredditMapper;
import com.love2code.springredditbackend.model.Subreddit;
import com.love2code.springredditbackend.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
	
	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		log.info("Saving Subreddit :: Service");
		Subreddit saveSubreddit = subredditRepository
				.save(subredditMapper.mapDtoToSubreddit(subredditDto));
		subredditDto.setId(saveSubreddit.getId());
		return subredditDto;
	}

	@Transactional
	public List<SubredditDto> getAllSubredditService() {
		log.info("Getting all subreddit in List :: Service");
		return subredditRepository.findAll()
				.stream()
				.map(subredditMapper::mapSubredditToDto)
				.collect(Collectors.toList());
	}

	public SubredditDto getSubredditByIdService(Long id) {
		log.info("Getting Subreddit By Id " + id + " :: Service");
		Subreddit subreddit =  subredditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("SubReddit Not Found with id " + id));
		return subredditMapper.mapSubredditToDto(subreddit);
	}
	
//	private SubredditDto mapToDto(Subreddit subreddit) {
//		return SubredditDto.builder().subredditName(subreddit.getName())
//				.id(subreddit.getId())
//				.numberOfPosts(subreddit.getPosts().size())
//				.build();
//	}
//	
//	private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//		return Subreddit.builder().name(subredditDto.getSubredditName())
//			.description(subredditDto.getDescription())
//			.build();
//	}

	

}
