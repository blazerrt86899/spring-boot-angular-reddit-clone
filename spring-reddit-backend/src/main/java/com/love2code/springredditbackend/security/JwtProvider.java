package com.love2code.springredditbackend.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.love2code.springredditbackend.exception.SpringRedditException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Service
public class JwtProvider {
	
	 private KeyStore keyStore;
	
	@PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
        	e.printStackTrace();
            throw new SpringRedditException("Exception occurred while loading keystore");
        }

    }
	
	public String generateToken(Authentication authentication) {
		User principal = (User)authentication.getPrincipal();
		return Jwts.builder()
				.setSubject(principal.getUsername())
				.signWith(getPrivateKey())
				.compact();
	}
	
	 private PrivateKey getPrivateKey() {
	        try {
	            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
	        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
	        	e.printStackTrace();
	            throw new SpringRedditException("Exception occured while retrieving public key from keystore");
	        }
	    }
	 
	 public boolean validateToken(String jwt) {
		 Jwts.parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
		 return true;
	 }
	 
	 private PublicKey getPublickey() {
	        try {
	            return keyStore.getCertificate("springblog").getPublicKey();
	        } catch (KeyStoreException e) {
	        	e.printStackTrace();
	            throw new SpringRedditException("Exception occured while " +
	                    "retrieving public key from keystore");
	        }
	    }
	 
	 public String getUsernameFromJwt(String token) {
		 Claims claims = Jwts.parser()
	                .setSigningKey(getPublickey())
	                .parseClaimsJws(token)
	                .getBody();

	        return claims.getSubject();
	    }

}