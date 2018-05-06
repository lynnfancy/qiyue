package com.qiyue.dao.annouce.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.qiyue.util.DateUtil;

import lombok.Data;

@Data
@Table(name="ANNOUCE_WEBSITE_INFO")
@Entity
@DynamicUpdate(true)
public class AnnouceWebsiteInfo implements Serializable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 5123109159240040088L;

	@Id
	@GeneratedValue(generator="IdGenerator")
	@GenericGenerator(name="IdGenerator",strategy="com.qiyue.dao.annouce.id.IdGenerator")
	@Column(name="AWI_FLOW_NO")
	private String flowNo; 
	
	@Column(name="AWI_WEBSITE_CODE")
	private String websiteCode;
	
	@Column(name="AWI_WEBSITE_URL")
	private String websiteUrl;
	
	@Column(name="AWI_WEBSITE_NAME")
	private String websiteName;
	
	@Column(name="AWI_TITLE_REGEX")
	private String titleRegex;
	
	@Column(name="AWI_ENCODE")
	private String encode;
	
	@Column(name="AWI_STATE")
	private String state;
	
	@Column(name="AWI_CREATE_TIME")
	private String createTime;
	
	@Column(name="AWI_CREATE_USERNAME")
	private String createUsername;
	
	@Column(name="AWI_MODIFY_TIME")
	private String modifyTime;
	
	@Column(name="AWI_MODIFY_USERNAME")
	private String modifyUsername;
	
	@Transient
	private List<AnnouceCollectorInfo> aciList = new ArrayList<AnnouceCollectorInfo>();
	
	/*
	 *  实体类代码 
	 *  生成自定义ID时需要 
	 */
	@Transient
	public static final String ENTITY_CODE= "AWI";
	
}
