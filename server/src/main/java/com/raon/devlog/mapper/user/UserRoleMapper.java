package com.raon.devlog.mapper.user;

public interface UserRoleMapper {
	void defaultUser(Long userId, Long roleId);

	Long getRoleId(String roleName);
}
