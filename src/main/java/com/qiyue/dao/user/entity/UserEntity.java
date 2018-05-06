package com.qiyue.dao.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="USER_USER_INFO")
public class UserEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3237705433860906626L;

	@Id
	@Column(name="UUI_PHONE_NO")
	private String phoneNo;
	
	@Column(name="UUI_USERNAME")
	private String username;
	
	@Column(name="UUI_NAME")
	private String name;
	
	@Column(name="UUI_GENDER")
	private String gender;
	
	@Column(name="UUI_IDENTIFICATION_TYPE")
	private String identificationType;
	
	@Column(name="UUI_IDENTIFICATION_NO")
	private String identificationNo;
	
	@Column(name="UUI_NATIVE_ADDRESS")
	private String nativeAddress;
	
	@Column(name="UUI_CURRENT_ADDRESS")
	private String currentAddress;
	
	@Column(name="UUI_PASSWORD")
	private String password;
	
	@Column(name="UUI_OPENID")
	private String openid;
	
	@Column(name="UUI_LEVEL")
	private String level;
	
	@Column(name="UUI_STATE")
	private String state;

	public UserEntity get() {
		return new UserEntity();
	}
}
