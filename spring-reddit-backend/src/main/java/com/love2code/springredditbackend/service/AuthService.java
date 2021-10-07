package com.love2code.springredditbackend.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.love2code.springredditbackend.dto.AuthenticationResponse;
import com.love2code.springredditbackend.dto.LoginRequest;
import com.love2code.springredditbackend.dto.RegisterRequest;
import com.love2code.springredditbackend.exception.SpringRedditException;
import com.love2code.springredditbackend.model.NotificationEmail;
import com.love2code.springredditbackend.model.User;
import com.love2code.springredditbackend.model.VerificationToken;
import com.love2code.springredditbackend.repository.UserRepository;
import com.love2code.springredditbackend.repository.VerificationTokenRepository;
import com.love2code.springredditbackend.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final UserRepository userRepository;
	
	private final VerificationTokenRepository verificationTokenRepository;
	
	private final MailService mailService;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtProvider jwtProvider;


	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepository.save(user);
		String token = generateVerificationToken(user);
		mailService.sendMail(new NotificationEmail("Please Activate Your account", 
				user.getEmail(), "Thank you for signing up to Spring Reddit, " +
		                "Please click on the below url to activate your account : " +
		                "http://localhost:9271/api/auth/accountVerification/" + token));
	}


	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		
		verificationTokenRepository.save(verificationToken);
		return token;
	}


	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = 
							verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		
		fetchUserAndEnable(verificationToken.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String userName = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(userName).
				orElseThrow(() -> new SpringRedditException("User Not Found with name " + userName));
		user.setEnabled(true);
		userRepository.save(user);
	}


	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate
				(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);
		return new AuthenticationResponse(token, loginRequest.getUsername());
	}

}
