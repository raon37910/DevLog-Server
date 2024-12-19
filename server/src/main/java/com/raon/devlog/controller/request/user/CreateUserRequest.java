package com.raon.devlog.controller.request.user;

import com.raon.devlog.service.user.CreateUserInfo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
	@Email(message = "이메일 형식이 아닙니다")
	@NotBlank(message = "이메일은 필수 입력 값입니다")
	String email,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하 여야 합니다")
	String password
) {

	public CreateUserInfo toCreateUserInfo() {
		return new CreateUserInfo(email, password);
	}
}
