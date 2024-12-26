package com.raon.devlog.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raon.devlog.controller.auth.request.SigninRequest;
import com.raon.devlog.controller.auth.response.SinginResponse;
import com.raon.devlog.service.auth.AuthService;
import com.raon.devlog.service.auth.model.Token;
import com.raon.devlog.support.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/token")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<SinginResponse> signin(@Valid @RequestBody SigninRequest request) {
		Token token = authService.generateToken(request.toSigninRequestInfo());
		return ApiResponse.success(
			new SinginResponse(token.accessToken(), token.refreshToken())
		);
	}
}
