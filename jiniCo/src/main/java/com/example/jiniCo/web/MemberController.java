package com.example.jiniCo.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jiniCo.common.annotation.Login;
import com.example.jiniCo.common.util.JwtUtil;
import com.example.jiniCo.dto.LoginDto;
import com.example.jiniCo.dto.MemberDto;
import com.example.jiniCo.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mem")
@Api(tags = {"과제 API"} )
public class MemberController {
	
	private final MemberService memberService;
	
	/**
	 * 회원가입
	 * 
	 * @param inDto (userId, password, name, regNo)
	 * @return
	 */
	@PostMapping(value = "/signup")
	@ApiOperation(value="회원가입")
	@ApiResponses({
		@ApiResponse(code=200, message="성공"),
		@ApiResponse(code=500, message="에러")
	})
	public ResponseEntity<Map<String, Object>> signup(@RequestBody MemberDto inDto) {
		
		log.info("##### signup START MemberDto = {}", inDto.toString());
		
		Map<String, Object> result = new HashMap<>();
		
		// 필수 파라미터 체크
		if ( inDto.getUserId().isBlank() ) {
			result.put("errorMessage", "아이디를 확인해 주세요. userId = " + inDto.getUserId());
			return ResponseEntity.internalServerError().body(result);
		}
		if ( inDto.getPassword().isBlank() ) {
			result.put("errorMessage", "비밀번호를 확인해 주세요. password = " + inDto.getPassword());
			return ResponseEntity.internalServerError().body(result);
		}
		if ( inDto.getName().isBlank() ) {
			result.put("errorMessage", "이름을 확인해 주세요. name = " + inDto.getName());
			return ResponseEntity.internalServerError().body(result);
		}
		String regNo = inDto.getRegNo();
		String regExp = "\\d{6}-[1-4]\\d{6}";
		if ( regNo.isBlank() || regNo.length() != 14 || !regNo.matches(regExp)) {
			result.put("errorMessage", "주민번호를 확인해 주세요. regNo = " + regNo);
			return ResponseEntity.internalServerError().body(result);
		}
		
		
		// 회원가입 처리
		result = memberService.signUp(inDto);
		log.info("##### signup END result = {}", result.toString());
		
		
		if ( result.containsKey("Message") ) {
			return ResponseEntity.ok().body(result);
			
		} else {
			// 에러
			return ResponseEntity.internalServerError().body(result);
		}
	}
	
	
	/**
	 * 로그인 ( JWT 발급 )
	 * 
	 * @param inDto (userId, password)
	 * @return
	 */
	@PostMapping(value="/login")
	@ApiOperation(value="로그인 ( JWT 발급 )")
	@ApiResponses({
		@ApiResponse(code=200, message="성공"),
		@ApiResponse(code=500, message="에러")
	})
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto inDto) {
		
		log.info("##### login START LoginDto = {}", inDto.toString());
		
		Map<String, Object> result = new HashMap<>();
		
		// 필수 파라미터 체크
		if ( inDto.getUserId().isBlank() ) {
			result.put("errorMessage", "아이디를 확인해 주세요. userId = " + inDto.getUserId());
			return ResponseEntity.internalServerError().body(result);
		}
		if ( inDto.getPassword().isBlank() ) {
			result.put("errorMessage", "비밀번호를 확인해 주세요. password = " + inDto.getPassword());
			return ResponseEntity.internalServerError().body(result);
		}

		
		// 토큰 발급
		result = memberService.getToken(inDto);
		log.info("##### login END result = {}", result.toString());
		
		
		if ( result.containsKey("token") ) {
			return ResponseEntity.ok().body(result);
			
		} else {
			// 에러
			return ResponseEntity.internalServerError().body(result);
		}
	}
	
	
	/**
	 * 내 정보 보기
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@Login
	@PostMapping(value="/me")
	@ApiOperation(value="내 정보 보기")
	public ResponseEntity<MemberDto> getMyInfo(HttpServletRequest request) throws Exception {
		
		log.info("##### getMyInfo START");
		
		// 회원정보 조회
		MemberDto member = memberService.getUserInfo(request);
		
		log.info("##### getMyInfo END result = {}", member);
		
		return ResponseEntity.ok().body(member);
	}
	
	
	/**
	 * 환급용 정보 스크랩 하기
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Login
	@PostMapping(value="/scrap")
	@ApiOperation(value="스크팹")
	public ResponseEntity<Map<String, Object>> doScrap(HttpServletRequest request) throws Exception {
		
		log.info("##### doScrap START");
		Map<String, Object> result = new HashMap<>();
		
		
		// 회원정보 조회
		MemberDto member = memberService.getUserInfo(request);
		
		
		// TODO 스크랩하기
		
		
		log.info("##### doScrap END result = {}", result.toString());
		
		if ( result.containsKey("Message") ) {
			return ResponseEntity.ok().body(result);
			
		} else {
			// 에러
			return ResponseEntity.internalServerError().body(result);
		}
	}
	
	
	/**
	 * 환급액 계산하기
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Login
	@PostMapping(value="/refund")
	@ApiOperation(value="환급액 계산")
	public ResponseEntity<Map<String, Object>> refund(HttpServletRequest request) throws Exception {
		
		log.info("##### refund START");
		Map<String, Object> result = new HashMap<>();
		
		
		// 회원정보 조회
		MemberDto member = memberService.getUserInfo(request);
		
		// TODO 환급액 계산하기
		
		
		log.info("##### refund END result = {}", result.toString());
		
		if ( result.containsKey("Message") ) {
			return ResponseEntity.ok().body(result);
			
		} else {
			// 에러
			return ResponseEntity.internalServerError().body(result);
		}
	}
}
