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
@ApiModel(value="로그인 정보")
@ToString
public class LoginDto {
	
	@ApiModelProperty(value="아이디", example="hong12")
	private String userId;
	
	@ApiModelProperty(value="패스워드", example="123456")
	private String password;
}
