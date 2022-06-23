package com.example.jiniCo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* @Data 
 * - @ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor를 한번에 사용
 * @AllArgsConstructor
 * - 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성
 * @NoArgsConstructor
 * - 빈 파라메터 생성자
 * @Entity
 * - 이 클래스는 JPA가 관리. DB테이블 엔티티로 맵핑
 * @Repository
 * - DB테이블 CRUD명령을 자동으로 생성
 * @Id
 * - 해당 테이블의 PK
 */

/**
 * 가입한 회원정보를 저장한 테이블
 */
@Getter
@Setter
@Entity(name="MEMBER")
@AllArgsConstructor
@NoArgsConstructor
public class H2Member {
	
	@Id
	@Column(name="USER_ID", length=15,  unique=true, nullable=false)
	String userId;		// 아이디
	@Column(length=15, nullable=false)
	String password;	// 패스워드
	@Column(length=10, nullable=false)
	String name;		// 이름
	@Column(name="REG_NO", length=14, nullable=false)
	String regNo;		//주민등록번호
}
