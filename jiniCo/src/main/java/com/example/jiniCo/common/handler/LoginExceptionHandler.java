package com.example.jiniCo.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.jiniCo.common.exception.LoginException;

@RestControllerAdvice
public class LoginExceptionHandler {
	
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<String> loginException() {
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 서비스 입니다.");
	}
}
