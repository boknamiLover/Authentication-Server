package com.example.jiniCo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jiniCo.entity.H2Member;
import com.example.jiniCo.entity.H2User;
/* Repository
 * - JPA의 DB Layer접근자
 * - 인터페이스를 생성 후 JpaRepository<Entity클래스, PK타입>을 상속하면 기본적인 CRUD메소드가 자동으로 생성된다.
 * - @Repository를 추가할 필요없다.
 *   - xxxRepository 인터페이스가 JpaRepository인터페이스를 상속하면 해당 인터페이스의 구현체인 org.springframework.data.jpa.repository.support.SimpleJpaRepository에서
 *   @Repository로 스프링 컨테이너가 관리하는 빈이 되기 때문에, 우리가 만든 Repository에서 @Repository어노테이션을 추가하지 않아도 된다.
 */
public interface H2MemberRepository extends JpaRepository<H2Member, String>{

	// 아이디와 비밀번호로 검색
	Optional<H2Member> findByUserIdAndPassword(String userId, String password);
}