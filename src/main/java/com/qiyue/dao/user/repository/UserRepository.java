package com.qiyue.dao.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qiyue.dao.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	UserEntity findByUsernameAndPassword(String username,String password);
}
