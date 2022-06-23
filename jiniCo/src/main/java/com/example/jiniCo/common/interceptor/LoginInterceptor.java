package com.example.jiniCo.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.jiniCo.common.annotation.Login;
import com.example.jiniCo.common.exception.LoginException;
import com.example.jiniCo.common.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor{
	
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	@Override
	public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		/* ===============================
		   [ 처리 순서 ]
		    1. @Login annotation유무 체크
		    2. JWT유무 체크
		    3. 토큰의 유효성 체크
		   =============================== */
		
		// @Login annotation유무 체크
		if ( checkAnnotation(handler, Login.class) ) {
			
			// JWT유무 체크
			String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
			if ( authorization.isBlank() ) {
				// 헤더에 Authorization이 없을때 
				log.info("### LoginInterceptor : no Authorization");
				throw new LoginException();
				
			} else {
				
				try {
					
					String token = authorization.split(" ")[1];
					if ( token.isBlank() ) {
						// 토큰값이 세팅되어 있지 않을 때
						log.info("### LoginInterceptor : no token ");
						throw new LoginException();
						
					} else {
						
						if ( JwtUtil.verifyToken(token, secretKey) ) {
							// 토큰이 유효하면
							return true;
							
						} else {
							// 토큰이 유효하지 않음 (유효기간 만료, 변조됨 등)
							log.info("### LoginInterceptor : unusable token");
							throw new LoginException();
						}
					}
				} catch (Exception e) {
					throw new LoginException();
				}
				
			}
		}
		return true;
	}

	
	/**
	 * '@Login' 어노테이션 유무 체크
	 * 
	 * @param handler
	 * @param cl
	 * @return
	 */
	private boolean checkAnnotation(Object handler, Class cl) {
		
		// annotation 유무 체크
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		if (   handlerMethod.getMethodAnnotation(cl) != null
			|| handlerMethod.getBeanType().getAnnotation(cl) !=null ) {
			// "@Login" annotation이 있을 때
			 return true;
		}
		return false;
	}
}
