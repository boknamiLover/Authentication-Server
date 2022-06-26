package com.example.jiniCo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

/** [ Spring Security ] 
 * 
 * 0. 사용자가 로그인 페이지에서 ID, Password를 입력하고 서버로 전송
 * 1. Security Context Persistence Filter는 비어있는 Security Context 객체를 생성하고, 
 *    Security Context Holder에 집어넣는다.
 * 2. 다음으로 Logout Filter로 이동하지만 아무것도 하지 않는다.
 * 3. 다음으로 Authentication Filter로 이동한다. 인증을 위해 Authentication Manager라고 부르는 인터페이스에 해당 요청을 위힘한다.
 * 4. Authentication Manager는 authenticate라는 메소드를 가지고 있으며, 해당 메소드의 argument는 Authentication클래스이고
 *    반환값 역시 Authentication이다.0
 * 5. Authentication Manager는 이러한 인증 객체를 직접 생성하지 않는다.
 *    Authentication Manager는 다시 Authentication Provider에게 해당 인증 객체 생성을 위힘한다.
 *    이 Provider들이 실제로 인증을 진행한다.
 * 6. Authentication Provider는 이름, 비밀번호와 같은 유저 정보를 필요로 하는데, Authentication Provider는 
 *    User Detail Service에게 해당 유저 정보를 가져다 줄 것을 위힘한다.
 * 7. User Detail Service는 DB에 접근하여 해당 유저의 인증 정보를 가져오고 User Details 객체를 
 *    Authentication Provider에게 반환해준다.
 * 8. User Detail Service는 인터페이스이고 그 구현체로는 InMemoryUserDetailsManager, JdbcUserDetailsService, LdapUserDetailManager가 있다.
 *    기본값은 InMemoryUserDetailsManager.
 * 9. Authentication Filter는 유저 인증이 성공했으므로 Security Context에게 Authentication 객체로 채우며
 *    Authentication은 Principal과 Authorities를 채운다.
 * 10. Security Context는 thread local에서 관리되기 때문에 하나의 요청에 대한 진행이라면 어디서든 이 인증 정보를 가져다 쓸 수 있다. 
 */

/**
 * Spring Security 5.7이상에서 더이상 WebSecurityConfigurerAdapter사용을 권장하지 않는다.
 * [ 해결 방안 ]
 * SecurityFilterChain Bean등록을 한다.
 * 
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */

/** @EnableWebSecurity
 * - @Configuration
 * - @Import( {WebSecurityConfiguration.class, SpringWebMvcImportSelector.class, OAuth2ImportSelector.class} )
 * - SpringSecurityFilterChain자동 생성
 * - permitAll     : 무조건 접근을 허용
 * - authenticated : 인증된 사용자의 접근을 허용
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	/* [ AuthenticationManager ] 
	 * user의 요청을 AuthenticationFilter에서 Authentication객체로 변환해 
	 * AuthenticationManager(ProviderManager)에게 넘겨주고, 
	 * AuthenticationProvider(DaoAuthenticationProvider)가 실제 인증을 한 이후에 
	 * 인증이 완료되면 Authentication객체를 반환해준다.
	 */
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		/* [ SpringSecurity Session 정책 ] 
		 * - ALWAYS      : 항상 세션 생성 
		 * - IF_REQUIRED : 필요시 생성
		 * - NAVER       : 생성하지 않지만, 기존에 존재하면 사용 
		 * - STATELESS   : 생성하지 않고, 기존 존재와 상관없이 미사용, JWT토큰방식 사용할 경우 지정
		 */
		
		http.httpBasic().disable()
		    .csrf().disable()		// 세션을 사용하지 않고 JWT토큰을 활용하여 진행, csrf토큰검사를 비활성화
		    .cors().and()
		    .headers().frameOptions().disable().and()
		    .authorizeRequests()	// 인증절차에 대한 설정을 진행
		    .antMatchers("").permitAll()		// 설정된 url은 인증되지 않더라도 누구든 접근 가능
		    .anyRequest().authenticated().and()	// 위 페이지외 인증이 되어야 접근 가능 (ROLE에 상관없이 로그인 요구)
		    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()	// 세션 정책
		    .formLogin().loginPage("/login")	// 접근이 차단된 페이지 클릭 시 이동할 url
		    .loginProcessingUrl("/loginProc")	// 로그인 시 맵핑되는 url
		    .usernameParameter("userId")		// form태그 내에 로그인할 id에 맵핑되는 필드 name
		    .passwordParameter("password")		// form태그 내에 로그인할 password에 맵핑되는 필드 name
//		    .successHandler(successHandler())
//		    .failureUrl(failureHandler())
		    .permitAll().and()
		    .logout()							// 로그아웃 설정
		    .logoutUrl("/logout")				// 로그아웃 시 맵핑되는 url
		    .logoutSuccessUrl("/")				// 로그아웃 성공 시 리다이렉트 url
		    .invalidateHttpSession(true);		// 세션 clear
		
		return http.build();
	}
	
}
