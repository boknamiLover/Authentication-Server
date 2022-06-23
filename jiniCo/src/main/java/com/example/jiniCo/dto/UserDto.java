package com.example.jiniCo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="회원가입 가능 유저 정보")
public class UserDto {

	@ApiModelProperty(value="이름")
	private String name;
	@ApiModelProperty(value="주민번호")
	private String regNo;
}
