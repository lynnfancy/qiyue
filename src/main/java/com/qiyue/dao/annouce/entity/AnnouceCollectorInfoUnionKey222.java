package com.qiyue.dao.annouce.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Embeddable
public class AnnouceCollectorInfoUnionKey222 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8950202278091599272L;
	
	@GeneratedValue(generator="IdGenerator")
	@GenericGenerator(name="IdGenerator",strategy="com.qiyue.dao.annouce.id.IdGenerator")
	@Column(name="ACI_FLOW_NO")
	private String flowNo;
	
	@Column(name="ACI_AWI_FLOW_NO")
	private String awiFlowNo;

}
