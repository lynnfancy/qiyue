package com.qiyue.dao.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.stereotype.Indexed;

import lombok.Data;

@Data
@Table(name="USER_NAVIGATION_REF")
@Entity
public class NaviUserRefEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9751936862984979L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UNR_FLOW_NO")
	private String flowNo;
	
	@Column(name="UNR_USER_LEVEL")
	private String level;
	
	@Column(name="UNR_UNI_CODE")
	private String code;
}
