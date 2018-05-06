package com.qiyue.dao.annouce.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name="ANNOUCE_COLLECTOR_INFO")
public class AnnouceCollectorInfo  implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1241774569122651463L;

	@Id
	@GeneratedValue(generator="IdGenerator")
	@GenericGenerator(name="IdGenerator",strategy="com.qiyue.dao.annouce.id.IdGenerator")
	@Column(name="ACI_FLOW_NO")
	private String flowNo;
	
	@Column(name="ACI_AWI_FLOW_NO")
	private String awiFlowNo;
	
	@Column(name="ACI_CATEGORY_CODE")
	private String categoryCode;
	
	@Column(name="ACI_CATEGORY_NAME")
	private String categoryName;

	@Column(name="ACI_TITLE_URL_WHITE")
	private String titleUrlWhite;
	
	@Column(name="ACI_TEXT_URL_REGEX_WHITE")
	private String textUrlRegexWhite;
	
	@Column(name="ACI_TEXT_URL_REGEX_BLACK")
	private String textUrlRegexBlack;
	
	@Column(name="ACI_PUBLISH_TIME_SELECTOR")
	private String publishTimeSelector;
	
	@Column(name="ACI_TITLE_SELECTOR")
	private String titleSelector;
	
	@Column(name="ACI_TEXT_SELECTOR")
	private String textSelector;
	
	@Column(name="ACI_STATE")
	private String state;
	
	@Column(name="ACI_CREATE_TIME")
	private String createTime;
	
	@Column(name="ACI_CREATE_USERNAME")
	private String createUsername;
	
	@Column(name="ACI_MODIFY_TIME")
	private String modifyTime;
	
	@Column(name="ACI_MODIFY_USERNAME")
	private String modifyUsername;
	
	/*
	 *  实体类代码 
	 *  生成自定义ID时需要 
	 */
	@Transient
	public static final String ENTITY_CODE= "ACI";
	
}
