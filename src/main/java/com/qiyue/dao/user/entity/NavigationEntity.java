package com.qiyue.dao.user.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Indexed;

import com.qiyue.node.Element;

import lombok.Data;

@Data
@Table(name="USER_NAVIGATION_INFO")
@Entity
public class NavigationEntity implements Element,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2796297496133610353L;

	@Id
	@Column(name="UNI_CODE")
	private String code;
	
	@Column(name="UNI_NAME")
	private String name;
	
	@Column(name="UNI_SUPER_CODE")
	private String supCode;
	
	@Column(name="UNI_URL")
	private String url;
	
	
}
