package com.example.jiniCo.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.jiniCo.dto.LoginDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JwtUtil {

	/**
	 * JWT 생성
	 * 
	 * @param inDto
	 * @param secretKey
	 * @return
	 */
	public static String makeToken (LoginDto inDto, String secretKey, int expireMinute) {
		
		Date now  = new Date();
		Long expireTime = now.getTime() + ( expireMinute * 60 * 1000 );
		
		// Header
		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put(Header.TYPE, Header.JWT_TYPE);
		headerMap.put("alg", "HS256");
		
		// PayLoad
		Map<String, Object> payLoadMap = new HashMap<String, Object>();
		payLoadMap.put("id", inDto.getUserId());	// 아이디
		
		String token = Jwts.builder()
						   .setHeader(headerMap)
						   .setIssuedAt(now)
						   .setClaims(payLoadMap)
						   .setExpiration(new Date(expireTime))
						   .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
						   .compact();
		
		return token;
	}
	
	
	/**
	 * Claim 추출
	 * 
	 * @param token
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	public static Claims getClaim(String token, String secretKey) throws Exception {
		return Jwts.parser()
				.setSigningKey(secretKey.getBytes("UTF-8"))
				.parseClaimsJws(token)
				.getBody();
	}
	
	
	/**
	 * JWT 검증
	 * 
	 * @param token
	 * @param secretKey
	 * @return
	 */
	public static boolean verifyToken(String token, String secretKey) {
		
		boolean result = false;
		
		try { 
			Claims claims = getClaim(token, secretKey);
			
			log.info("##### verifyToken ##### id = {}, expireTime = {}", claims.get("id"), claims.getExpiration());
			result = true;
			
		} catch (ExpiredJwtException e) {
			log.info("###### verifyToken ##### 토큰 만료");
			result = false;
			
		} catch (JwtException j) {
			log.info("###### verifyToken ##### 토큰 변조");
			result = false;
		} catch (Exception ex) {
			log.info("###### verifyToken ##### 그 외 에러");
			return false;
		}
		
		return result;
	}
	
	
	/**
	 * 토큰의 payload에서 사용자아이디 추출
	 * 
	 * @param token
	 * @param secretKey
	 * @return
	 */
	public static String getUserId(String token, String secretKey)  throws Exception{
		
		return (String) getClaim(token, secretKey).get("id");
	}
	
	
	/**
	 * 토큰 만료 체크
	 * 
	 * @param token
	 * @param secretKey
	 * @return
	 * @throws Exception 
	 */
	public boolean isExpiredToken(String token, String secretKey) throws Exception {
		
		return getClaim(token, secretKey).getExpiration().before(new Date());
	}

}
