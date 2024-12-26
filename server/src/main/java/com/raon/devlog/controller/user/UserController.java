package com.raon.devlog.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raon.devlog.controller.user.request.CreateUserRequest;
import com.raon.devlog.controller.user.response.CreateUserResponse;
import com.raon.devlog.service.user.UserInfo;
import com.raon.devlog.service.user.UserService;
import com.raon.devlog.support.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
		UserInfo createdUser = userService.createUser(request.toCreateUserInfo());
		return ApiResponse.success(CreateUserResponse.from(createdUser));
	}
}
