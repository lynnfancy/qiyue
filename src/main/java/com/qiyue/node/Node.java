package com.qiyue.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -257501844091438213L;

	private Element element;
	
	private List<Node> children = new ArrayList<Node>();
	private Node parent;
	
	public int height = 1;
	public boolean hasChild = false;
	public boolean hasParent = false;
	
	public Node() {
		
	}
	
	public Node(Element element) {
		this.element = element;
	}
}
