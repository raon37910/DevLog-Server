package com.raon.devlog.mapper.user;

import java.util.List;

public interface UserMapper {
	UserEntity findByEmail(String email);

	void create(UserEntity user);

	boolean existsByEmail(String email);

	List<RoleEntity> getRoles(Long userId);
}
