package com.qiyue.node;

public class NodeTree {

	public static boolean insert(Node node,Element ele) {
		boolean flag = false;
		if (node.getElement() == null) {
			node.setElement(ele);
			flag = true;
		} else {
			String code = node.getElement().getCode();
			String supCode = ele.getSupCode();
			if (code.equals(supCode)) {
				Node subNode = new Node(ele);
				subNode.setParent(new Node(node.getElement()));
				subNode.hasParent = true;
				node.getChildren().add(subNode);
				node.hasChild = true;
				flag = true;
			} else {
				for (Node child:node.getChildren()) {
					return insert(child,ele);
				}
			}
		}
		if (node.hasChild) {
			node.height = node.getChildren().get(0).getHeight() +1;
		}
		return flag;
	}
}
