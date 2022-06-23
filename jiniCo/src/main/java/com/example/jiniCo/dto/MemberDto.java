package com.example.jiniCo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="회원가입 정보")
@ToString
public class MemberDto {

	@ApiModelProperty(value="아이디", example="hong12")
	private String userId;
	
	@ApiModelProperty(value="패스워드", example="123456")
	private String password;

	@ApiModelProperty(value="이름", example="홍길동")
	private String name;
	
	@ApiModelProperty(value="주민번호", example="860824-1655068")
	private String regNo;
	
}
