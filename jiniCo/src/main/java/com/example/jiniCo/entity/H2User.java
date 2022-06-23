package com.example.jiniCo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 회원가입 가능 유저 정보를 저장한 테이블
 */

@Getter
@Setter
@Entity(name="PERSONS")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="유저정보", description="이름, 주민번호")
public class H2User {
	
	@Id
	@Column(name="REG_NO")
	String regNo;		//주민등록번호
	
	String name;		// 이름
}
