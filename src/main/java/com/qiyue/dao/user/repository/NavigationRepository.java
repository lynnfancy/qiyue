package com.qiyue.dao.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qiyue.dao.user.entity.NaviUserRefEntity;
import com.qiyue.dao.user.entity.NavigationEntity;

public interface NavigationRepository extends JpaRepository<NavigationEntity, String> {
	@Query(value="SELECT ni FROM NavigationEntity ni,NaviUserRefEntity nr" 
				+ "	WHERE nr.code = ni.code and nr.level >= (SELECT ui.level FROM UserEntity ui WHERE ui.username = ?1)"
				+ "	ORDER BY ni.code ASC")
	List<NavigationEntity> getNaviList(String username);
}