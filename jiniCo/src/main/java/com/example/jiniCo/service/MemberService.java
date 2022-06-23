package com.example.jiniCo.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.example.jiniCo.common.util.JwtUtil;
import com.example.jiniCo.dto.LoginDto;
import com.example.jiniCo.dto.MemberDto;
import com.example.jiniCo.entity.H2Member;
import com.example.jiniCo.entity.H2User;
import com.example.jiniCo.repository.H2MemberRepository;
import com.example.jiniCo.repository.H2UserRepository;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	
	// JWT 용 시크릿 키
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	// JWT 유효시간 : 30분
	@Value("${jwt.expire.minute}")
	private int expireMinute;
	
	// 회원가입 가능 유저 정보를 저장한 테이블
	private final H2UserRepository userRepository;
	
	// 가입한 회원정보를 저장한 테이블
	private final H2MemberRepository memberRepository;
	
	// Object 필드 맵핑
	private final ModelMapper modelMapper;

	/**
	 * 회원가입 처리를 한다.
	 * 1. 회원가입 가능 유저 체크
	 * 2. 기 회원여부 체크
	 * 3. 아이디 기 사용 여부 체크
	 * 4. 회원가입 처리
	 * 
	 * @param inDto
	 * @return
	 */
	@Transactional
	public Map<String, Object> signUp ( MemberDto inDto ) {
		
		log.info("##### MemberService.signup START MemberDto = {}", inDto.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 회원가입 가능 유저 체크
		Optional<H2User> user = userRepository.findByNameAndRegNo(inDto.getName(), inDto.getRegNo());
		
		if ( user.isPresent() ) {
			
			// 회원여부 확인
			Optional<H2Member> member = memberRepository.findByUserIdAndPassword(inDto.getUserId(), inDto.getPassword());
			
			member.ifPresentOrElse(
					me -> result.put("errorMessage", "이미 가입된 회원입니다."),
					() -> {
						// 이미 사용중인 아이디
						if ( memberRepository.existsById((String) inDto.getUserId()) ) {
							result.put("errorMessage", "이미 사용중인 아이디입니다.");
							
						} else {
							
							// 회원가입 처리
							H2Member memberEntity = modelMapper.map(inDto, H2Member.class);
							memberRepository.save(memberEntity);
							result.put("Message", "성공");
						}
					}
			);
			
		} else {
			result.put("errorMessage", "회원가입이 가능한 유저가 아닙니다.");
		}

		log.info("##### MemberService.signup END result = {}", result.toString());
		return result;
	}
	
	
	/**
	 * 로그인 처리를 한다.
	 * 1. 회원여부 확인
	 * 2. JWT발급
	 * 
	 * @param inDto
	 * @return
	 */
//	@Transactional
	public Map<String, Object> getToken ( LoginDto inDto ) {
		
		log.info("##### MemberService.getToken START LoginDto = {}", inDto.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 회원여부 확인
		Optional<H2Member> member = memberRepository.findByUserIdAndPassword(inDto.getUserId(), inDto.getPassword());
		
		// 회원일경우, 토큰발급
		member.ifPresentOrElse(
				me -> {
					String jwt = JwtUtil.makeToken(inDto, secretKey, expireMinute);
					result.put("token", jwt);
				},
				() -> result.put("errorMessage", "정보를 찾을 수 없습니다. 아이디와 패스워드를 확인해주세요.")
		);
			

		log.info("##### MemberService.getToken END result = {}", result.toString());
		return result;
	}
	

	/**
	 * jwt에서 추출한 사용자ID로 회원정보를 조회한다.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public MemberDto getUserInfo ( HttpServletRequest request ) throws Exception {
		
		log.info("##### MemberService.getUserInfo START");
		
		// jwt에서 사용자ID 추출
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token = authorization.split(" ")[1];
		String userId = JwtUtil.getUserId(token, secretKey);
		
		log.info("##### MemberService.getUserInfo userId = {}", userId);
		
		// 회원정보 조회
		Optional<H2Member> member = memberRepository.findById(userId);
		
		MemberDto result = modelMapper.map(member, MemberDto.class);
		
		log.info("##### MemberService.getUserInfo END result = {}", result.toString());
		return result;
	}
	
	
//	/**
//	 * EntityToDto
//	 * 
//	 * @param member
//	 * @return
//	 */
//	private MemberDto converToDto (H2Member member) {
//		MemberDto memberDto = modelMapper.map(member, MemberDto.class);
//		return memberDto;
//	}
//	
//	/**
//	 * DtoToEntity
//	 * @param member
//	 * @return
//	 */
//	private H2Member convertToEntity (MemberDto member) {
//		H2Member memberEntity = modelMapper.map(member,  H2Member.class);
//		return memberEntity;
//	}
}
