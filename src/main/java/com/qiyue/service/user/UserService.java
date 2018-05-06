package com.qiyue.service.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.qiyue.dao.user.entity.NavigationEntity;
import com.qiyue.dao.user.entity.UserEntity;
import com.qiyue.dao.user.repository.NavigationRepository;
import com.qiyue.dao.user.repository.UserRepository;
import com.qiyue.node.Node;
import com.qiyue.node.NodeTree;
import com.qiyue.redis.RedisHandler;

@Service
public class UserService {
	@Autowired
	private RedisHandler rh;
	
	@Autowired
	private UserRepository ur;
	@Autowired
	private NavigationRepository nr;
	
	public UserEntity checkLogin(String username,String password) {
		return ur.findByUsernameAndPassword(username,password);
	}
	
	public Node navi(){
		return (Node)rh.get("naviNode");
	}
}
