package com.qiyue.dao.annouce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Table(name="ANNOUCE_COLLECTOR_RESULT")
@Entity
public class AnnouceCollectorResult {
//	@Id
//	@GeneratedValue(generator="IdGenerator")
//	@GenericGenerator(name="IdGenerator",strategy="com.qiyue.dao.annouce.id.IdGenerator")
//	@Column(name="ACR_FLOW_NO")
//	private String flowNo;
	
	@Id
	@Column(name="ACR_URL")
	private String url;
	
	@Column(name="ACR_AWI_FLOW_NO")
	private String awiFlowNo;
	
	@Column(name="ACR_ACI_FLOW_NO")
	private String aciFlowNo;
	
	@Column(name="ACR_URL_TITLE")
	private String title;
	
	@Column(name="ACR_URL_TEXT")
	private String text;

	@Column(name="ACR_PUBLISH_TIME")
	private String publishTime;
	
	@Column(name="ACR_SUP_URL")
	private String supUrl;
	
	@Column(name="ACR_CATEGORY_CODE")
	private String categoryCode;
	
	@Column(name="ACR_CATEGORY_NAME")
	private String categoryName;

	@Column(name="ACR_WEBSITE_CODE")
	private String websiteCode;
	
	@Column(name="ACR_WEBSITE_NAME")
	private String websiteName;

	@Column(name="ACR_RECORD_TIME")
	private String recordTime;

	/*
	 *  实体类代码 
	 *  生成自定义ID时需要 
	 */
	@Transient
	public static final String ENTITY_CODE= "ACR";
}
