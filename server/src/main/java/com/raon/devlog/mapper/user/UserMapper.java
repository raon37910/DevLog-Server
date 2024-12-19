package com.raon.devlog.mapper.user;

public interface UserMapper {
	UserEntity findByEmail(String email);

	void create(UserEntity user);

	boolean existsByEmail(String email);
}
