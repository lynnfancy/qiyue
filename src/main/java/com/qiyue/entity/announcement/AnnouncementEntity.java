package com.qiyue.entity.announcement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="ANNOUNCEMENT_COLLECTOR_RESULT")
@Entity
public class AnnouncementEntity {
//	@Id
//	@GeneratedValue(generator  = "myIdStrategy")
//	@GenericGenerator(name = "myIdStrategy", strategy = "uuid")
//	@Column(name="ACR_FLOW_NO")
//	private String flowNo;
	@Id
	@Column(name="ACR_URL")
	private String url;
	
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

}
